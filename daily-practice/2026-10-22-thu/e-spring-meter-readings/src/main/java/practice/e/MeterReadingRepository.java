package practice.e;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

// TODO 1: the first two method NAMES are already derived-query names. Replace the @Query("TODO") on them: delete it, then say
//   why Spring Data can build the query from the name, whether "Between" includes both ends, and what the Pageable on a List does.
// TODO 2: write the JPQL for totalKwh: the sum of kwh of one meter, and 0 (not null) for a meter with no readings.
public interface MeterReadingRepository extends JpaRepository<MeterReading, Long> {
    @Query("TODO")
    List<MeterReading> findByMeterIdAndReadAtBetweenOrderByReadAtAsc(String meterId, Instant from, Instant to);

    @Query("TODO")
    List<MeterReading> findByMeterIdOrderByReadAtDesc(String meterId, Pageable page);

    @Query("TODO")
    BigDecimal totalKwh(@Param("meterId") String meterId);
}
