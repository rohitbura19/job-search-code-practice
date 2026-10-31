package practice.e;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notes")
public class NoteController {
    private final NoteService service;

    public NoteController(NoteService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<NoteView> create(@Valid @RequestBody NoteRequest req) {
        NoteView v = service.create(req);
        return ResponseEntity.created(URI.create("/api/notes/" + v.id())).body(v);
    }

    @GetMapping("/{id}")
    public NoteView get(@PathVariable long id) { return service.get(id); }

    @GetMapping
    public PagedResponse<NoteView> list(@RequestParam String owner, @RequestParam(required = false) String q,
                                        @RequestParam(defaultValue = "0") @Min(0) int page,
                                        @RequestParam(defaultValue = "20") @Min(1) int size) {
        return service.list(owner, q, page, size);
    }

    @PutMapping("/{id}")
    public NoteView update(@PathVariable long id, @Valid @RequestBody NoteUpdate req) { return service.update(id, req); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/restore")
    public NoteView restore(@PathVariable long id) { return service.restore(id); }
}
