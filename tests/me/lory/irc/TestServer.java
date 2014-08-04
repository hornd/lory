package me.lory.irc;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import me.lory.IServer;
import me.lory.irc.internal.IServerConnection;
import me.lory.irc.mocks.MockServerConnection;

import org.junit.Test;

public class TestServer {
	@Test
	public void isConnected() throws Exception {
		IServerConnection con = new MockServerConnection();
		IServer server = new Server(con);
		
		assertFalse("Expected disconnected server", server.isConnected());
		server.connect();
		assertTrue("Expected connected server", server.isConnected());
		server.disconnect();
		assertFalse("Expected disconnected server", server.isConnected());
	}
}
