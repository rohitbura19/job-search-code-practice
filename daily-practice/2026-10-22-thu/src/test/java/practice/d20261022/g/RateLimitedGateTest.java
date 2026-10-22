package practice.d20261022.g;

import static org.junit.jupiter.api.Assertions.*;
import static practice.d20261022.g.Decision.*;

import java.time.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RateLimitedGateTest {
    static final ZoneId IST = ZoneId.of("Asia/Kolkata");

    static final class TestClock extends Clock {
        Instant now;
        TestClock(Instant start) { now = start; }
        void advance(Duration d) { now = now.plus(d); }
        @Override public ZoneId getZone() { return ZoneOffset.UTC; }
        @Override public Clock withZone(ZoneId z) { return this; }
        @Override public Instant instant() { return now; }
    }

    TestClock clock;
    RateLimitedGate gate;

    static Instant istAt(int h, int m) { return LocalDate.of(2026, 10, 22).atTime(h, m).atZone(IST).toInstant(); }

    @BeforeEach void setUp() {
        clock = new TestClock(istAt(12, 0));
        gate = new RateLimitedGate(clock, new GateConfig(3, Duration.ofMinutes(10), Duration.ofHours(1),
                LocalTime.of(22, 0), LocalTime.of(7, 0), IST));
    }

    static Notification push(String user, String key) { return new Notification(user, Channel.PUSH, key, false); }

    @Test void allowsUpToLimitThenThrottles() {
        for (int i = 0; i < 3; i++) assertEquals(SEND, gate.decide(push("u1", null)));
        assertEquals(THROTTLED, gate.decide(push("u1", null)));
        assertEquals(SEND, gate.decide(push("u2", null)), "other user unaffected");
        assertEquals(SEND, gate.decide(new Notification("u1", Channel.EMAIL, null, false)), "other channel unaffected");
    }

    @Test void windowSlides() {
        gate.decide(push("u1", null));                  // t=0
        clock.advance(Duration.ofMinutes(4));
        gate.decide(push("u1", null));                  // t=4
        gate.decide(push("u1", null));                  // t=4
        assertEquals(THROTTLED, gate.decide(push("u1", null)));
        clock.advance(Duration.ofMinutes(6));           // t=10: the t=0 send is exactly one window old and falls out
        assertEquals(SEND, gate.decide(push("u1", null)));
        assertEquals(THROTTLED, gate.decide(push("u1", null)));
    }

    @Test void throttledAttemptsAreNotRecorded() {
        for (int i = 0; i < 3; i++) gate.decide(push("u1", null));
        for (int i = 0; i < 50; i++) assertEquals(THROTTLED, gate.decide(push("u1", null)));
        clock.advance(Duration.ofMinutes(10));
        assertEquals(SEND, gate.decide(push("u1", null)));
    }

    @Test void duplicateWithinTtl() {
        assertEquals(SEND, gate.decide(push("u1", "order-9")));
        assertEquals(DUPLICATE, gate.decide(push("u1", "order-9")));
        assertEquals(SEND, gate.decide(push("u1", "order-10")));
        assertEquals(SEND, gate.decide(push("u2", "order-9")), "dedupe is per user");
        clock.advance(Duration.ofHours(1));
        assertEquals(SEND, gate.decide(push("u1", "order-9")), "ttl elapsed");
    }

    @Test void duplicateIsCheckedBeforeRateLimitAndDoesNotConsumeQuota() {
        gate.decide(push("u1", "k"));
        for (int i = 0; i < 20; i++) assertEquals(DUPLICATE, gate.decide(push("u1", "k")));
        assertEquals(SEND, gate.decide(push("u1", null)));
        assertEquals(SEND, gate.decide(push("u1", null)));
        assertEquals(THROTTLED, gate.decide(push("u1", null)));
    }

    @Test void quietHoursWrapMidnight() {
        clock.now = istAt(23, 30);
        assertEquals(QUIET_HOURS, gate.decide(push("u1", null)));
        clock.now = istAt(3, 0);
        assertEquals(QUIET_HOURS, gate.decide(push("u1", null)));
        clock.now = istAt(7, 0);
        assertEquals(SEND, gate.decide(push("u1", null)), "end is exclusive");
        clock.now = istAt(22, 0);
        assertEquals(QUIET_HOURS, gate.decide(push("u2", null)), "start is inclusive");
        clock.now = istAt(21, 59);
        assertEquals(SEND, gate.decide(push("u2", null)));
    }

    @Test void urgentIgnoresQuietHoursButNotRateLimit() {
        clock.now = istAt(2, 0);
        Notification urgent = new Notification("u1", Channel.SMS, null, true);
        for (int i = 0; i < 3; i++) assertEquals(SEND, gate.decide(urgent));
        assertEquals(THROTTLED, gate.decide(urgent));
    }

    @Test void quietHoursAreEvaluatedInTheConfiguredZoneNotUtc() {
        clock.now = Instant.parse("2026-10-22T17:00:00Z");      // 22:30 in Kolkata, 17:00 UTC
        assertEquals(QUIET_HOURS, gate.decide(push("u1", null)));
    }

    @Test void quietHoursDoNotConsumeQuotaOrDedupe() {
        clock.now = istAt(23, 0);
        assertEquals(QUIET_HOURS, gate.decide(push("u1", "k")));
        clock.now = istAt(8, 0).plus(Duration.ofDays(1));
        assertEquals(SEND, gate.decide(push("u1", "k")));
    }
}
