package me.lory.irc;

import me.lory.IConversation;
import me.lory.IServer;

/**
 * Handles creation of Conversations on a per-server basis.
 * 
 * @author hornd
 *
 */
public class ConversationFactory {
	private final IServer server;

	public ConversationFactory(IServer server) {
		this.server = server;
	}

	public synchronized IConversation createStatusConversation() {
		IConversation ret = new Conversation("Status", true);
		ret.setDispatcher(this.server);
		return ret;
	}

	// TODO.
	public synchronized IConversation createConversation(String name) {
		throw new UnsupportedOperationException();
	}
}
