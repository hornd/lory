package me.lory.irc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import me.lory.EMessageType;
import me.lory.IConversation;
import me.lory.IMessage;
import me.lory.IMessage.MessageBuilderException;
import me.lory.IServerMessageDispatcher;
import me.lory.irc.message.MessageFactory;

public class Conversation implements IConversation {

	private IServerMessageDispatcher dispatcher;
	private final List<IMessage> mailbox;
	private final String name;
	private final boolean isStatus;

	public Conversation(String name, boolean isStatus) {
		this.name = name;
		this.isStatus = isStatus;
		this.mailbox = new ArrayList<IMessage>();
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void send(EMessageType type, String contents) {
		try {
			IMessage msg = MessageFactory.compileEgress(type, this.getName(), contents);
			this.dispatcher.sendMessage(msg);
		} catch (MessageBuilderException exc) {
			// TODO:
			exc.printStackTrace();
		}
	}

	public void queueReceived(IMessage msg) {
		synchronized (this.mailbox) {
			this.mailbox.add(msg);
		}
	}

	@Override
	public Collection<IMessage> getRecent() {
		Collection<IMessage> ret = new ArrayList<IMessage>();
		synchronized (this.mailbox) {
			ret.addAll(this.mailbox);
		}
		return ret;
	}

	@Override
	public void setDispatcher(IServerMessageDispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	@Override
	public boolean isStatus() {
		return this.isStatus;
	}

}
