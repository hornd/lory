package me.lory.irc;

/**
 * Defines a user.
 * 
 * @author hornd
 *
 */
public class User {
	private String userName;
	private String realName;

	public User(String userName, String realName) throws IllegalArgumentException {
		this.realName = realName;
		this.setUserName(userName);
	}

	public String getUserName() {
		return this.userName;
	}

	public String getRealName() {
		return this.realName;
	}

	public final void setUserName(String uName) throws IllegalArgumentException {
		if (!this.validateUserName(uName)) {
			throw new IllegalArgumentException("Illegal username.");
		}

		this.userName = uName;
	}

	private boolean validateUserName(String userName) {
		// TODO.
		return true;
	}

	@Override
	public String toString() {
		return this.userName + " (" + this.realName + ")";
	}
}
