package me.lory.irc.cli;

import me.lory.EMessageType;

final class PingHandle implements IControlHandler {
	private final IShell shell;
	
	PingHandle(IShell shell) {
		this.shell = shell;
	}
	
	@Override
	public void handle(String fullMsg) {
		this.shell.getStatusConversation().send(EMessageType.PONG, "PONG");
	}
}
