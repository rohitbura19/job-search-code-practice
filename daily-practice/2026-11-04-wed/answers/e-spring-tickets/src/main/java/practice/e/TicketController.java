package practice.e;

import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    private final TicketService service;

    public TicketController(TicketService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<TicketView> create(@Valid @RequestBody CreateTicketRequest req) {
        TicketView v = service.create(req);
        return ResponseEntity.created(URI.create("/api/tickets/" + v.id())).body(v);
    }

    @GetMapping("/{id}")
    public TicketView get(@PathVariable Long id) { return service.get(id); }

    @PatchMapping("/{id}/status")
    public TicketView changeStatus(@PathVariable Long id, @Valid @RequestBody StatusChange change) { return service.transition(id, change.status()); }

    @GetMapping
    public TicketPage search(@RequestParam(required = false) Status status,
                             @RequestParam(required = false) String assignee,
                             @RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "20") int size) {
        return service.search(status, assignee, page, size);
    }
}
