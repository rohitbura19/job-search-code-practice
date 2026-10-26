package practice.e;

import java.time.Instant;

public record RefundView(Long id, String paymentId, long amountPaise, RefundStatus status, Instant createdAt) {
    static RefundView of(Refund r) { return new RefundView(r.getId(), r.getPaymentId(), r.getAmountPaise(), r.getStatus(), r.getCreatedAt()); }
}
