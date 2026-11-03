package practice.e;

import java.time.Instant;
import java.util.UUID;

/**
 * GIVEN. Plain object here so no database is needed. The JPA mapping (@Entity, @Id, @Column(nullable = false),
 * @Enumerated(EnumType.STRING), ...) is a paper question in questions/E.
 */
public class Alert {
    public enum Severity { INFO, WARN, CRITICAL }

    private UUID id = UUID.randomUUID();
    private String siteId;
    private Severity severity;
    private String message;
    private Instant createdAt;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getSiteId() { return siteId; }
    public void setSiteId(String siteId) { this.siteId = siteId; }
    public Severity getSeverity() { return severity; }
    public void setSeverity(Severity severity) { this.severity = severity; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
