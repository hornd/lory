package me.lory.irc.cli;

/**
 * This interface describes handlers that complete specific actions in response
 * to stimulus from the user, or from the server.
 * 
 * @author hornd
 *
 */
interface IControlHandler {
	class IllegalServerStateException extends Exception {
		private static final long serialVersionUID = 1L;

		public IllegalServerStateException(String s) {
			super(s);
		}
	}
	
	void handle(String fullMsg) throws IllegalArgumentException, IllegalServerStateException;
}
