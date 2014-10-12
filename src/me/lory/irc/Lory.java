package me.lory.irc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.lory.irc.cli.Shell;

public class Lory {

	private static class Option {
		private String name;
		private String help;
		private Runnable action;

		private Option(String name, Runnable action) {
			this(name, null, action);
		}

		private Option(String name, String help, Runnable action) {
			this.name = name;
			this.help = help;
			this.action = action;
		}

		private void executeAction() {
			action.run();
		}

		private boolean hasHelpString() {
			return this.help != null;
		}

	}

	public static class Flags {
		public static boolean cli = false;
		public static boolean help = false;
	}

	public static final Logger LOG = Logger.getLogger("lory");
	static {
		LOG.setLevel(Level.FINE);
	}

	private static final Option[] options = new Option[] {
			new Option("--cli", "Runs lory in CLI mode.", () -> Flags.cli = true),
			new Option("--help", () -> Flags.help = true) };

	private static Option getOption(String s) {
		for (Option option : options) {
			if (option.name.equals(s)) {
				return option;
			}
		}
		return null;
	}

	private static boolean parseArg(String[] args) {
		for (int i = 0; i < args.length; i++) {
			Option option = getOption(args[i]);
			if (option == null) {
				return false;
			}
			option.executeAction();
		}
		return true;
	}

	private static void dumpHelp() {
		System.out.println("Lory - An IRC Client\n");
		for (Option s : options) {
			System.out.print(s.name);
			if (s.hasHelpString()) {
				System.out.print("\t" + s.help);
			}
			System.out.println();
		}
	}

	public static void launchCliMode() {
		new Shell().go();
	}

	public static void main(String[] args) {
	    Configuration config = getConfiguration();
		boolean parseArgSuccess = parseArg(args);

		if (Flags.help || !parseArgSuccess) {
			dumpHelp();
			return;
		}

		if (Flags.cli) {
			launchCliMode();
		} else {
			System.out.println("GUI not yet supported.");
		}
	}
	
	private static Configuration getConfiguration() {
	    Configuration config = null;
	    try {
            File configFile = getConfigFile();
            BufferedReader reader = new BufferedReader(new FileReader(configFile));
            Configuration.Parser parser = new Configuration.Parser(reader);
            config = parser.parse();
        } catch (IOException|SecurityException e) {
            LOG.log(Level.WARNING, "Error reading configuration file.");
        }
	    
	    return config;
	}
	
	private static File getConfigFile() throws IOException, SecurityException {
	    File f = new File(System.getProperty("user.home") + File.separator + ".lory" + File.separator + "lory.conf");
	    if (!f.exists()) {
	        f.createNewFile();
	    }
	    return f;
	}
}
