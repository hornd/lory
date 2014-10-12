package me.lory.irc.cli;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.IllegalFormatException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

import me.lory.EControlMessage;
import me.lory.EMessageType;
import me.lory.IConversation;
import me.lory.IMessage;
import me.lory.IServer;
import me.lory.irc.Lory;
import me.lory.irc.Server;
import me.lory.irc.ServerConnection;
import me.lory.irc.ServerDescription;
import me.lory.irc.User;
import me.lory.irc.cli.IControlHandler.IllegalServerStateException;
import me.lory.irc.internal.IServerConnection;

public class Shell implements IShell {

	private IServer server;
	private IConversation activeConversation;
	/**
	 * Maintain a reference to the status conversation so we can properly handle
	 * PING, CTCP etc.
	 */
	private IConversation statusConversation;

	/**
	 * The ShellOut class continuously polls the status connection to handle
	 * interactions, but this clears any {@link IMessage} objects that should
	 * just be displayed. We can hackily maintain a cache of them here, to be
	 * replayed when the activeConversation is set to status.
	 */
	private List<IMessage> statusBackup;

	private User user;

	private final ExecutorService exec = Executors.newFixedThreadPool(1);

	private Map<EControlMessage, IControlHandler> inputHandlers = new HashMap<EControlMessage, IControlHandler>();
	{
		inputHandlers.put(EControlMessage.CONNECT, new ConnectHandle());
		inputHandlers.put(EControlMessage.NICK, new NickHandle(this));
		inputHandlers.put(EControlMessage.LIST, new ListHandle(this));
	}

	private Map<EMessageType, IControlHandler> outputHandlers = new HashMap<EMessageType, IControlHandler>();
	{
		outputHandlers.put(EMessageType.PING, new PingHandle(this));
	}

	public Shell() {
		this.server = null;
		this.user = null;
		this.activeConversation = null;
		this.statusConversation = null;

		this.statusBackup = new ArrayList<IMessage>();
	}

	public void go() {
		System.out.println("*********************************************");
		System.out.println("Lory (CLI mode)");
		System.out.println("*********************************************\n");

		try (Scanner s = new Scanner(System.in)) {
			while (true) {
				String n = s.nextLine();
				IControlHandler handler = this.inputHandlers.get(EControlMessage.getMessageType(n));
				if (handler != null) {
					try {
						handler.handle(n);
					} catch (IllegalArgumentException e) {
						// Print help message.
					} catch (IllegalServerStateException e) {
						Lory.LOG.log(Level.SEVERE, "Not connected to server!");
					}
				} else if (this.activeConversation != null) {
					this.activeConversation.send(EMessageType.PRIVMSG, n);
				}
			}
		}
	}

	@Override
	public synchronized IServer getServer() {
		return this.server;
	}

	@Override
	public synchronized IConversation getStatusConversation() {
		return this.statusConversation;
	}

	@Override
	public synchronized IConversation getActiveConversation() {
		return this.activeConversation;
	}

	@Override
	public void setUser(User user) {
		this.user = user;
		if (this.server != null && this.server.isConnected()) {
			Shell.this.activeConversation.send(EMessageType.USER, Shell.this.user.getRealName()
					+ " 192.33.22.22 irc.freenode.net :TODO");
		}
	}

	class ShellOut implements Runnable {
		@Override
		public void run() {
			while (true) {
				for (IMessage msg : Shell.this.statusConversation.getRecent()) {
					IControlHandler handle = Shell.this.outputHandlers.get(msg.getMessageType());
					if (handle != null) {
						try {
							handle.handle(msg.getMessage());
						} catch (IllegalArgumentException|IllegalServerStateException e) {
							// TODO: Probably indicates a bug in handler...
							Lory.LOG.log(Level.SEVERE, e.getMessage());
						}
					} else {
						synchronized (Shell.this.statusBackup) {
							Shell.this.statusBackup.add(msg);
						}
					}
				}
			}
		}
	}

	private final class ConnectHandle implements IControlHandler {
		@Override
		public void handle(String fullMsg) {
			if (user == null) {
				Lory.LOG.log(Level.WARNING,
						"Please set a nick before connecting. Set a nick by typing /nick nickYouWant");
				return;
			}

			ServerDescription desc;
			try {
				desc = ServerDescription.createFromString(fullMsg);
			} catch (IllegalFormatException e) {
				Lory.LOG.log(Level.WARNING, "Invalid server format. Please use host:port");
				desc = null;
			}

			if (desc != null) {
				IServerConnection con = new ServerConnection(desc);

				if (Shell.this.server != null) {
					try {
						Shell.this.server.disconnect();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				Shell.this.server = new Server(con);
				try {
					Shell.this.server.connect();

					while (!Shell.this.server.isConnected())
						;

					Shell.this.activeConversation = Shell.this.server.getStatusConversation();
					Shell.this.statusConversation = Shell.this.server.getStatusConversation();
					Shell.this.activeConversation.send(EMessageType.NICK, Shell.this.user.getUserName());
					Shell.this.activeConversation.send(EMessageType.USER, Shell.this.user.getRealName()
							+ " 192.33.22.22 irc.freenode.net :TODO");

					// DEBUG
					Shell.this.server.sendMessage(new IMessage() {
						@Override
						public EMessageType getMessageType() {
							// TODO Auto-generated method stub
							return null;
						}

						@Override
						public String getMessage() {
							return "JOIN ##dbgchannel";
						}

						@Override
						public String getTarget() {
							return "";
						}
					});

					Shell.this.exec.execute(new ShellOut());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}