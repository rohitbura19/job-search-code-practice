package practice.e;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

@DataJpaTest
class MeterReadingRepositoryTest {
    static final Instant T0 = Instant.parse("2026-10-01T00:00:00Z");

    @Autowired MeterReadingRepository repo;

    static MeterReading r(String meter, String kwh, int hour) {
        return new MeterReading(meter, new BigDecimal(kwh), T0.plusSeconds(3600L * hour));
    }

    @BeforeEach void seed() {
        repo.saveAll(List.of(r("m1", "1.500", 3), r("m1", "2.250", 1), r("m1", "0.125", 2), r("m1", "4.000", 4), r("m2", "9.000", 1)));
    }

    @Test void entityGetsGeneratedIdAndKeepsScale() {
        MeterReading saved = repo.save(r("m3", "12.345", 1));
        assertNotNull(saved.getId());
        repo.flush();
        assertEquals(0, new BigDecimal("12.345").compareTo(repo.findById(saved.getId()).orElseThrow().getKwh()));
    }

    @Test void betweenIsInclusiveAndOrderedOldestFirst() {
        List<MeterReading> l = repo.findByMeterIdAndReadAtBetweenOrderByReadAtAsc("m1", T0.plusSeconds(3600), T0.plusSeconds(3 * 3600));
        assertEquals(List.of(T0.plusSeconds(3600), T0.plusSeconds(7200), T0.plusSeconds(10800)), l.stream().map(MeterReading::getReadAt).toList());
        assertTrue(repo.findByMeterIdAndReadAtBetweenOrderByReadAtAsc("m1", T0.plusSeconds(99_999), T0.plusSeconds(999_999)).isEmpty());
    }

    @Test void latestIsNewestFirstAndLimited() {
        List<MeterReading> l = repo.findByMeterIdOrderByReadAtDesc("m1", PageRequest.of(0, 2));
        assertEquals(List.of(T0.plusSeconds(4 * 3600), T0.plusSeconds(3 * 3600)), l.stream().map(MeterReading::getReadAt).toList());
        assertEquals(1, repo.findByMeterIdOrderByReadAtDesc("m2", PageRequest.of(0, 50)).size());
    }

    @Test void totalSumsOneMeterAndIsZeroWhenThereAreNoReadings() {
        assertEquals(0, new BigDecimal("7.875").compareTo(repo.totalKwh("m1")));
        BigDecimal none = repo.totalKwh("nobody");
        assertNotNull(none);
        assertEquals(0, BigDecimal.ZERO.compareTo(none));
    }
}
