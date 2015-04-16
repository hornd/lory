package me.lory.irc;

import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.junit.Test;

public class TestFileUtil {
	@Test
	public void testGetConfig() throws Exception {
		File cfg = FileUtil.getOrCreateUserConfigFile();
		assertNotNull(cfg);
	}
}
