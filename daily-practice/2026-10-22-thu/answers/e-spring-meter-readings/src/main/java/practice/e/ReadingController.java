package practice.e;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.net.URI;
import java.time.Instant;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Spring 6.1+ validates @RequestParam / @PathVariable constraints itself (HandlerMethodValidationException -> 400 via the base advice)
 * when there is NO class-level @Validated. Adding @Validated switches to the AOP proxy and ConstraintViolationException (500 unless handled).
 * The repository is used directly because there is no business rule; add a service when there is a transaction or a rule to own.
 */
@RestController
@RequestMapping("/api")
public class ReadingController {
    private final MeterReadingRepository repo;

    public ReadingController(MeterReadingRepository repo) { this.repo = repo; }

    @PostMapping("/readings")
    public ResponseEntity<ReadingView> create(@Valid @RequestBody ReadingRequest request) {
        MeterReading saved = repo.save(new MeterReading(request.meterId(), request.kwh(), request.readAt()));
        return ResponseEntity.created(URI.create("/api/readings/" + saved.getId())).body(ReadingView.of(saved));
    }

    @GetMapping("/meters/{meterId}/readings")
    public List<ReadingView> range(@PathVariable String meterId,
                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to) {
        return repo.findByMeterIdAndReadAtBetweenOrderByReadAtAsc(meterId, from, to).stream().map(ReadingView::of).toList();
    }

    @GetMapping("/meters/{meterId}/latest")
    public List<ReadingView> latest(@PathVariable String meterId, @RequestParam(defaultValue = "5") @Min(1) @Max(100) int limit) {
        return repo.findByMeterIdOrderByReadAtDesc(meterId, PageRequest.of(0, limit)).stream().map(ReadingView::of).toList();
    }

    @GetMapping("/meters/{meterId}/total")
    public TotalView total(@PathVariable String meterId) {
        return new TotalView(meterId, repo.totalKwh(meterId));
    }
}
