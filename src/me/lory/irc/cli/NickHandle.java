package me.lory.irc.cli;

import me.lory.irc.User;

final class NickHandle implements IControlHandler {

	private final IShell shell;

	NickHandle(IShell shell) {
		this.shell = shell;
	}

	@Override
	public void handle(String fullMsg) throws IllegalArgumentException {
		String[] args = fullMsg.split(" ");
		if (args == null || args.length < 2) {
			throw new IllegalArgumentException();
		}

		String nick = args[1];

		if (!isNickLegal(nick)) {
			throw new IllegalArgumentException();
		}

		this.shell.setUser(new User(nick, "Soe dude"));
	}

	// TODO.
	private boolean isNickLegal(String nick) {
		return true;
	}
}
