package practice.d20261023.g;

import static org.junit.jupiter.api.Assertions.*;

import java.time.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InMemorySchedulerTest {
    static final Instant T0 = Instant.parse("2026-10-23T10:00:00Z");

    static final class TestClock extends Clock {
        Instant now = T0;
        void advance(Duration d) { now = now.plus(d); }
        @Override public ZoneId getZone() { return ZoneOffset.UTC; }
        @Override public Clock withZone(ZoneId z) { return this; }
        @Override public Instant instant() { return now; }
    }

    TestClock clock;
    InMemoryScheduler s;
    List<String> ran;

    @BeforeEach void setUp() {
        clock = new TestClock();
        s = new InMemoryScheduler(clock, new SchedulerConfig(3, Duration.ofSeconds(10)));
        ran = new ArrayList<>();
    }

    Job rec(String name) { return () -> ran.add(name); }

    @Test void runsOnlyDueJobsInTimeThenInsertionOrder() {
        s.schedule("late", T0.plusSeconds(30), rec("late"));
        s.schedule("b", T0.plusSeconds(5), rec("b"));
        s.schedule("a", T0.plusSeconds(5), rec("a"));
        s.schedule("first", T0.plusSeconds(1), rec("first"));
        assertEquals(0, s.runDue());
        clock.advance(Duration.ofSeconds(5));
        assertEquals(3, s.runDue());
        assertEquals(List.of("first", "b", "a"), ran);
        assertEquals(1, s.pending());
        assertEquals(Optional.of(T0.plusSeconds(30)), s.nextRunAt());
        assertEquals(0, s.runDue(), "nothing runs twice");
    }

    @Test void dueExactlyNowRunsAndPastDueRuns() {
        s.schedule("now", T0, rec("now"));
        s.schedule("past", T0.minusSeconds(60), rec("past"));
        assertEquals(2, s.runDue());
        assertEquals(List.of("past", "now"), ran);
        assertEquals(Optional.empty(), s.nextRunAt());
    }

    @Test void duplicateIdIsRejectedWhilePendingAndFreeAfterRun() {
        assertTrue(s.schedule("x", T0, rec("1")));
        assertFalse(s.schedule("x", T0.plusSeconds(99), rec("2")));
        assertEquals(1, s.pending());
        s.runDue();
        assertTrue(s.schedule("x", T0, rec("3")));
        s.runDue();
        assertEquals(List.of("1", "3"), ran);
    }

    @Test void cancel() {
        s.schedule("x", T0, rec("x"));
        s.schedule("y", T0, rec("y"));
        assertTrue(s.cancel("x"));
        assertFalse(s.cancel("x"));
        assertFalse(s.cancel("never"));
        assertEquals(1, s.pending());
        assertEquals(1, s.runDue());
        assertEquals(List.of("y"), ran);
        assertTrue(s.schedule("x", T0, rec("x2")), "cancelled id can be reused");
    }

    @Test void failedJobIsRetriedWithExponentialBackoffThenSucceeds() {
        AtomicInteger calls = new AtomicInteger();
        s.schedule("flaky", T0, () -> {
            if (calls.incrementAndGet() < 3) throw new IllegalStateException("boom");
        });
        assertEquals(1, s.runDue());                                  // attempt 1 fails -> retry at +10s
        assertEquals(Optional.of(T0.plusSeconds(10)), s.nextRunAt());
        assertEquals(1, s.pending());
        assertFalse(s.schedule("flaky", T0, rec("dup")), "id is still pending while waiting for a retry");
        clock.advance(Duration.ofSeconds(9));
        assertEquals(0, s.runDue());
        clock.advance(Duration.ofSeconds(1));
        assertEquals(1, s.runDue());                                  // attempt 2 fails -> retry at +20s from now
        assertEquals(Optional.of(T0.plusSeconds(30)), s.nextRunAt());
        clock.advance(Duration.ofSeconds(20));
        assertEquals(1, s.runDue());                                  // attempt 3 succeeds
        assertEquals(3, calls.get());
        assertEquals(0, s.pending());
        assertTrue(s.deadLetters().isEmpty());
    }

    @Test void jobThatKeepsFailingGoesToDeadLettersAfterMaxAttempts() {
        AtomicInteger calls = new AtomicInteger();
        s.schedule("bad", T0, () -> { calls.incrementAndGet(); throw new Exception("always"); });
        s.schedule("ok", T0.plusSeconds(1), rec("ok"));
        for (int i = 0; i < 10; i++) { s.runDue(); clock.advance(Duration.ofSeconds(60)); }
        assertEquals(3, calls.get());
        assertEquals(List.of("bad"), s.deadLetters());
        assertEquals(List.of("ok"), ran);
        assertEquals(0, s.pending());
        assertTrue(s.schedule("bad", T0, rec("again")), "a dead id is free again");
    }

    @Test void aFailingJobDoesNotBlockTheOthersAndRetryNeverRerunsInTheSameCall() {
        AtomicInteger calls = new AtomicInteger();
        s.schedule("bad", T0, () -> { calls.incrementAndGet(); throw new RuntimeException("x"); });
        s.schedule("good", T0, rec("good"));
        assertEquals(2, s.runDue());
        assertEquals(1, calls.get());
        assertEquals(List.of("good"), ran);
    }

    @Test void cancelWhileWaitingForRetry() {
        s.schedule("bad", T0, () -> { throw new RuntimeException("x"); });
        s.runDue();
        assertTrue(s.cancel("bad"));
        clock.advance(Duration.ofMinutes(5));
        assertEquals(0, s.runDue());
        assertTrue(s.deadLetters().isEmpty());
    }

    @Test void manyJobsPerformance() {
        for (int i = 0; i < 200_000; i++) s.schedule("j" + i, T0.plusMillis(i % 1000), () -> { });
        for (int i = 0; i < 200_000; i += 2) s.cancel("j" + i);
        clock.advance(Duration.ofSeconds(5));
        org.junit.jupiter.api.Assertions.assertTimeoutPreemptively(Duration.ofSeconds(3), () -> assertEquals(100_000, s.runDue()));
    }
}
