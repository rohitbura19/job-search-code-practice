package practice.d20261101.g;

public class UnknownConversationException extends RuntimeException {
    public UnknownConversationException(String conversationId) { super("unknown conversation " + conversationId); }
}
