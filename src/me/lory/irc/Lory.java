package me.lory.irc;

import java.util.Scanner;
import java.util.logging.Logger;

import me.lory.EMessageType;
import me.lory.IMessage;
import me.lory.irc.cli.Shell;

public class Lory {

	private static class Option {
		private String name;
		private String help;
		private Runnable action;

		private Option(String name, String help, Runnable action) {
			this.name = name;
			this.help = help;
			this.action = action;
		}

		public void executeAction() {
			action.run();
		}
	}

	public static class Configuration {
		public static boolean cli = false;
	}

	public static final Logger LOG = Logger.getLogger("lory");

	private static final Option[] options = new Option[] {
			new Option("--cli", "Runs lory in CLI mode.", () -> Configuration.cli = true),
			new Option("--help", "TODO", () -> System.out.println("Help")) };

	private static Option getOption(String s) {
		for (Option option : options) {
			if (option.name.equals(s)) {
				return option;
			}
		}
		return null;
	}

	private static boolean parseArg(String[] args) {
		// TODO
		return true;
	}

	public static void main(String[] args) {
		if (true) {
			Shell a = new Shell();
			a.go();
			return;
		}
		ServerDescription des = new ServerDescription("Freenode", "irc.freenode.net", 6667);
		Server a = new Server(new ServerConnection(des));
		try {
			a.connect();
		} catch (Exception e) {
			e.printStackTrace();
		}

		while (!a.isConnected())
			;

		try {
			a.sendMessage(new MsgDbg("NICK testtet2222"));
			a.sendMessage(new MsgDbg("USER testtest2222 192.33.22.22 irc.freenode.net :Some Guy"));

			for (int i = 0; i < 1000; i++) {
				a.sendMessage(null);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		while (a.isConnected()) { /* Yay */
		}
	}

	private static class MsgDbg implements IMessage {

		private String mg;

		public MsgDbg(String mg) {
			this.mg = mg;
		}

		@Override
		public EMessageType getMessageType() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getMessage() {
			return mg;
		}

	}
}
