package me.lory.irc.message;

import java.util.Arrays;

import me.lory.IMessage;
import me.lory.IRawMessage;

/**
 * An IRC message consists of three parts:
 * 
 * + An optional prefix - If a prefix exists, the message will be prepended with
 * a colon. + A command + A list of parameters.
 * 
 * The prefix, command, and list of parameters are seperated by one or more
 * ASCII spaces.
 * 
 * @author hornd
 *
 */
public final class RawMessage implements IRawMessage {
    private static final String PREFIX_PARAM_INDICATOR = ":";

    private final String raw;
    private final String prefix;
    private final String command;
    private final String[] params;

    private RawMessage(final String message) throws MessageParserException {
        String[] parts = message.split(" ");
        if (parts.length < 2) {
            throw new MessageParserException("Invalid message format.");
        }

        boolean hasPrefix = parts[0].startsWith(PREFIX_PARAM_INDICATOR);
        int commandIdx = hasPrefix ? 1 : 0;
        
        this.raw = message;
        this.prefix = hasPrefix ? parts[0].substring(1) : null;
        this.command = parts[commandIdx];
        this.params = this.getCoalescedParams(Arrays.copyOfRange(parts, commandIdx + 1, parts.length));
    }

    /**
     * Coalesce parameters that include a trailing by turning the trailing into
     * a single string param.
     * 
     * @param params
     * @return
     */
    private String[] getCoalescedParams(String[] params) {
        int trailIdx = this.getTrailingIndex(params);
        if (trailIdx < 0) {
            return params;
        }

        StringBuilder comb = new StringBuilder();
        comb.append(params[trailIdx].substring(1));
        for (int i = trailIdx + 1; i < params.length; i++) {
            comb.append(" ");
            comb.append(params[i]);
        }

        String[] ret = new String[trailIdx + 1];
        for (int i = 0; i < trailIdx; i++) {
            ret[i] = params[i];
        }

        ret[trailIdx] = comb.toString();
        return ret;
    }

    private int getTrailingIndex(String[] params) {
        int trailIdx = -1;
        for (int i = 0; i < params.length; i++) {
            if (params[i].startsWith(PREFIX_PARAM_INDICATOR)) {
                trailIdx = i;
                break;
            }
        }

        return trailIdx;
    }

    public static RawMessage create(String message) throws MessageParserException {
        return new RawMessage(message);
    }

    @Override
    public String getMessage() {
        return this.raw;
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
        return this.params.clone();
    }

    @Override
    public boolean hasPrefix() {
        return this.getPrefix() != null;
    }

    @Override
    public IMessage compile() {
        return IngressMessageCompiler.compile(this);
    }
}
