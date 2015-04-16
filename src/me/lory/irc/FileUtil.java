package me.lory.irc;

import java.io.File;
import java.io.IOException;

/**
 * Various utilities methods for interacting with the filesystem.
 * @author hornd
 */
public class FileUtil {
	private static final String LORY_DIR_NAME = ".lory";
	private static final String LORY_CONFIG_FILE = "lory.conf";
	
	public static File getOrCreateUserConfigFile() throws IOException {
		File dir = new File(getLoryUserDirectory());
		dir.mkdir();
		
		String cfgName = getLoryUserDirectory() + File.separator + LORY_CONFIG_FILE;
		File cfgFile = new File(cfgName);
		cfgFile.createNewFile();
		
		return cfgFile;
	}
	
	private static String getLoryUserDirectory() {
		return System.getProperty("user.home") + File.separator + LORY_DIR_NAME;
	}
}
