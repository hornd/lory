package me.lory.irc.message;

import me.lory.EMessageType;
import me.lory.IMessage;

class EgressMessage extends AbstractMessage implements IMessage {

	private String compiled;
	private final String outgoing;

	public EgressMessage(EMessageType type, String target, String outgoing) {
		super(type, target);
		this.outgoing = outgoing;
	}

	@Override
	public String getMessage() {
		if (this.compiled == null) {
			StringBuilder sb = new StringBuilder();
			sb.append(this.type.toString());
			sb.append(" ");
			if (this.type.isRequireTarget()) {
				sb.append(this.target);
				sb.append(" ");
			}
			sb.append(this.outgoing);
			this.compiled = sb.toString();
		}

		return this.compiled;

	}

}
