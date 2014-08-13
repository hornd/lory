package me.lory.irc.message;

public class MessageParserException extends Exception {
	public MessageParserException() {
		super();
	}

	public MessageParserException(String msg) {
		super(msg);
	}

	private static final long serialVersionUID = 1L;
}
