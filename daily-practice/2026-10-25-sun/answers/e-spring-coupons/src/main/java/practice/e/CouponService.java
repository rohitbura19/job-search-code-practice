package practice.e;

import java.time.Clock;
import java.time.LocalDate;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CouponService {
    private final CouponRepository repo;
    private final CouponProperties props;
    private final Clock clock;

    public CouponService(CouponRepository repo, CouponProperties props, Clock clock) {
        this.repo = repo;
        this.props = props;
        this.clock = clock;
    }

    @Transactional
    public CouponView create(CreateCouponRequest r) {
        if (r.percentOff() > props.maxPercentOff()) throw CouponException.tooGenerous(r.percentOff(), props.maxPercentOff());
        if (r.maxRedemptions() > props.maxRedemptionsCap()) throw CouponException.tooMany(r.maxRedemptions(), props.maxRedemptionsCap());
        if (repo.existsByCode(r.code())) throw CouponException.duplicate(r.code());
        try {
            return CouponView.of(repo.saveAndFlush(new Coupon(r.code(), r.percentOff(), r.validUntil(), r.maxRedemptions())));
        } catch (DataIntegrityViolationException e) {
            throw CouponException.duplicate(r.code());
        }
    }

    @Transactional
    public CouponView redeem(String code) {
        LocalDate today = LocalDate.now(clock);
        if (repo.redeem(code, today) == 1) return CouponView.of(repo.findByCode(code).orElseThrow());
        Coupon c = repo.findByCode(code).orElseThrow(() -> CouponException.notFound(code));
        throw c.getValidUntil().isBefore(today) ? CouponException.expired(code) : CouponException.exhausted(code);
    }

    @Transactional(readOnly = true)
    public CouponView get(String code) {
        return CouponView.of(repo.findByCode(code).orElseThrow(() -> CouponException.notFound(code)));
    }
}
