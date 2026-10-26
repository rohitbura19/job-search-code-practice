package practice.e;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import java.net.URI;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/refunds")
public class RefundController {
    private final RefundService service;

    public RefundController(RefundService service) { this.service = service; }

    // No @Validated on the class: Spring 6.1 validates the @Pattern on the parameter itself and throws HandlerMethodValidationException (400).
    @PostMapping
    public ResponseEntity<RefundView> create(@RequestHeader("Idempotency-Key") @Pattern(regexp = "[A-Za-z0-9_-]{8,64}") String idempotencyKey,
                                             @Valid @RequestBody CreateRefundRequest request) {
        CreateResult result = service.create(idempotencyKey, request);
        URI location = URI.create("/api/refunds/" + result.view().id());
        return result.replayed()
                ? ResponseEntity.ok().location(location).header("Idempotent-Replayed", "true").body(result.view())
                : ResponseEntity.created(location).body(result.view());
    }

    @GetMapping("/{id}")
    public RefundView get(@PathVariable long id) { return service.get(id); }

    @GetMapping
    public PageView<RefundView> list(@RequestParam(required = false) String paymentId, Pageable pageable) { return service.list(paymentId, pageable); }

    @PostMapping("/{id}/decision")
    public RefundView decide(@PathVariable long id, @Valid @RequestBody DecisionRequest request) { return service.decide(id, request.approved()); }
}
