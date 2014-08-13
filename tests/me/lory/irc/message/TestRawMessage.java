package me.lory.irc.message;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

/**
 * Test the {@link RawMessage} class.
 * 
 * @author hornd
 *
 */
public class TestRawMessage {
    @Test
    public void testUserMsg() throws Exception {
        String userMssage = ":anotherUser!alias@unaffiliated/omehtin/x-4957395 PRIVMSG someUser :this is a test message";
        RawMessage parts = RawMessage.create(userMssage);
        assertTrue(parts.hasPrefix());
        assertEquals(":anotherUser!alias@unaffiliated/omehtin/x-4957395", parts.getPrefix());
        assertEquals("PRIVMSG", parts.getCommand());
        assertArrayEquals(new String[] { "someUser", "this is a test message" }, parts.getParams());
    }
    
    @Test(expected=MessageParserException.class) 
    public void testInvalidMessage() throws Exception {
        String userMsg = "noprefixnocommandnoparams";
        RawMessage.create(userMsg);
    }
}
