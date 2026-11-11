package practice.e;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "notifications", indexes = @Index(name = "ix_notifications_user_created", columnList = "user_id, created_at"))
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "channel", discriminatorType = DiscriminatorType.STRING, length = 10)
public abstract class Notification {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Column(nullable = false, length = 500)
    private String message;
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
    @Column(name = "read_at")
    private Instant readAt;

    protected Notification() {}
    protected Notification(Long userId, String message, Instant createdAt) { this.userId = userId; this.message = message; this.createdAt = createdAt; }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public String getMessage() { return message; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getReadAt() { return readAt; }
    public boolean isRead() { return readAt != null; }
    public void markRead(Instant now) { if (readAt == null) readAt = now; }

    public abstract Channel channel();
    public abstract String target();
}
