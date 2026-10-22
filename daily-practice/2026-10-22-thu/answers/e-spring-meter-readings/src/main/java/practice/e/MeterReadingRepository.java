package practice.e;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * "Between" in a derived query is INCLUSIVE on both ends. A Pageable on a List-returning method only limits (no count query is run);
 * its sort would be appended to the OrderBy, so pass an unsorted PageRequest.of(0, n).
 * coalesce(sum(...), 0): SUM over zero rows is NULL in SQL.
 */
public interface MeterReadingRepository extends JpaRepository<MeterReading, Long> {
    List<MeterReading> findByMeterIdAndReadAtBetweenOrderByReadAtAsc(String meterId, Instant from, Instant to);

    List<MeterReading> findByMeterIdOrderByReadAtDesc(String meterId, Pageable page);

    @Query("select coalesce(sum(r.kwh), 0) from MeterReading r where r.meterId = :meterId")
    BigDecimal totalKwh(@Param("meterId") String meterId);
}
