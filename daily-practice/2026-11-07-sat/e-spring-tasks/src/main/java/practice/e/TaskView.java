package practice.e;

import java.time.Instant;
import java.util.List;

public record TaskView(Long id, String title, TaskStatus status, Instant createdAt, Instant updatedAt, List<CommentView> comments) {
    static TaskView from(Task t) {
        return new TaskView(t.getId(), t.getTitle(), t.getStatus(), t.getCreatedAt(), t.getUpdatedAt(),
                t.getComments().stream().map(CommentView::from).toList());
    }
}
