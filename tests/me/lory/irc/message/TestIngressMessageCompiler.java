package me.lory.irc.message;

import me.lory.EMessageType;
import me.lory.IMessage;
import me.lory.irc.mocks.MockRawMessage;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TestIngressMessageCompiler {

    @Test
    public void testPrivateMessage() throws Exception {
        String prefix = "anotherUser!alias@unaffiliated/omehtin/x-23423";
        String command = "PRIVMSG";
        String[] params = new String[] { "someUser", "this is a test message" };

        MockRawMessage raw = new MockRawMessage(prefix, command, null, params);

        IMessage msg = IngressMessageCompiler.compile(raw);
        assertEquals(EMessageType.PRIVMSG, msg.getMessageType());
        assertEquals("this is a test message", msg.getMessage());
        assertEquals("anotherUser", msg.getTarget());
    }
    
    @Test
    public void testChannelMessage() throws Exception {
        String prefix = "synx!hornd@unaffiliated/synx/x-4957395";
        String command = "PRIVMSG";
        String[] params = new String[] { "##channel", "hello friends" };
        
        MockRawMessage raw = new MockRawMessage(prefix, command, null, params);

        IMessage msg = IngressMessageCompiler.compile(raw);
        assertEquals(EMessageType.PRIVMSG, msg.getMessageType());
        assertEquals("hello friends", msg.getMessage());
        assertEquals("##channel", msg.getTarget());
    }
}
