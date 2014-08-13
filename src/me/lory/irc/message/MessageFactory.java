package me.lory.irc.message;

import me.lory.EMessageType;
import me.lory.IMessage;
import me.lory.IMessage.MessageBuilderException;

public class MessageFactory {
	// Total max message length (command + parameters). -2 for CRLF.
	private static final int MAX_LENGTH = 510;

	public static IMessage compileEgress(EMessageType type, String target, String contents)
			throws MessageBuilderException {
		if (type.toString().length() + contents.length() > MAX_LENGTH) {
			throw new MessageBuilderException("Message too long");
		}

		return new EgressMessage(type, target, contents);
	}
}
