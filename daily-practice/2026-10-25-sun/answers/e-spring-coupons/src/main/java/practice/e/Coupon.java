package practice.e;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "coupon", uniqueConstraints = @UniqueConstraint(name = "uk_coupon_code", columnNames = "code"))
public class Coupon {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 30)
    private String code;
    @Column(nullable = false)
    private int percentOff;
    @Column(nullable = false)
    private LocalDate validUntil;
    @Column(nullable = false)
    private int maxRedemptions;
    // No setter: only the atomic repository update may change it, so a stale in-memory copy can never overwrite the counter.
    @Column(nullable = false)
    private int redemptions;

    protected Coupon() {}

    public Coupon(String code, int percentOff, LocalDate validUntil, int maxRedemptions) {
        this.code = code;
        this.percentOff = percentOff;
        this.validUntil = validUntil;
        this.maxRedemptions = maxRedemptions;
    }

    public Long getId() { return id; }
    public String getCode() { return code; }
    public int getPercentOff() { return percentOff; }
    public LocalDate getValidUntil() { return validUntil; }
    public int getMaxRedemptions() { return maxRedemptions; }
    public int getRedemptions() { return redemptions; }
}
