package me.lory.irc;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

import me.lory.IConversation;
import me.lory.IMessage;
import me.lory.IRawMessage;
import me.lory.IServer;
import me.lory.irc.internal.IServerConnection;
import me.lory.irc.internal.ISocketReader;
import me.lory.irc.internal.ISocketWriter;
import me.lory.irc.message.IngressMessageCompiler;
import me.lory.irc.message.MessageParserException;
import me.lory.irc.message.RawMessage;

/**
 * Implements the IServer interface describing a connection to an IRC server.
 * 
 * A server is made up of a socket connection and a list of {@link IConversation}s. This
 * implementation uses two separate {@link ExecutorService}s to poll for incoming and outgoing
 * messages.
 * 
 * @author hornd
 *
 */
public class Server implements IServer {
	private final IServerConnection connection;
	// Should conversations be handled elsewhere?
	private final List<IConversation> conversations;
	private final ConversationFactory factory;

	private IConversation status;

	private ExecutorService egressPoll;
	private ExecutorService ingressPoll;

	private ConcurrentLinkedQueue<IMessage> clientToServer;

	public Server(IServerConnection connection) {
		this.connection = connection;

		this.conversations = Collections.synchronizedList(new ArrayList<IConversation>());
		this.clientToServer = new ConcurrentLinkedQueue<IMessage>();
		this.factory = new ConversationFactory(this);
	}

	@Override
	public void connect() throws IOException, UnknownHostException {
		this.egressPoll = Executors.newFixedThreadPool(1);
		this.ingressPoll = Executors.newFixedThreadPool(1);

		this.connection.connect();

		this.status = this.factory.createStatusConversation();

		this.egressPoll.execute(new EgressProcessor(this.connection));
		this.ingressPoll.execute(new IngressProcessor(this.connection));
	}

	@Override
	public void disconnect() throws IOException {
		this.connection.disconnect();
		this.ingressPoll.shutdown();
		this.egressPoll.shutdown();
	}

	@Override
	public boolean isConnected() {
		return this.connection.isConnected();
	}

	@Override
	public IConversation getStatusConversation() {
		return this.status;
	}

	@Override
	public Collection<IConversation> getConversations() {
		return new ArrayList<IConversation>(this.conversations);
	}

	@Override
	public IConversation openConversation(String name) {
		Lory.LOG.log(Level.FINE, String.format("Opening conversation: %s", name));
		IConversation convo = this.factory.createConversation(name);
		this.conversations.add(convo);
		return convo;
	}

	@Override
	public void closeConversation(IConversation conversation) {
		this.conversations.remove(conversation);
	}

	@Override
	public void sendMessage(IMessage message) {
		if (message != null) {
			this.clientToServer.add(message);
		}
	}

	private class EgressProcessor implements Runnable {

		private ISocketWriter egress;

		private EgressProcessor(ISocketWriter egress) {
			this.egress = egress;
		}

		@Override
		public void run() {
			while (true) {
				IMessage toEgress = Server.this.clientToServer.poll();
				if (toEgress != null) {
					try {
						this.egress.send(toEgress.getMessage());
					} catch (IOException e) {
						// TODO: Log.
					}
				}
			}
		}
	}

	private class IngressProcessor implements Runnable {
		private ISocketReader ingress;

		private IngressProcessor(ISocketReader ingress) {
			this.ingress = ingress;
		}

		@Override
		public void run() {
			while (true) {
				String rec;
				try {
					rec = this.ingress.recv();
				} catch (IOException e) {
					// TODO: Log.
					rec = null;
				}

				if (rec != null) {
					try {
						IRawMessage rawMsg = RawMessage.create(rec);

						IMessage msg = IngressMessageCompiler.compile(rawMsg);
						IConversation convo = this.getConversationForIngressMsg(msg);

						if (convo == null) {
							convo = Server.this.openConversation(msg.getTarget());
						}

						Lory.LOG.log(Level.FINE, String.format("Queuing %s on %s", msg, convo.getName()));

						convo.queueReceived(msg);
					} catch (MessageParserException e) {
						Lory.LOG.log(Level.SEVERE, String.format("Unrecognized message! %s", rec));
					}
				}
			}
		}

		public IConversation getConversationForIngressMsg(IMessage msg) {
			IConversation ret = null;
			List<IConversation> convos = new ArrayList<IConversation>(Server.this.conversations);
			for (IConversation convo : convos) {
				if (convo.getName().equals(msg.getTarget())) {
					ret = convo;
					break;
				}
			}

			// TODO
			if (msg.getTarget().equals("(status)")) {
				ret = Server.this.status;
			}

			return ret;
		}
	}
}
