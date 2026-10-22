package practice.e;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * IDENTITY: the database assigns the id on insert, so the id is null until save() (isNew() = id == null, Spring Data persists).
 * Protected no-arg constructor: Hibernate needs one to instantiate and subclass (lazy proxies), but callers must not create half-built objects.
 * No setters: a reading is immutable once stored. BigDecimal for energy: no binary rounding; precision/scale also drive the DDL.
 * The composite index serves both repository queries: equality on meterId, then a range / ordering on readAt.
 */
@Entity
@Table(name = "meter_reading", indexes = @Index(name = "idx_reading_meter_time", columnList = "meterId,readAt"))
public class MeterReading {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 40)
    private String meterId;
    @Column(nullable = false, precision = 12, scale = 3)
    private BigDecimal kwh;
    @Column(nullable = false)
    private Instant readAt;

    protected MeterReading() {}

    public MeterReading(String meterId, BigDecimal kwh, Instant readAt) {
        this.meterId = meterId;
        this.kwh = kwh;
        this.readAt = readAt;
    }

    public Long getId() { return id; }
    public String getMeterId() { return meterId; }
    public BigDecimal getKwh() { return kwh; }
    public Instant getReadAt() { return readAt; }
}
