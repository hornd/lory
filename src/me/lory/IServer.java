package me.lory;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Collection;

import me.lory.irc.ServerDescription;

/**
 * Describes an IRC server and a connection to that IRC server. Servers are
 * described by {@link ServerDescription}.
 * 
 * @author hornd
 *
 */
public interface IServer extends IServerMessageDispatcher {

	/**
	 * Connect to this server.
	 * 
	 * @throws IOException
	 * @throws UnknownHostException
	 */
	void connect() throws IOException, UnknownHostException;

	/**
	 * Disconnect from this server.
	 * 
	 * @throws IOException
	 */
	void disconnect() throws IOException;

	/**
	 * Returns true if connected, false otherwise.
	 */
	boolean isConnected();

	/**
	 * Return the collection of conversations associated with this server.
	 * 
	 * @return
	 */
	Collection<IConversation> getConversations();

	/**
	 * Create a new conversation belonging to this server. A conversation can be
	 * created either via an incoming message with an unknown sender, or a user
	 * command such as /join, /query or /msg.
	 * 
	 * @param name
	 * @return
	 */
	IConversation openConversation(String name);
	
	/**
	 * Close a conversation.
	 * @param conversation
	 */
	void closeConversation(IConversation conversation);

	/**
	 * Return the status conversation associated with this server.
	 * 
	 * @return
	 */
	IConversation getStatusConversation();
}
