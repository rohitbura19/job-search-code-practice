package practice.d20261101.g;

import java.util.List;
import java.util.Set;

/**
 * Core of a 1:1 / group chat backend, in memory, thread-safe.
 * Order of checks everywhere: IllegalArgumentException for null/blank arguments, then UnknownConversationException, then NotMemberException.
 */
public interface ChatService {
    /** members must be non-empty and non-null (copy it). A second create with the same id -> IllegalStateException. */
    void createConversation(String conversationId, Set<String> members);

    /**
     * Append a message. seq is 1, 2, 3, ... per conversation, gap-free, assigned under the conversation's lock.
     * Idempotent per (conversation, sender, clientMsgId): a retry returns the ORIGINAL message (same seq, original text), nothing is appended.
     * text must be non-blank and at most 2000 chars (IllegalArgumentException); clientMsgId non-blank.
     */
    Message send(String conversationId, String senderId, String clientMsgId, String text);

    /** Up to limit messages with seq > afterSeq, ascending (afterSeq < 0 counts as 0). limit <= 0 -> IllegalArgumentException. Members only. */
    List<Message> fetch(String conversationId, String userId, long afterSeq, int limit);

    /** Move the user's read marker to upToSeq. Never moves backwards (a smaller value is ignored). upToSeq beyond the last seq -> IllegalArgumentException. */
    void markRead(String conversationId, String userId, long upToSeq);

    /** Current read marker, 0 if none. */
    long lastRead(String conversationId, String userId);

    /** Messages with seq > lastRead that were NOT sent by this user. */
    long unreadCount(String conversationId, String userId);
}
