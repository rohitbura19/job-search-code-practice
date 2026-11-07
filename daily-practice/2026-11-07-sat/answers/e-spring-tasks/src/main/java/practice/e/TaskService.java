package practice.e;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TaskService {
    private static final Set<String> SORTABLE = Set.of("createdAt", "title");

    private final TaskRepository repo;
    private final TaskProperties props;

    public TaskService(TaskRepository repo, TaskProperties props) { this.repo = repo; this.props = props; }

    public TaskView create(CreateTaskRequest req) {
        return TaskView.from(repo.save(new Task(req.title().trim())));
    }

    @Transactional(readOnly = true)
    public TaskView get(Long id) { return TaskView.from(load(id)); }

    @Transactional(readOnly = true)
    public Page<TaskSummary> list(TaskStatus status, int page, int size, String sort) {
        PageRequest pr = PageRequest.of(page, size, parseSort(sort));
        Page<Task> result = status == null ? repo.findAll(pr) : repo.findByStatus(status, pr);
        return result.map(TaskSummary::from);
    }

    /** "field" or "field,asc|desc"; only whitelisted fields (never pass user input to Sort unchecked). Default: id ascending. */
    static Sort parseSort(String sort) {
        if (sort == null || sort.isBlank()) return Sort.by("id");
        String[] p = sort.split(",", -1);
        String field = p[0].trim();
        if (p.length > 2 || !SORTABLE.contains(field)) throw new IllegalArgumentException("Unsupported sort: " + sort);
        Sort.Direction dir = Sort.Direction.ASC;
        if (p.length == 2) {
            String d = p[1].trim();
            if (d.equalsIgnoreCase("desc")) dir = Sort.Direction.DESC;
            else if (!d.equalsIgnoreCase("asc")) throw new IllegalArgumentException("Unsupported sort direction: " + d);
        }
        return Sort.by(dir, field).and(Sort.by("id")); // id as tie-breaker keeps paging stable
    }

    /** A finished task is final. */
    public TaskView changeStatus(Long id, TaskStatus next) {
        Task t = repo.findById(id).orElseThrow(() -> new Exceptions.NotFoundException("Task " + id + " not found"));
        if (t.getStatus() == TaskStatus.DONE && next != TaskStatus.DONE)
            throw new Exceptions.ConflictException("Task " + id + " is DONE and cannot be reopened");
        t.setStatus(next);
        repo.flush(); // make @LastModifiedDate visible in the returned view
        return TaskView.from(t);
    }

    public CommentView addComment(Long taskId, CommentRequest req) {
        Task t = load(taskId);
        if (t.getComments().size() >= props.maxCommentsPerTask())
            throw new Exceptions.ConflictException("Task " + taskId + " already has " + props.maxCommentsPerTask() + " comments");
        Comment c = t.addComment(req.author().trim(), req.text().trim());
        repo.flush(); // assigns the comment id
        return CommentView.from(c);
    }

    public void removeComment(Long taskId, Long commentId) {
        Task t = load(taskId);
        if (!t.removeComment(commentId)) throw new Exceptions.NotFoundException("Comment " + commentId + " not found on task " + taskId);
    }

    /** Every status present, zero-filled, in enum order. */
    @Transactional(readOnly = true)
    public Map<TaskStatus, Long> stats() {
        Map<TaskStatus, Long> out = new EnumMap<>(TaskStatus.class);
        for (TaskStatus s : TaskStatus.values()) out.put(s, 0L);
        for (StatusCount c : repo.countByStatus()) out.put(c.getStatus(), c.getTotal());
        return out;
    }

    private Task load(Long id) {
        return repo.findWithComments(id).orElseThrow(() -> new Exceptions.NotFoundException("Task " + id + " not found"));
    }
}
