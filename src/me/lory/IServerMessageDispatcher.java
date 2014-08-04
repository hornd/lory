package me.lory;

/**
 * Defines the interface between a server and a conversation.
 * 
 * Implementations are thread-safe.
 * 
 * @author hornd
 *
 */
public interface IServerMessageDispatcher {

	/**
	 * Send the message to the server associated with this dispatcher.
	 * 
	 * @param message
	 */
	void sendMessage(IMessage message);
}
