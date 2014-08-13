package me.lory.irc.cli;

import me.lory.IConversation;
import me.lory.IServer;
import me.lory.irc.User;

/**
 * Defines the interface exposed by a CLI.
 * 
 * @author hornd
 *
 */
interface IShell {
	/**
	 * Retrieve the currently connected server. Returns null if not currently
	 * connected.
	 * 
	 * @return Server associated with this CLI.
	 */
	IServer getServer();

	/**
	 * Return the status conversation associated with this CLI. Returns null if
	 * not currently connected.
	 * 
	 * @return Status conversation.
	 */
	IConversation getStatusConversation();

	/**
	 * Return the active conversation associated with this CLI. Returns null if
	 * not currently connected.
	 * 
	 * @return Active conversation.
	 */
	IConversation getActiveConversation();
	
	/**
	 * Sets a new user.
	 * @param user
	 */
	void setUser(User user);
}
