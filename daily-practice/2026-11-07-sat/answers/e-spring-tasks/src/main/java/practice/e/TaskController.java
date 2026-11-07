package practice.e;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import java.net.URI;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService service;

    public TaskController(TaskService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<TaskView> create(@Valid @RequestBody CreateTaskRequest req) {
        TaskView v = service.create(req);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(v.id()).toUri();
        return ResponseEntity.created(location).body(v);
    }

    @GetMapping("/{id}")
    public TaskView get(@PathVariable @Positive Long id) { return service.get(id); }

    @GetMapping
    public ResponseEntity<List<TaskSummary>> list(@RequestParam(required = false) TaskStatus status,
                                                  @RequestParam(defaultValue = "0") @Min(0) int page,
                                                  @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size,
                                                  @RequestParam(required = false) String sort) {
        Page<TaskSummary> p = service.list(status, page, size, sort);
        return ResponseEntity.ok().header("X-Total-Count", String.valueOf(p.getTotalElements())).body(p.getContent());
    }

    @GetMapping("/stats")
    public Map<TaskStatus, Long> stats() { return service.stats(); }

    @PutMapping("/{id}/status")
    public TaskView changeStatus(@PathVariable @Positive Long id, @Valid @RequestBody ChangeStatusRequest req) {
        return service.changeStatus(id, req.status());
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentView> addComment(@PathVariable @Positive Long id, @Valid @RequestBody CommentRequest req) {
        CommentView c = service.addComment(id, req);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{cid}").buildAndExpand(c.id()).toUri();
        return ResponseEntity.created(location).body(c);
    }

    @DeleteMapping("/{id}/comments/{commentId}")
    public ResponseEntity<Void> removeComment(@PathVariable @Positive Long id, @PathVariable @Positive Long commentId) {
        service.removeComment(id, commentId);
        return ResponseEntity.noContent().build();
    }
}
