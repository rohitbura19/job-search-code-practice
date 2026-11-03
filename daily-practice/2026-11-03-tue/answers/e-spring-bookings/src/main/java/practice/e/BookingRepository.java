package practice.e;

import java.time.Instant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    /** Two half-open intervals overlap iff each starts before the other ends. */
    @Query("select count(b) > 0 from Booking b where b.room = :room and b.startAt < :end and b.endAt > :start")
    boolean existsOverlap(@Param("room") String room, @Param("start") Instant start, @Param("end") Instant end);

    @Query(value = "select b from Booking b where b.room = :room and b.endAt > :since order by b.startAt",
           countQuery = "select count(b) from Booking b where b.room = :room and b.endAt > :since")
    Page<Booking> upcoming(@Param("room") String room, @Param("since") Instant since, Pageable pageable);

    /** Bulk JPQL delete: one statement, bypasses the persistence context and entity callbacks. */
    @Modifying
    @Query("delete from Booking b where b.endAt < :cutoff")
    int purgeEndedBefore(@Param("cutoff") Instant cutoff);
}
