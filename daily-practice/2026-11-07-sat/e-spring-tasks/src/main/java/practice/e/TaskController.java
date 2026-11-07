package practice.e;

import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;

/**
 * TODO(E5): make this a REST controller under /tasks.
 * POST   /tasks                         body CreateTaskRequest (validated) -> 201, Location: .../tasks/{id}, body TaskView
 * GET    /tasks/{id}                    id must be positive -> TaskView
 * GET    /tasks?status&page&size&sort   defaults page=0 (>=0), size=20 (1..100); body List<TaskSummary>, header X-Total-Count
 * GET    /tasks/stats                   -> Map<TaskStatus, Long>
 * PUT    /tasks/{id}/status             body ChangeStatusRequest (validated) -> TaskView
 * POST   /tasks/{id}/comments           body CommentRequest (validated), id positive -> 201, Location: .../comments/{cid}, body CommentView
 * DELETE /tasks/{id}/comments/{commentId}   ids positive -> 204
 * Parameter constraints (@Positive/@Min/@Max) must produce a 400 ProblemDetail; do NOT add class-level @Validated (Spring 6.1 validates handler methods itself).
 */
public class TaskController {
    private final TaskService service;

    public TaskController(TaskService service) { this.service = service; }

    public ResponseEntity<TaskView> create(CreateTaskRequest req) { throw new UnsupportedOperationException("TODO(E5)"); }

    public TaskView get(Long id) { throw new UnsupportedOperationException("TODO(E5)"); }

    public ResponseEntity<List<TaskSummary>> list(TaskStatus status, int page, int size, String sort) { throw new UnsupportedOperationException("TODO(E5)"); }

    public Map<TaskStatus, Long> stats() { throw new UnsupportedOperationException("TODO(E5)"); }

    public TaskView changeStatus(Long id, ChangeStatusRequest req) { throw new UnsupportedOperationException("TODO(E5)"); }

    public ResponseEntity<CommentView> addComment(Long id, CommentRequest req) { throw new UnsupportedOperationException("TODO(E5)"); }

    public ResponseEntity<Void> removeComment(Long id, Long commentId) { throw new UnsupportedOperationException("TODO(E5)"); }
}
