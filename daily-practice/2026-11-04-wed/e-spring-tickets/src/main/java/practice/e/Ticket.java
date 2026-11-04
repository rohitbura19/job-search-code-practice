package practice.e;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.Instant;

/** Given. Optimistic locking through @Version: two concurrent status changes of one ticket cannot both win. */
@Entity
@Table(name = "tickets")
public class Ticket {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, length = 100) private String title;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20) private Status status = Status.OPEN;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20) private Priority priority;
    @Column(length = 80) private String assignee;
    @Column(nullable = false) private Instant createdAt;
    @Version private long version;

    protected Ticket() {}

    public Ticket(String title, Priority priority, String assignee, Instant createdAt) {
        this.title = title; this.priority = priority; this.assignee = assignee; this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }
    public String getAssignee() { return assignee; }
    public Instant getCreatedAt() { return createdAt; }
    public long getVersion() { return version; }
}
