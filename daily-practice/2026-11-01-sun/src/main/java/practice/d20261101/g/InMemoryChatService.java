package practice.d20261101.g;

import java.util.List;
import java.util.Set;

public final class InMemoryChatService implements ChatService {
    @Override public void createConversation(String conversationId, Set<String> members) { throw new UnsupportedOperationException("TODO"); }
    @Override public Message send(String conversationId, String senderId, String clientMsgId, String text) { throw new UnsupportedOperationException("TODO"); }
    @Override public List<Message> fetch(String conversationId, String userId, long afterSeq, int limit) { throw new UnsupportedOperationException("TODO"); }
    @Override public void markRead(String conversationId, String userId, long upToSeq) { throw new UnsupportedOperationException("TODO"); }
    @Override public long lastRead(String conversationId, String userId) { throw new UnsupportedOperationException("TODO"); }
    @Override public long unreadCount(String conversationId, String userId) { throw new UnsupportedOperationException("TODO"); }
}
