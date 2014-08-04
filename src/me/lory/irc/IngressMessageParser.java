package me.lory.irc;

import java.util.List;

import me.lory.EMessageType;
import me.lory.IConversation;
import me.lory.IMessage.MessageBuilderException;

public class IngressMessageParser {
	public static class MessageParserException extends Exception {
		public MessageParserException() {
			super();
		}

		public MessageParserException(String msg) {
			super(msg);
		}

		private static final long serialVersionUID = 1L;
	}

	private final String message;

	IngressMessageParser(String message) {
		this.message = message;
	}

	/**
	 * Parse the message for the {@link EMessageType}.
	 * 
	 * @return
	 * @throws MessageParserException
	 */
	EMessageType getType() throws MessageParserException {
		String[] splitMsg = this.message.split(" ");
		if (splitMsg == null || splitMsg.length < 2) {
			throw new MessageParserException();
		}

		String msgTypeStr = splitMsg[1];
		EMessageType ret = EMessageType.getTypeFromStr(msgTypeStr);
		if (ret == null) {
			throw new MessageParserException();
		}

		return ret;
	}

	IConversation getTarget(List<IConversation> targets, EMessageType type) throws MessageParserException {
		if (type != EMessageType.PRIVMSG) {
			throw new MessageParserException("TODO TODO");
		}

		// IConversation ret = getStatusConversation(targets);
		// TODO: Non PRIVMSG should (could) return status by default.
		IConversation ret = null;

		String sender = this.getMessageSender(this.message);
		String recipient = this.getMessageRecipient(this.message);
		
		String convoName = isChannel(recipient) ? recipient : sender;
		
		for (IConversation target : targets) {
			if (target.getName().equals(convoName)) {
				ret = target;
			}
		}

		return ret;
	}
	
	private boolean isChannel(String message) {
		return message.startsWith("#");
	}
	
	private String getMessageRecipient(String message) throws MessageParserException {
		String[] split = this.message.split(" ");
		if (split == null || split.length < 3) {
			throw new MessageParserException();
		}
		
		return split[2];
	}
	
	private String getMessageSender(String message) throws MessageParserException {
		String[] split = this.message.split(":");
		if (split == null || split.length < 2) {
			throw new MessageParserException();
		}
		
		String[] convoNames = split[1].split("!");
		if (convoNames == null || convoNames.length < 1) {
			throw new MessageParserException();
		}

		return convoNames[0];
	}

	String getMessage(EMessageType type) throws MessageParserException {
		if (type != EMessageType.PRIVMSG) {
			throw new MessageParserException("TODO TODO");
		}

		String[] spl = message.split(":");
		if (spl == null || spl.length < 3) {
			throw new MessageParserException();
		}

		return spl[2];
	}

	@SuppressWarnings("unused")
	private static IConversation getStatusConversation(List<IConversation> targets) throws MessageBuilderException {
		for (IConversation conversation : targets) {
			if (conversation.isStatus())
				return conversation;
		}
		throw new MessageBuilderException();
	}
}
