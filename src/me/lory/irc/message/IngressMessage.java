package me.lory.irc.message;

import me.lory.EMessageType;
import me.lory.IMessage;

class IngressMessage extends AbstractMessage implements IMessage {

    private final String msg;
    
    public IngressMessage(EMessageType type, String target, String msg) {
        super(type, target);
        
        this.msg = msg;
    }

    @Override
    public String getMessage() {
        return this.msg;
    }
    
    

}
