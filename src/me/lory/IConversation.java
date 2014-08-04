package me.lory;

import java.util.Collection;

/**
 * Defines a conversation. A conversation can be a channel, a private message,
 * or server-status. 
 * 
 * Implementations are thread-safe
 * 
 * @author horn
 */
public interface IConversation {

	/**
	 * The name of this conversation. A conversation name can be another client
	 * or a channel.
	 * @return Name of this conversation.
	 */
	String getName();
	
	/**
	 * Send the parameter contents to this conversation. The contents parameter
	 * will be wrapped in an appropriate {@link IMessage} and sent to the server.
	 * @param message
	 */
	void send(EMessageType type, String contents);
	
	/**
	 * Returns the most recent messages that have been received in this conversation 
	 * since the last time getRecent() has been called, or all messages if this is the
	 * first time called. Returns an empty collection if there are no new messages.
	 */
	Collection<IMessage> getRecent();
	
	/**
	 * Queue a received message on this conversation.
	 * @param message
	 */
	void queueReceived(IMessage message);
	
	/**
	 * Attach the specified dispatcher to this conversation.
	 * @param dispatcher
	 */
	void setDispatcher(IServerMessageDispatcher dispatcher);
	
	/**
	 * Returns true if this conversation is a status conversation. A status conversation is a conversation
	 * that is auto-created on connection to the server, and handles various non-channel related communication.F
	 * @return
	 */
	boolean isStatus();
}
