package practice.e;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
class CouponRepositoryTest {
    static final LocalDate TODAY = LocalDate.of(2026, 10, 25);
    @Autowired CouponRepository repo;

    @BeforeEach void seed() {
        repo.save(new Coupon("SAVE10", 10, TODAY.plusDays(5), 2));
        repo.save(new Coupon("LASTDAY", 20, TODAY, 5));
        repo.save(new Coupon("OLDONE", 20, TODAY.minusDays(1), 5));
        repo.save(new Coupon("FULL", 5, TODAY.plusDays(5), 1));
        repo.flush();
        assertEquals(1, repo.redeem("FULL", TODAY));
    }

    @Test void entityGetsIdAndStartsAtZero() {
        Coupon c = repo.saveAndFlush(new Coupon("NEWC", 5, TODAY, 3));
        assertNotNull(c.getId());
        assertEquals(0, c.getRedemptions());
    }

    @Test void codeIsUnique() {
        assertThrows(DataIntegrityViolationException.class, () -> repo.saveAndFlush(new Coupon("SAVE10", 1, TODAY, 1)));
    }

    @Test void lookupsAreCaseSensitive() {
        assertTrue(repo.existsByCode("SAVE10"));
        assertFalse(repo.existsByCode("save10"));
        assertTrue(repo.findByCode("SAVE10").isPresent());
        assertTrue(repo.findByCode("nope").isEmpty());
    }

    @Test void redeemCountsUpToTheLimitThenStops() {
        assertEquals(1, repo.redeem("SAVE10", TODAY));
        assertEquals(1, repo.redeem("SAVE10", TODAY));
        assertEquals(0, repo.redeem("SAVE10", TODAY), "limit of 2 reached");
        assertEquals(2, repo.findByCode("SAVE10").orElseThrow().getRedemptions(), "the persistence context was refreshed");
    }

    @Test void redeemRespectsTheLastValidDayAndUnknownCodes() {
        assertEquals(1, repo.redeem("LASTDAY", TODAY), "valid through validUntil inclusive");
        assertEquals(0, repo.redeem("OLDONE", TODAY));
        assertEquals(0, repo.redeem("LASTDAY", TODAY.plusDays(1)));
        assertEquals(0, repo.redeem("NOSUCH", TODAY));
        assertEquals(0, repo.findByCode("OLDONE").orElseThrow().getRedemptions());
    }
}
