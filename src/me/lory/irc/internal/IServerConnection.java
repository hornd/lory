package me.lory.irc.internal;

import java.io.IOException;
import java.net.UnknownHostException;

/**
 * Defines the connection with a server.
 * 
 * @author hornd
 *
 */
public interface IServerConnection extends ISocketWriter, ISocketReader {
	/**
	 * Connect this connection.
	 */
	void connect() throws IOException, UnknownHostException;

	/**
	 * Disconnect from this connection.
	 */
	void disconnect() throws IOException;

	/**
	 * @return True if connected, false otherwise.
	 */
	boolean isConnected();
}
