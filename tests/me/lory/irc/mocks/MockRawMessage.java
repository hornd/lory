package me.lory.irc.mocks;

import me.lory.IMessage;
import me.lory.IRawMessage;

public class MockRawMessage implements IRawMessage {

    private final String prefix;
    private final String command;
    private final String msg;
    private final String[] params;

    public MockRawMessage(String prefix, String command, String msg, String[] params) {
        this.prefix = prefix;
        this.command = command;
        this.params = params;
        this.msg = msg;
    }

    @Override
    public boolean hasPrefix() {
        return this.prefix != null;
    }

    @Override
    public String getMessage() {
        return this.msg;
    }

    @Override
    public String getPrefix() {
        return this.prefix;
    }

    @Override
    public String getCommand() {
        return this.command;
    }

    @Override
    public String[] getParams() {
        return this.params;
    }

    @Override
    public IMessage compile() {
        throw new UnsupportedOperationException();
    }

}
