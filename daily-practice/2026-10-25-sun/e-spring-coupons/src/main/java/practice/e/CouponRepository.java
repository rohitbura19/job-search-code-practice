package practice.e;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

// TODO: complete the three methods (the signatures are given):
//   1. findByCode: derived query, case-SENSITIVE.
//   2. existsByCode: derived.
//   3. redeem: ONE conditional update that adds 1 to redemptions only if the coupon exists, redemptions < maxRedemptions and
//      validUntil >= :today; it returns the number of rows changed (1 = redeemed, 0 = unknown / exhausted / expired). It must be
//      @Modifying and make the next read in the same transaction see the new count (clearAutomatically / flushAutomatically).
//      Why is "read, check, then save" wrong here? What would two concurrent redeems of the last slot do?
public interface CouponRepository extends JpaRepository<Coupon, Long> {
    Optional<Coupon> findByCode(String code);

    boolean existsByCode(String code);

    int redeem(String code, LocalDate today);
}
