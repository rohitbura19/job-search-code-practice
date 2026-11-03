package practice.e;

import java.time.Instant;

/** GIVEN. Plain object (the @Entity/@Index/jsonb mapping is a paper question). sentAt == null means not yet published. */
public class OutboxEvent {
    private Long id;
    private String aggregateId;     // Kafka key = siteId
    private String payload;         // JSON
    private Instant createdAt;
    private Instant sentAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAggregateId() { return aggregateId; }
    public void setAggregateId(String aggregateId) { this.aggregateId = aggregateId; }
    public String getPayload() { return payload; }
    public void setPayload(String payload) { this.payload = payload; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getSentAt() { return sentAt; }
    public void setSentAt(Instant sentAt) { this.sentAt = sentAt; }
}
