package practice.d20261101.g;

public record Message(String conversationId, long seq, String senderId, String clientMsgId, String text) {}
