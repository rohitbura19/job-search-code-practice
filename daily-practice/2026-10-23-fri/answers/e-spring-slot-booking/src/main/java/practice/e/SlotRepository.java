package practice.e;

import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SlotRepository extends JpaRepository<Slot, Long> {
    @Query("select s from Slot s where s.stationId = :stationId and s.startsAt >= :from and s.booked < s.capacity order by s.startsAt")
    List<Slot> available(@Param("stationId") String stationId, @Param("from") Instant from);

    // One statement = the database serialises concurrent reservations of the same row; no lost update, no oversell.
    // clearAutomatically evicts the persistence context so the next find sees the new count (cost: other managed entities detach).
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Slot s set s.booked = s.booked + 1 where s.id = :id and s.booked < s.capacity")
    int reserve(@Param("id") long id);
}
