package practice.e;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.net.URI;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {
    private final SubscriptionService service;

    public SubscriptionController(SubscriptionService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<Views.SubscriptionView> create(@Valid @RequestBody CreateSubscriptionRequest r,
                                                        @RequestHeader(value = "X-Request-Id", required = false) String requestId) {
        Views.SubscriptionView v = service.subscribe(r.email(), r.plan());
        ResponseEntity.BodyBuilder b = ResponseEntity.created(URI.create("/api/subscriptions/" + v.id()));
        if (requestId != null) b.header("X-Request-Id", requestId);
        return b.body(v);
    }

    @GetMapping("/{id}")
    public Views.SubscriptionView get(@PathVariable @Positive Long id) { return service.get(id); }

    @GetMapping
    public List<Views.SubscriptionView> byEmail(@RequestParam @NotBlank @Email String email) { return service.forEmail(email); }

    @PutMapping("/{id}/plan")
    public Views.SubscriptionView changePlan(@PathVariable @Positive Long id, @Valid @RequestBody CreateSubscriptionRequest.ChangePlanRequest r) {
        return service.changePlan(id, r.plan());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancel(@PathVariable @Positive Long id) { service.cancel(id); }

    @GetMapping("/renewals")
    public List<Views.SubscriptionView> renewals(@RequestParam(defaultValue = "7") @Min(1) @Max(90) int withinDays) {
        return service.renewalsDue(Duration.ofDays(withinDays));
    }

    @GetMapping("/stats")
    public Map<Plan, Long> stats() { return service.activeByPlan(); }
}
