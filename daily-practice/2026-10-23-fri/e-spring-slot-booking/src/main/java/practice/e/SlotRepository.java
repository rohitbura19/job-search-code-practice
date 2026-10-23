package practice.e;

import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SlotRepository extends JpaRepository<Slot, Long> {
    // TODO 1: slots of one station that start at or after :from and still have room (booked < capacity), earliest first.
    @Query("TODO")
    List<Slot> available(@Param("stationId") String stationId, @Param("from") Instant from);

    // TODO 2: ATOMIC reservation in one statement: booked = booked + 1 where the id matches AND booked < capacity.
    //   It returns the number of rows changed (1 = reserved, 0 = full or missing). Why is this safer than read-then-save?
    //   Which @Modifying attribute keeps a later findById from returning a stale entity, and what does it cost?
    @Query("TODO")
    int reserve(@Param("id") long id);
}
