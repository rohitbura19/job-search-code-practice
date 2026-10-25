package practice.e;

import java.time.Clock;
import java.time.LocalDate;

// TODO: make this a Spring service; writes are transactional, reads are read-only transactions.
public class CouponService {
    private final CouponRepository repo;
    private final CouponProperties props;
    private final Clock clock;

    public CouponService(CouponRepository repo, CouponProperties props, Clock clock) {
        this.repo = repo;
        this.props = props;
        this.clock = clock;
    }

    /**
     * percentOff > props.maxPercentOff() -> CouponException.tooGenerous; maxRedemptions > props.maxRedemptionsCap() -> CouponException.tooMany.
     * A known code -> CouponException.duplicate WITHOUT saving; and if two requests race past that check, the unique constraint
     * (DataIntegrityViolationException from saveAndFlush) must also surface as CouponException.duplicate.
     */
    public CouponView create(CreateCouponRequest request) { throw new UnsupportedOperationException("TODO"); }

    /**
     * Redeem one use. Use the atomic repo.redeem(code, today) with today = LocalDate.now(clock) and branch on the result:
     * 1 -> return the fresh view; 0 -> load by code: unknown -> notFound, validUntil before today -> expired, otherwise exhausted.
     */
    public CouponView redeem(String code) { throw new UnsupportedOperationException("TODO"); }

    public CouponView get(String code) { throw new UnsupportedOperationException("TODO"); }
}
