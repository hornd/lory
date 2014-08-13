package me.lory.irc.message;

import me.lory.EMessageType;
import me.lory.IMessage;
import me.lory.IRawMessage;

public class IngressMessageCompiler {
    public static IMessage compile(IRawMessage raw) {
        EMessageType type = getMessageType(raw);
        if (type != EMessageType.PRIVMSG) {
            return new IngressMessage(type, "UNKOWN", raw.getMessage());
        }

        String target = getTarget(raw);
        String message = getMessage(raw);

        return new IngressMessage(type, target, message);
    }

    private static String getMessage(IRawMessage raw) {
        String[] params = raw.getParams();
        return params[params.length - 1];
    }

    private static String getTarget(IRawMessage raw) {
        String sender = getMessageSender(raw);
        String recipient = getMessageRecipient(raw);
        
        return isChannel(recipient) ? recipient : sender;
    }

    private static boolean isChannel(String message) {
        return message.startsWith("#");
    }

    private static String getMessageSender(IRawMessage raw) {
        if (!raw.hasPrefix()) {
            return ""; // TODO
        }

        String[] split = raw.getPrefix().split("!");
        return split[0];
    }

    private static String getMessageRecipient(IRawMessage raw) {
        return raw.getParams()[0];
    }

    private static EMessageType getMessageType(IRawMessage raw) {
        return EMessageType.getTypeFromStr(raw.getCommand());
    }
}
