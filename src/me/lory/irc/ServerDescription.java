package me.lory.irc;

import java.util.IllegalFormatConversionException;
import java.util.IllegalFormatException;

/**
 * Describes a server.
 * 
 * @author dhorn
 *
 */
public class ServerDescription {
	private String name;
	private String host;
	private int port;

	public ServerDescription(String name, String host, int port) {
		this.name = name;
		this.host = host;
		this.port = port;
	}

	/**
	 * Parse the tring connection of the form host:port to form an instance of
	 * {@link ServerDescription}
	 * 
	 * @param connection
	 * @throws IllegalAr
	 * @return
	 */
	public static ServerDescription createFromString(String connection) throws IllegalArgumentException {
		ServerPort serverPort = getServerPortFromConnectionString(connection);
		return new ServerDescription(serverPort.tryGenerateName(), serverPort.host, serverPort.port);
	}

	// [0] = host, [1] = port.
	private static ServerPort getServerPortFromConnectionString(String connection) throws IllegalArgumentException {
		String[] split = connection.split(" ");
		if (split == null) {
			throw new IllegalArgumentException();
		}

		String[] serverPortCombo = split[split.length - 1].split(":");
		if (serverPortCombo == null || serverPortCombo.length != 2) {
			throw new IllegalArgumentException();
		}

		ServerPort ret;
		try {
			ret = new ServerPort(serverPortCombo[0], Integer.parseInt(serverPortCombo[1]));
		} catch (Exception e) {
			throw new IllegalArgumentException();
		}

		return ret;
	}

	public String getName() {
		return this.name;
	}

	public String getHost() {
		return this.host;
	}

	public int getPort() {
		return this.port;
	}

	private static class ServerPort {
		private final String host;
		private final int port;

		public ServerPort(String host, int port) {
			this.host = host;
			this.port = port;
		}

		public String tryGenerateName() {
			String[] hostSplit = this.host.split(".");
			if (hostSplit.length == 3) {
				return hostSplit[1];
			}
			return this.host;
		}
	}
}
