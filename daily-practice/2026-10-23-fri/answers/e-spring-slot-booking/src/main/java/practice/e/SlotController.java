package practice.e;

import jakarta.validation.Valid;
import java.net.URI;
import java.time.Instant;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SlotController {
    private final SlotService service;

    public SlotController(SlotService service) { this.service = service; }

    @PostMapping("/slots")
    public ResponseEntity<SlotView> create(@Valid @RequestBody CreateSlotRequest request) {
        SlotView v = service.create(request);
        return ResponseEntity.created(URI.create("/api/slots/" + v.id())).body(v);
    }

    @GetMapping("/stations/{stationId}/slots")
    public List<SlotView> available(@PathVariable String stationId,
                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from) {
        return service.available(stationId, from);
    }

    @PostMapping("/slots/{id}/reservations")
    public SlotView reserve(@PathVariable long id) { return service.reserve(id); }
}
