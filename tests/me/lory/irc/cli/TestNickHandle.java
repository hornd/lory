package me.lory.irc.cli;

import org.junit.Test;
import org.mockito.Mockito;

/**
 * Simple tests for the /nick handler.
 * 
 * TODO: Test illegal nicks.
 * 
 * @author hornd
 *
 */
public class TestNickHandle {
	/**
	 * Verify that valid nicks do not throw an exception, and that subsequent
	 * arguments after one are ignored.
	 */
	@Test
	public void testValid() {
		IShell shell = Mockito.mock(IShell.class);

		NickHandle handle = new NickHandle(shell);

		handle.handle("/nick asdf");
		handle.handle("/nick a");
		handle.handle("/nick SFT");
		handle.handle("/nick a a a a a ");
	}

	@Test(expected=IllegalArgumentException.class)
	public void testNotEnoughArgs() {
		IShell shell = Mockito.mock(IShell.class);
		
		NickHandle handle = new NickHandle(shell);
		
		handle.handle("/nick");
	}
}
