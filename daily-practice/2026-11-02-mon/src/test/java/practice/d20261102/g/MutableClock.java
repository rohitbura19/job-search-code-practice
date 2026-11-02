package practice.d20261102.g;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;

/** Test helper: a clock you move by hand. */
final class MutableClock extends Clock {
    private volatile long millis = 1_000_000L;

    void advance(Duration d) { millis += d.toMillis(); }
    void set(long ms) { millis = ms; }
    @Override public long millis() { return millis; }
    @Override public Instant instant() { return Instant.ofEpochMilli(millis); }
    @Override public ZoneId getZone() { return ZoneOffset.UTC; }
    @Override public Clock withZone(ZoneId zone) { return this; }
}
