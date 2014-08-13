package me.lory;

/**
 * Defines a set of messages that tell lory to do a certain task (launch a new
 * server, disconnect, etc).
 * 
 * @author hornd
 *
 */
public enum EControlMessage {
	CONNECT("/connect"), DISCONNECT("/disconnect"), JOIN("/join"), NICK("/nick"), LIST("/list");

	private String messagePrefix;

	private EControlMessage(String messagePrefix) {
		this.messagePrefix = messagePrefix;
	}

	public static EControlMessage getMessageType(String prefix) {
		prefix = prefix.split(" ")[0];
		for (EControlMessage msg : EControlMessage.values()) {
			if (msg.messagePrefix.equals(prefix)) {
				return msg;
			}
		}
		return null;
	}
}
