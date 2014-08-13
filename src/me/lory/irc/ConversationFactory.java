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

	public IConversation createStatusConversation() {
		return this.create("Status", true);
	}

	public IConversation createConversation(String name) {
		return this.create(name, false);
	}
	
	private IConversation create(String name, boolean isStatus) {
		IConversation ret = new Conversation(name, isStatus);
		ret.setDispatcher(this.server);
		return ret;
	}
}
