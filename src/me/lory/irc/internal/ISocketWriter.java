package me.lory.irc.internal;

import java.io.IOException;

public interface ISocketWriter {
	/**
	 * Sends a message over a socket. Throws exception if not
	 * connected.
	 * @param msg
	 * @throws IOException if not connected, or on any error in the unerlying socket.
	 */
	void send(String msg) throws IOException;
}
