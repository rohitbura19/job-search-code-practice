package practice.e;

import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;

/**
 * TODO(E5): REST controller under /api/subscriptions. Do NOT put @Validated on the class (Spring 6.1 method validation is built in).
 *   POST   /api/subscriptions            @Valid body -> 201 + Location /api/subscriptions/{id} + view. Optional request header X-Request-Id:
 *                                        when present it is echoed as a response header X-Request-Id.
 *   GET    /api/subscriptions/{id}       id must be positive
 *   GET    /api/subscriptions?email=     email REQUIRED, not blank, well-formed -> list of views
 *   PUT    /api/subscriptions/{id}/plan  @Valid body ChangePlanRequest -> 200 + view
 *   DELETE /api/subscriptions/{id}       -> 204 no body (idempotent)
 *   GET    /api/subscriptions/renewals?withinDays=7   withinDays 1..90 (default 7) -> list of views
 *   GET    /api/subscriptions/stats      -> JSON object plan -> active count
 */
public class SubscriptionController {
    public SubscriptionController(SubscriptionService service) {}

    public ResponseEntity<Views.SubscriptionView> create(CreateSubscriptionRequest r, String requestId) { throw new UnsupportedOperationException("TODO"); }
    public Views.SubscriptionView get(Long id) { throw new UnsupportedOperationException("TODO"); }
    public List<Views.SubscriptionView> byEmail(String email) { throw new UnsupportedOperationException("TODO"); }
    public Views.SubscriptionView changePlan(Long id, CreateSubscriptionRequest.ChangePlanRequest r) { throw new UnsupportedOperationException("TODO"); }
    public void cancel(Long id) { throw new UnsupportedOperationException("TODO"); }
    public List<Views.SubscriptionView> renewals(int withinDays) { throw new UnsupportedOperationException("TODO"); }
    public Map<Plan, Long> stats() { throw new UnsupportedOperationException("TODO"); }
}
