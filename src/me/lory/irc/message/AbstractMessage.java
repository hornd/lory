package me.lory.irc.message;

import me.lory.EMessageType;
import me.lory.IMessage;

abstract public class AbstractMessage implements IMessage {
	protected EMessageType type;
	protected String target;

	public AbstractMessage(EMessageType type, String target) {
		this.type = type;
		this.target = target;
	}

	@Override
	public EMessageType getMessageType() {
		return this.type;
	}

	@Override
	public String getTarget() {
		return this.target;
	}

	public abstract String getMessage();
}
