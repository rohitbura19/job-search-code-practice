package practice.e;

import org.springframework.data.domain.Pageable;

// TODO: make this a REST controller under /api/refunds; no logic here, only delegation:
//   POST /api/refunds                 header Idempotency-Key (required, must match [A-Za-z0-9_-]{8,64}) + valid body
//                                     -> 201 + Location /api/refunds/{id} + the RefundView for a NEW refund;
//                                        200 + the same Location + header "Idempotent-Replayed: true" for a replay.
//   GET  /api/refunds/{id}            -> 200 + view
//   GET  /api/refunds?paymentId=...   -> 200 + PageView; the Pageable comes from ?page=&size=&sort= (paymentId is optional)
//   POST /api/refunds/{id}/decision   body {"approved": true|false} (valid) -> 200 + view
//   A bad header value is a 400 problem+json WITHOUT @Validated on the class (which exception does Spring 6.1 raise?).
public class RefundController {
    private final RefundService service;

    public RefundController(RefundService service) { this.service = service; }

    public Object create(String idempotencyKey, CreateRefundRequest request) { return null; }

    public RefundView get(long id) { return null; }

    public PageView<RefundView> list(String paymentId, Pageable pageable) { return null; }

    public RefundView decide(long id, DecisionRequest request) { return null; }
}
