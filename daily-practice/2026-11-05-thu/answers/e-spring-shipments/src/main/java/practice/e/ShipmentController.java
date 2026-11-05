package practice.e;

import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/shipments")
public class ShipmentController {
    private final ShipmentService service;

    public ShipmentController(ShipmentService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<ShipmentView> create(@Valid @RequestBody CreateShipmentRequest request) {
        ShipmentView view = service.create(request);
        return ResponseEntity.created(URI.create("/api/shipments/" + view.id())).body(view);
    }

    @GetMapping("/{id}")
    public ShipmentView get(@PathVariable Long id) { return service.get(id); }

    @GetMapping
    public ShipmentPage search(@RequestParam(required = false) ShipmentStatus status,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "20") int size) {
        return service.search(status, page, size);
    }
}
