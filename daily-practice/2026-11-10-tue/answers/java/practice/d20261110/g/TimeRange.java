package practice.d20261110.g;

import java.time.Instant;

/** Given. Half-open interval [start, end): back-to-back ranges do not overlap. */
public record TimeRange(Instant start, Instant end) {
    public TimeRange {
        if (!end.isAfter(start)) throw new IllegalArgumentException("end must be after start");
    }
    public boolean overlaps(TimeRange o) { return start.isBefore(o.end) && o.start.isBefore(end); }
}
