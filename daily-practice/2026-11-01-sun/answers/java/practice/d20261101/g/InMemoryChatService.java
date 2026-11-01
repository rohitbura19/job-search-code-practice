package practice.d20261101.g;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * One ConcurrentHashMap entry per conversation; each Conversation is guarded by its own monitor, so conversations never contend.
 * The log is an ArrayList where index = seq - 1, giving O(1) lookup by seq, O(limit) fetch and a gap-free seq assigned under the lock.
 * Idempotency: map (sender, clientMsgId) -> original Message, checked inside the same critical section as the append.
 * Costs: send O(1); fetch O(limit); markRead O(1); unreadCount O(unread) (scan from the marker).
 * Production notes: persist the log (append-only, partition by conversationId), bound the dedupe map with a TTL window.
 */
public final class InMemoryChatService implements ChatService {
    private static final int MAX_TEXT = 2000;

    private static final class Conversation {
        final Set<String> members;
        final List<Message> log = new ArrayList<>();
        final Map<String, Message> byClientId = new HashMap<>();
        final Map<String, Long> read = new HashMap<>();
        Conversation(Set<String> members) { this.members = members; }
    }

    private final ConcurrentHashMap<String, Conversation> conversations = new ConcurrentHashMap<>();

    private static void require(String s, String name) {
        if (s == null || s.isBlank()) throw new IllegalArgumentException(name + " must not be blank");
    }

    @Override public void createConversation(String id, Set<String> members) {
        require(id, "conversationId");
        if (members == null || members.isEmpty() || members.stream().anyMatch(m -> m == null || m.isBlank()))
            throw new IllegalArgumentException("members");
        if (conversations.putIfAbsent(id, new Conversation(Set.copyOf(members))) != null)
            throw new IllegalStateException("conversation exists: " + id);
    }

    private Conversation member(String id, String user) {
        Conversation c = conversations.get(id);
        if (c == null) throw new UnknownConversationException(id);
        if (!c.members.contains(user)) throw new NotMemberException(user, id);
        return c;
    }

    @Override public Message send(String id, String sender, String clientMsgId, String text) {
        require(id, "conversationId"); require(sender, "senderId"); require(clientMsgId, "clientMsgId"); require(text, "text");
        if (text.length() > MAX_TEXT) throw new IllegalArgumentException("text too long");
        Conversation c = member(id, sender);
        synchronized (c) {
            String key = sender + '\u0000' + clientMsgId;
            Message existing = c.byClientId.get(key);
            if (existing != null) return existing;
            Message m = new Message(id, c.log.size() + 1L, sender, clientMsgId, text);
            c.log.add(m);
            c.byClientId.put(key, m);
            return m;
        }
    }

    @Override public List<Message> fetch(String id, String user, long afterSeq, int limit) {
        require(id, "conversationId"); require(user, "userId");
        if (limit <= 0) throw new IllegalArgumentException("limit");
        Conversation c = member(id, user);
        synchronized (c) {
            int from = (int) Math.min(Math.max(afterSeq, 0), c.log.size());
            return List.copyOf(c.log.subList(from, Math.min(c.log.size(), (int) Math.min((long) from + limit, Integer.MAX_VALUE))));
        }
    }

    @Override public void markRead(String id, String user, long upToSeq) {
        require(id, "conversationId"); require(user, "userId");
        Conversation c = member(id, user);
        synchronized (c) {
            if (upToSeq > c.log.size()) throw new IllegalArgumentException("beyond last seq");
            c.read.merge(user, Math.max(upToSeq, 0), Math::max);
        }
    }

    @Override public long lastRead(String id, String user) {
        require(id, "conversationId"); require(user, "userId");
        Conversation c = member(id, user);
        synchronized (c) { return c.read.getOrDefault(user, 0L); }
    }

    @Override public long unreadCount(String id, String user) {
        require(id, "conversationId"); require(user, "userId");
        Conversation c = member(id, user);
        synchronized (c) {
            long n = 0;
            for (int i = (int) (long) c.read.getOrDefault(user, 0L); i < c.log.size(); i++)
                if (!c.log.get(i).senderId().equals(user)) n++;
            return n;
        }
    }
}
