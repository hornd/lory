package me.lory.irc;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import me.lory.EMessageType;
import me.lory.IConversation;
import me.lory.irc.IngressMessageParser.MessageParserException;

import org.junit.Test;
import org.mockito.Mockito;

public class TestIngressMsgParser {

	/**
	 * Verify the message parsing and IMessage creation for ingress messages.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testPrivmsgParse() throws Exception {
		String userMssage = ":anotherUser!alias@unaffiliated/omehtin/x-4957395 PRIVMSG someUser :this is a test message";
		this.assertPrivMsgParsing(userMssage, "anotherUser", "this is a test message");
	}

	/**
	 * Verify the paring of a PRIVMSG from another user to a channel.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testPrivMsgChannel() throws Exception {
		String channelMessage = ":synx!hornd@unaffiliated/synx/x-4957395 PRIVMSG ##channel :hello friends";
		this.assertPrivMsgParsing(channelMessage, "##channel", "hello friends");
	}

	private void assertPrivMsgParsing(String fullMessage, String source, String msg) throws Exception {
		IConversation target = Mockito.mock(IConversation.class);
		Mockito.when(target.getName()).thenReturn(source);

		IngressMessageParser parser = new IngressMessageParser(fullMessage);
		assertEquals(EMessageType.PRIVMSG, parser.getType());
		assertEquals(target, parser.getTarget(Arrays.asList(target), EMessageType.PRIVMSG));
		assertEquals(msg, parser.getMessage(EMessageType.PRIVMSG));
	}

	@Test(expected = MessageParserException.class)
	public void testPrivMsgMessageBlank() throws Exception {
		String blank = ":synx!hornd@unaffiliated/synx/x-4957395 PRIVMSG ##channel :";

		IngressMessageParser parser = new IngressMessageParser(blank);
		parser.getMessage(EMessageType.PRIVMSG);
	}

	@Test(expected = MessageParserException.class)
	public void testUnsupportedType() throws Exception {
		String blank = ":synx!hornd@unaffiliated/synx/x-4957395 UNKNOWN ##channel :";

		IngressMessageParser parser = new IngressMessageParser(blank);
		parser.getMessage(EMessageType.PRIVMSG);
	}
}
