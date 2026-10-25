package practice.e;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    Optional<Coupon> findByCode(String code);

    boolean existsByCode(String code);

    // The check and the increment are one statement, so two concurrent redeems of the last slot cannot both win:
    // read-check-save would let both read redemptions = max - 1 and both write max (lost update).
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Coupon c set c.redemptions = c.redemptions + 1 "
            + "where c.code = :code and c.redemptions < c.maxRedemptions and c.validUntil >= :today")
    int redeem(@Param("code") String code, @Param("today") LocalDate today);
}
