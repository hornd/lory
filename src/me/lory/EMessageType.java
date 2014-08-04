package me.lory;

/**
 * Defines the various supported message types.
 * 
 * @author hornd
 */
public enum EMessageType {
	/**
	 * Used to send messages between clients.
	 */
	PRIVMSG(true),

	/**
	 * Message used to test the connection of a client to a server.
	 */
	PING(false),

	/**
	 * Message used to indicate that the client has requested a nick change.
	 */
	NICK(false),

	/**
	 * Message used to register the USER field of a connection.
	 */
	USER(false),

	/**
	 * A reply to a PING message.
	 */
	PONG(false);

	// TODO: message id from rfc.
	@SuppressWarnings("unused")
	private final Integer id;
	private final boolean requiresTarget;

	private EMessageType(boolean requiresTarget) {
		this(requiresTarget, null);
	}

	private EMessageType(boolean requiresTarget, Integer messageId) {
		this.requiresTarget = requiresTarget;
		this.id = messageId;
	}

	public boolean isRequireTarget() {
		return this.requiresTarget;
	}

	public static EMessageType getTypeFromStr(String string) {
		for (EMessageType type : EMessageType.values()) {
			if (type.toString().equals(string))
				return type;
		}
		return null;
	}
}
