package practice.e;

import java.time.Instant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    // TODO: true iff a booking in `room` overlaps the half-open interval [start, end).
    //  Overlap means existing.startAt < end AND existing.endAt > start. Explicit JPQL @Query with @Param, returning "count(b) > 0".
    boolean existsOverlap(String room, Instant start, Instant end);

    // TODO: bookings of `room` that end after `since`, ordered by startAt ascending, paged.
    //  Either a derived query name or @Query (then add a countQuery).
    Page<Booking> upcoming(String room, Instant since, Pageable pageable);

    // TODO: bulk delete of bookings that ended before `cutoff`; returns the number of rows deleted.
    //  Needs @Modifying (and @Query); callers must run it inside a transaction.
    int purgeEndedBefore(Instant cutoff);
}
