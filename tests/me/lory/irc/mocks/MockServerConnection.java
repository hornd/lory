package me.lory.irc.mocks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.UnknownHostException;

import me.lory.irc.internal.IServerConnection;

import org.mockito.Mockito;

/**
 * Mocks a server connection.
 * 
 * @author hornd
 *
 */
public class MockServerConnection implements IServerConnection {

	private volatile boolean isConnected;
	
	private BufferedReader ingress;
	private PrintWriter egress;
	
	public MockServerConnection() {
		this(new BufferedReader(Mockito.mock(BufferedReader.class)), Mockito.mock(PrintWriter.class));
	}
	
	public MockServerConnection(BufferedReader ingress, PrintWriter egress) {
		this.ingress = ingress;
		this.egress = egress;
	}
	
	@Override
	public void connect() throws IOException, UnknownHostException {
		this.isConnected = true;
	}

	@Override
	public void disconnect() throws IOException {
		this.isConnected = false;
	}

	@Override
	public boolean isConnected() {
		return this.isConnected;
	}

	@Override
	public void send(String msg) throws IOException {
		this.egress.print(msg);
		this.egress.flush();
	}

	@Override
	public String recv() throws IOException {
		return this.ingress.readLine();
	}
}
