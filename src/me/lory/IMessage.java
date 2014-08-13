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
	 * Returns the target of this message, as a string. The server should search
	 * for the appropriate {@link IConversation} that matches this target.
	 * 
	 * @return Target of this message.
	 */
	String getTarget();

	/**
	 * Returns the contents of this message.
	 * 
	 * @return
	 */
	String getMessage();
}
