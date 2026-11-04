package practice.e;

import java.time.Instant;

/** Given. */
public record TicketView(Long id, String title, Status status, Priority priority, String assignee, Instant createdAt) {
    public static TicketView from(Ticket t) {
        return new TicketView(t.getId(), t.getTitle(), t.getStatus(), t.getPriority(), t.getAssignee(), t.getCreatedAt());
    }
}
