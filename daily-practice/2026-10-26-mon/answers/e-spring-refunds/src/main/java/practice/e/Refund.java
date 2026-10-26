package practice.e;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "refund", uniqueConstraints = @UniqueConstraint(name = "uk_refund_idem", columnNames = "idempotency_key"))
public class Refund {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 40)
    private String paymentId;
    @Column(nullable = false)
    private long amountPaise;
    @Column(length = 200)
    private String reason;
    @Column(name = "idempotency_key", nullable = false, length = 64)
    private String idempotencyKey;
    // EnumType.STRING: the ordinal changes when someone inserts a constant in the middle and silently corrupts old rows.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RefundStatus status;
    @Column(nullable = false)
    private Instant createdAt;
    // Hibernate adds "and version = ?" to the UPDATE and bumps it; zero rows updated -> OptimisticLockException (a lost update is impossible).
    @Version
    private long version;

    protected Refund() {}

    public Refund(String paymentId, long amountPaise, String reason, String idempotencyKey, RefundStatus status, Instant createdAt) {
        this.paymentId = paymentId;
        this.amountPaise = amountPaise;
        this.reason = reason;
        this.idempotencyKey = idempotencyKey;
        this.status = status;
        this.createdAt = createdAt;
    }

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
