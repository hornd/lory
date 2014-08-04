package me.lory.irc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import me.lory.irc.internal.IServerConnection;

public class ServerConnection implements IServerConnection {

	private static final String LINE_SEP = System.getProperty("line.separator");

	ServerDescription description;

	private Socket socket;

	private BufferedReader ingress;
	private PrintWriter egress;

	private volatile boolean isConnected;

	public ServerConnection(ServerDescription description) {
		this.description = description;
		this.isConnected = false;
	}

	@Override
	public void connect() throws IOException, UnknownHostException {
		this.socket = new Socket(this.description.getHost(), this.description.getPort());
		this.ingress = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.egress = new PrintWriter(socket.getOutputStream(), false);
		this.isConnected = true;
	}

	@Override
	public void disconnect() throws IOException {
		if (this.isConnected()) {
			this.isConnected = false;
			this.socket.close();
		}
	}

	@Override
	public boolean isConnected() {
		return this.isConnected;
	}

	@Override
	public void send(String msg) throws IOException {
		if (!this.isConnected) {
			throw new IOException("[ServerConnection] Cannot send -- not connected to server");
		}

		if (msg != null) {
			String out = msg.endsWith(LINE_SEP) ? msg : (msg + LINE_SEP);

			this.egress.print(out);
			this.egress.flush();
		}
	}

	@Override
	public String recv() throws IOException {
		if (!this.isConnected) {
			throw new IOException("[ServerConnection] Cannot recv -- not connected to server");
		}

		return this.ingress.readLine();
	}

}
