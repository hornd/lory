package me.lory;

/**
 * An IRC message.
 * 
 * @author hornd
 *
 */
public interface IMessage {
	public static class MessageBuilderException extends Exception {
		public MessageBuilderException() {
			super();
		}

		public MessageBuilderException(String msg) {
			super(msg);
		}

		private static final long serialVersionUID = 1L;
	}

	/**
	 * Returns the {@link EMessageType} associated with this message.
	 * 
	 * @return
	 */
	EMessageType getMessageType();

	/**
	 * Returns the contents of this message.
	 * 
	 * @return
	 */
	String getMessage();
}
