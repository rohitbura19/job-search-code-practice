package practice.e;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@DataJpaTest
class BookingRepositoryTest {
    static final Instant T = Instant.parse("2026-11-10T10:00:00Z");
    @Autowired BookingRepository repo;

    Booking slot(String room, int fromHour, int toHour) {
        return new Booking(room, T.plusSeconds(fromHour * 3600L), T.plusSeconds(toHour * 3600L), "a@x.com");
    }

    @Test void overlapDetectionHalfOpen() {
        repo.saveAndFlush(slot("A", 0, 2));       // 10:00-12:00
        Instant s = T, e = T.plusSeconds(7200);
        assertThat(repo.existsOverlap("A", s.plusSeconds(3600), e.plusSeconds(3600))).isTrue();   // 11-13 overlaps
        assertThat(repo.existsOverlap("A", s.minusSeconds(3600), s.plusSeconds(1800))).isTrue();  // 9-10:30 overlaps
        assertThat(repo.existsOverlap("A", s.plusSeconds(1800), s.plusSeconds(3600))).isTrue();   // inside
        assertThat(repo.existsOverlap("A", s.minusSeconds(3600), e.plusSeconds(3600))).isTrue();  // surrounds
        assertThat(repo.existsOverlap("A", e, e.plusSeconds(3600))).isFalse();                    // starts exactly when it ends
        assertThat(repo.existsOverlap("A", s.minusSeconds(3600), s)).isFalse();                   // ends exactly when it starts
        assertThat(repo.existsOverlap("B", s, e)).isFalse();                                      // another room
    }

    @Test void upcomingIsFilteredOrderedAndPaged() {
        repo.save(slot("A", 4, 5));
        repo.save(slot("A", 0, 1));
        repo.save(slot("A", 2, 3));
        repo.save(slot("B", 1, 2));
        repo.saveAndFlush(slot("A", -5, -4));     // already over
        Page<Booking> p1 = repo.upcoming("A", T, PageRequest.of(0, 2));
        assertThat(p1.getTotalElements()).isEqualTo(3);
        assertThat(p1.getContent()).extracting(Booking::getStartAt).containsExactly(T, T.plusSeconds(2 * 3600L));
        Page<Booking> p2 = repo.upcoming("A", T, PageRequest.of(1, 2));
        assertThat(p2.getContent()).hasSize(1);
        assertThat(repo.upcoming("zzz", T, PageRequest.of(0, 5)).getTotalElements()).isZero();
        // a booking that is still running at `since` counts as upcoming (ends after since)
        assertThat(repo.upcoming("A", T.plusSeconds(1800), PageRequest.of(0, 10)).getTotalElements()).isEqualTo(3);
    }

    @Test void purgeDeletesOnlyEndedBookings() {
        repo.save(slot("A", -5, -4));
        repo.save(slot("A", -2, -1));
        repo.save(slot("A", 0, 1));
        repo.flush();
        assertThat(repo.purgeEndedBefore(T)).isEqualTo(2);
        assertThat(repo.count()).isEqualTo(1);
        assertThat(repo.purgeEndedBefore(T)).isZero();
    }
}
