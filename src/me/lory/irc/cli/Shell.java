package me.lory.irc.cli;

import java.util.HashMap;
import java.util.IllegalFormatException;
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
import me.lory.irc.internal.IServerConnection;

public class Shell {

	private IServer server;
	private IConversation activeConversation;
	/**
	 * Maintain a reference to the status conversation so we can properly handle
	 * PING, CTCP etc.
	 */
	private IConversation statusConversation;
	private User user;

	private final ExecutorService exec = Executors.newFixedThreadPool(1);

	private Map<EControlMessage, IControlHandler> inputHandlers = new HashMap<EControlMessage, IControlHandler>();
	{
		inputHandlers.put(EControlMessage.CONNECT, new ConnectHandle());
		inputHandlers.put(EControlMessage.NICK, new NickHandle());
	}

	private Map<EMessageType, IControlHandler> outputHandlers = new HashMap<EMessageType, IControlHandler>();
	{
		outputHandlers.put(EMessageType.PING, new PingHandle());
	}

	private interface IControlHandler {
		void handle(String fullMsg);
	}

	public Shell() {
		this.server = null;
		this.user = null;
		this.activeConversation = null;
		this.statusConversation = null;
	}

	public void go() {
		System.out.println("Lory cli");
		try (Scanner s = new Scanner(System.in)) {
			while (true) {
				String n = s.nextLine();
				IControlHandler handler = this.inputHandlers.get(EControlMessage.getMessageType(n));
				if (handler != null) {
					handler.handle(n);
				} else if (this.activeConversation != null) {
					this.activeConversation.send(EMessageType.PRIVMSG, n);
				}
			}
		}
	}

	class ShellOut implements Runnable {
		@Override
		public void run() {
			for (IMessage msg : Shell.this.statusConversation.getRecent()) {
				IControlHandler handle = Shell.this.outputHandlers.get(msg.getMessageType());
				if (handle != null) {
					handle.handle(msg.getMessage());
				} else {
					System.out.println(msg.getMessage());
				}
			}
		}
	}

	private class PingHandle implements IControlHandler {
		@Override
		public void handle(String fullMsg) {
			Shell.this.statusConversation.send(EMessageType.PONG, "PONG");
		}
	}

	private class NickHandle implements IControlHandler {
		@Override
		public void handle(String fullMsg) {
			String[] args = fullMsg.split(" ");
			String nick = args[1];
			Shell.this.user = new User(nick, "Soe dude");
			if (Shell.this.server != null && Shell.this.server.isConnected()) {
				Shell.this.activeConversation.send(EMessageType.USER, Shell.this.user.getRealName()
						+ " 192.33.22.22 irc.freenode.net :TODO");
			}
		}
	}

	private class ConnectHandle implements IControlHandler {
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
					});

					Shell.this.exec.execute(new ShellOut());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}