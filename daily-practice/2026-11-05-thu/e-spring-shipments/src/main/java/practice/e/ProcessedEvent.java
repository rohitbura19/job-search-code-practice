package practice.e;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.Instant;

/** Given. One row per carrier event already applied: the primary key is the dedupe key. */
@Entity
public class ProcessedEvent {
    @Id private String eventId;
    private Instant processedAt;

    protected ProcessedEvent() {}
    public ProcessedEvent(String eventId, Instant processedAt) { this.eventId = eventId; this.processedAt = processedAt; }
    public String getEventId() { return eventId; }
    public Instant getProcessedAt() { return processedAt; }
}
