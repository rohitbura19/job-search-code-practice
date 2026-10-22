package practice.e;

import java.math.BigDecimal;
import java.time.Instant;

// TODO: Bean Validation: meterId not blank, max 40; kwh required, not negative, at most 9 integer and 3 fraction digits;
//       readAt required and not in the future.
public record ReadingRequest(String meterId, BigDecimal kwh, Instant readAt) {}
