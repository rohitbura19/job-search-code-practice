package practice.d20261101.g;

public class NotMemberException extends RuntimeException {
    public NotMemberException(String user, String conversationId) { super(user + " is not a member of " + conversationId); }
}
