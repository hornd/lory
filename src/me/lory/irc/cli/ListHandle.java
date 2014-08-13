package me.lory.irc.cli;

import java.util.Collection;

import me.lory.IConversation;

final class ListHandle implements IControlHandler {
	private final IShell shell;

	ListHandle(IShell shell) {
		this.shell = shell;
	}

	@Override
	public void handle(String fullMsg) throws IllegalServerStateException {
		if (!this.shell.getServer().isConnected()) {
			throw new IllegalServerStateException("Not currently connected");
		}
		
		Collection<IConversation> convos = this.shell.getServer().getConversations();
		// TODO. Filter out PMs / status.
		System.out.println(String.format("Currently in %d channels:", convos.size()));
		
		int i = 0;
		for (IConversation convo : convos) {
			System.out.println(String.format("(%d) %s", i++, convo.getName()));
		}
	}
}
