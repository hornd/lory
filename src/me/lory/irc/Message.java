package me.lory.irc;

import me.lory.EMessageType;
import me.lory.IConversation;
import me.lory.IMessage;

public class Message implements IMessage {

	public static class Builder {
		private String msg;
		private IConversation convo;
		private EMessageType messageType;

		public Builder() {
			this.messageType = EMessageType.PRIVMSG;
		}

		public Builder setMessage(String msg) {
			this.msg = msg;
			return this;
		}

		public Builder setTarget(IConversation convo) {
			this.convo = convo;
			return this;
		}

		public Builder setMessageType(EMessageType type) {
			this.messageType = type;
			return this;
		}

		public IMessage build() throws MessageBuilderException {
			if (!this.canBuild()) {
				throw new MessageBuilderException();
			}

			return new Message(this.convo, this.messageType, this.msg);
		}

		private boolean canBuild() {
			return this.msg != null && (this.convo != null || !this.messageType.isRequireTarget());
		}
	}

	private final String message;
	private final IConversation convo;
	private final EMessageType type;

	private String compiled;

	Message(IConversation convo, EMessageType type, String message) {
		this.message = message;
		this.convo = convo;
		this.type = type;
		this.compiled = null;
	}

	@Override
	public EMessageType getMessageType() {
		return EMessageType.PRIVMSG;
	}

	@Override
	public String getMessage() {
		if (this.compiled == null) {
			StringBuilder sb = new StringBuilder();
			sb.append(this.type.toString());
			sb.append(" ");
			if (this.type.isRequireTarget()) {
				sb.append(this.convo.getName());
				sb.append(" ");
			}
			sb.append(this.message);
			this.compiled = sb.toString();
		}

		return this.compiled;
	}
}
