package practice.e;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

/** Given. The unique idempotency key is the real guard against double-submits; the service check is only the fast path. */
@Entity
@Table(name = "transfers", uniqueConstraints = @UniqueConstraint(name = "uk_transfer_key", columnNames = "idempotencyKey"))
public class Transfer {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, length = 64) private String idempotencyKey;
    @Column(nullable = false) private Long fromId;
    @Column(nullable = false) private Long toId;
    @Column(nullable = false, precision = 19, scale = 2) private BigDecimal amount;
    @Column(nullable = false) private Instant createdAt;

    protected Transfer() {}
    public Transfer(String idempotencyKey, Long fromId, Long toId, BigDecimal amount, Instant createdAt) {
        this.idempotencyKey = idempotencyKey; this.fromId = fromId; this.toId = toId; this.amount = amount; this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public String getIdempotencyKey() { return idempotencyKey; }
    public Long getFromId() { return fromId; }
    public Long getToId() { return toId; }
    public BigDecimal getAmount() { return amount; }
    public Instant getCreatedAt() { return createdAt; }
}
