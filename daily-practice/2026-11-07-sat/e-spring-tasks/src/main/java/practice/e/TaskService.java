package practice.e;

import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/** TODO(E4): make it transactional (class level; read-only where nothing is written). */
@Service
public class TaskService {
    private final TaskRepository repo;
    private final TaskProperties props;

    public TaskService(TaskRepository repo, TaskProperties props) { this.repo = repo; this.props = props; }

    /** Trim the title, save, return the view. */
    public TaskView create(CreateTaskRequest req) { throw new UnsupportedOperationException("TODO(E4)"); }

    /** Load with comments or NotFoundException("Task <id> not found"). */
    public TaskView get(Long id) { throw new UnsupportedOperationException("TODO(E4)"); }

    /** status == null means all. Sort via parseSort. Map entities to TaskSummary (never touch the comments here). */
    public Page<TaskSummary> list(TaskStatus status, int page, int size, String sort) { throw new UnsupportedOperationException("TODO(E4)"); }

    /**
     * "field" or "field,asc|desc" (direction case-insensitive); only createdAt and title are allowed, anything else -> IllegalArgumentException.
     * null/blank -> id ascending. Add id ascending as a tie-breaker so page boundaries are stable.
     */
    static Sort parseSort(String sort) { throw new UnsupportedOperationException("TODO(E4)"); }

    /** NotFoundException if missing. A DONE task cannot move to another status (ConflictException); DONE -> DONE is fine. Flush so updatedAt is visible. */
    public TaskView changeStatus(Long id, TaskStatus next) { throw new UnsupportedOperationException("TODO(E4)"); }

    /**
     * NotFoundException if missing; ConflictException when the task already has props.maxCommentsPerTask() comments.
     * Trim author and text. Flush so the new comment has its id, return its view.
     */
    public CommentView addComment(Long taskId, CommentRequest req) { throw new UnsupportedOperationException("TODO(E4)"); }

    /** NotFoundException when the task or the comment (on that task) does not exist. */
    public void removeComment(Long taskId, Long commentId) { throw new UnsupportedOperationException("TODO(E4)"); }

    /** Every TaskStatus present with its count (0 when none), in enum order. */
    public Map<TaskStatus, Long> stats() { throw new UnsupportedOperationException("TODO(E4)"); }
}
