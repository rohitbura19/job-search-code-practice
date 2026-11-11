package practice.e;

import java.time.Instant;

/**
 * TODO(E1a): map this ABSTRACT class as the root of a SINGLE_TABLE hierarchy in table "notifications".
 *  - @Entity, @Inheritance(SINGLE_TABLE), @DiscriminatorColumn name "channel" (STRING, length 10).
 *  - id: IDENTITY. userId -> column user_id, not null. message: not null, length 500. createdAt -> created_at, not null. readAt -> read_at, nullable.
 *  - @Table(indexes = ...) on (user_id, created_at) named ix_notifications_user_created (the inbox query filters and sorts on exactly these).
 */
public abstract class Notification {
    private Long id;
    private Long userId;
    private String message;
    private Instant createdAt;
    private Instant readAt;

    protected Notification() {}
    protected Notification(Long userId, String message, Instant createdAt) { this.userId = userId; this.message = message; this.createdAt = createdAt; }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public String getMessage() { return message; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getReadAt() { return readAt; }
    public boolean isRead() { return readAt != null; }
    /** Idempotent: the FIRST read time is kept. */
    public void markRead(Instant now) { if (readAt == null) readAt = now; }

    public abstract Channel channel();
    public abstract String target();
}
