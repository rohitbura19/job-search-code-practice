package practice.e;

import java.time.Instant;

// TODO: make this a JPA entity on table "refund" with a named UNIQUE constraint on idempotency_key (uk_refund_idem).
// TODO: Long id (IDENTITY); paymentId not null length 40; amountPaise not null; reason length 200 (nullable); idempotencyKey not null
//       length 64; status not null and stored as its NAME (why not the ordinal?); createdAt not null; @Version long version
//       (optimistic locking: what does Hibernate add to the UPDATE statement?). Protected no-arg constructor for JPA.
public class Refund {
    private Long id;
    private String paymentId;
    private long amountPaise;
    private String reason;
    private String idempotencyKey;
    private RefundStatus status;
    private Instant createdAt;
    private long version;

    public Refund(String paymentId, long amountPaise, String reason, String idempotencyKey, RefundStatus status, Instant createdAt) {
        this.paymentId = paymentId;
        this.amountPaise = amountPaise;
        this.reason = reason;
        this.idempotencyKey = idempotencyKey;
        this.status = status;
        this.createdAt = createdAt;
    }

    /** PENDING -> APPROVED or REJECTED; anything else is an IllegalStateException. */
    public void decide(boolean approved) {
        if (status != RefundStatus.PENDING) throw new IllegalStateException("already " + status);
        status = approved ? RefundStatus.APPROVED : RefundStatus.REJECTED;
    }

    public Long getId() { return id; }
    public String getPaymentId() { return paymentId; }
    public long getAmountPaise() { return amountPaise; }
    public String getReason() { return reason; }
    public String getIdempotencyKey() { return idempotencyKey; }
    public RefundStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public long getVersion() { return version; }
}
