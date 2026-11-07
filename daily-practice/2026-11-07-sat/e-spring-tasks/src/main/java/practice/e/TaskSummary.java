package practice.e;

import java.time.Instant;

/** List item: deliberately has no comments, so listing never touches the lazy collection. */
public record TaskSummary(Long id, String title, TaskStatus status, Instant createdAt) {
    static TaskSummary from(Task t) { return new TaskSummary(t.getId(), t.getTitle(), t.getStatus(), t.getCreatedAt()); }
}
