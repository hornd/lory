package me.lory.irc.internal;

import java.io.IOException;

public interface ISocketReader {
	/**
	 * Receive a message from this connection. Throws exception if not
	 * connected.
	 * 
	 * @return A full line from the read buffer.
	 * @throws IOException
	 *             If not connected, or on any error in the underlying socket.
	 */
	String recv() throws IOException;
}
