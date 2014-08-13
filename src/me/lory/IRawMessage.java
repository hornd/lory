package me.lory;

/**
 * An uncompiled IRC message. This is a message split into its component parts
 * defined by RFC 1459.
 * 
 * @author hornd
 *
 */
public interface IRawMessage {

    /**
     * Checks whether this message has a prefix or not.
     * 
     * @return true if a prefix exists, false otherwise.
     */
    boolean hasPrefix();

    /**
     * Return the full, raw message.
     * 
     * @return full message.
     */
    String getMessage();

    /**
     * Returns the prefix of this message. Prefix may be null.
     * 
     * @return
     */
    String getPrefix();

    /**
     * Returns the command of this message.
     * 
     * @return
     */
    String getCommand();

    /**
     * Returns the parameters of this message.
     * 
     * @return
     */
    String[] getParams();

    /**
     * Compile this message into a more suitable format.
     * 
     * @return Compiled IMessage.
     */
    IMessage compile();
}
