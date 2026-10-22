package practice.e;

import java.math.BigDecimal;
import java.time.Instant;

public record ReadingView(Long id, String meterId, BigDecimal kwh, Instant readAt) {
    static ReadingView of(MeterReading r) { return new ReadingView(r.getId(), r.getMeterId(), r.getKwh(), r.getReadAt()); }
}
