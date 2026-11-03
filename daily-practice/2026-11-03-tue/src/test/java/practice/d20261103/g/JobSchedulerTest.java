package practice.d20261103.g;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JobSchedulerTest {
    static final Instant T0 = Instant.parse("2026-11-03T00:00:00Z");

    static final class TestClock extends Clock {
        Instant now = T0;
        void advance(Duration d) { now = now.plus(d); }
        @Override public ZoneId getZone() { return ZoneOffset.UTC; }
        @Override public Clock withZone(ZoneId z) { return this; }
        @Override public Instant instant() { return now; }
    }

    TestClock clock;
    JobScheduler s;
    List<String> log;

    @BeforeEach void setUp() { clock = new TestClock(); s = new JobScheduler(clock); log = new ArrayList<>(); }

    Job note(String x) { return () -> log.add(x); }

    @Test void backoffDoublesAndIsCapped() {
        RetryPolicy p = new RetryPolicy(10, Duration.ofSeconds(1));
        assertEquals(Duration.ofSeconds(1), p.backoff(1));
        assertEquals(Duration.ofSeconds(2), p.backoff(2));
        assertEquals(Duration.ofSeconds(8), p.backoff(4));
        assertEquals(Duration.ofHours(1), p.backoff(100));
        assertEquals(Duration.ofHours(1), new RetryPolicy(3, Duration.ofDays(2)).backoff(1));
        assertEquals(Duration.ZERO, RetryPolicy.NONE.backoff(5));
        assertThrows(IllegalArgumentException.class, () -> new RetryPolicy(0, Duration.ZERO));
        assertThrows(IllegalArgumentException.class, () -> new RetryPolicy(1, Duration.ofSeconds(-1)));
    }

    @Test void runsOnlyWhenDueInDueThenInsertionOrder() {
        s.schedule(note("c"), Duration.ofSeconds(10));
        s.schedule(note("a"), Duration.ofSeconds(5));
        s.schedule(note("b"), Duration.ofSeconds(5));
        assertEquals(0, s.runDue());
        clock.advance(Duration.ofSeconds(5));
        assertEquals(2, s.runDue());
        assertEquals(List.of("a", "b"), log);
        assertEquals(Optional.of(T0.plusSeconds(10)), s.nextDue());
        clock.advance(Duration.ofSeconds(100));
        assertEquals(1, s.runDue());
        assertEquals(List.of("a", "b", "c"), log);
        assertEquals(0, s.runDue());
        assertEquals(Optional.empty(), s.nextDue());
        assertEquals(0, s.pending());
    }

    @Test void zeroDelayIsDueImmediatelyAndIdsAreSequential() {
        assertEquals("job-1", s.schedule(note("x"), Duration.ZERO));
        assertEquals("job-2", s.schedule(note("y"), Duration.ZERO));
        assertEquals(2, s.runDue());
    }

    @Test void cancelSemantics() {
        String a = s.schedule(note("a"), Duration.ofSeconds(1));
        String b = s.schedule(note("b"), Duration.ofSeconds(2));
        assertTrue(s.cancel(a));
        assertFalse(s.cancel(a));
        assertFalse(s.cancel("nope"));
        assertEquals(1, s.pending());
        assertEquals(Optional.of(T0.plusSeconds(2)), s.nextDue());   // cancelled head is skipped
        clock.advance(Duration.ofSeconds(5));
        assertEquals(1, s.runDue());
        assertEquals(List.of("b"), log);
        assertFalse(s.cancel(b));                                    // already ran
    }

    @Test void retriesWithExponentialBackoffThenSucceeds() {
        int[] calls = {0};
        s.schedule(() -> { calls[0]++; if (calls[0] < 3) throw new IllegalStateException("boom"); log.add("ok"); },
                Duration.ZERO, new RetryPolicy(5, Duration.ofSeconds(1)));
        assertEquals(1, s.runDue());                                  // attempt 1 fails -> due +1s
        assertEquals(Optional.of(T0.plusSeconds(1)), s.nextDue());
        clock.advance(Duration.ofSeconds(1));
        assertEquals(1, s.runDue());                                  // attempt 2 fails -> due +2s
        assertEquals(Optional.of(T0.plusSeconds(3)), s.nextDue());
        clock.advance(Duration.ofSeconds(1));
        assertEquals(0, s.runDue());
        clock.advance(Duration.ofSeconds(1));
        assertEquals(1, s.runDue());                                  // attempt 3 succeeds
        assertEquals(List.of("ok"), log);
        assertEquals(0, s.pending());
        assertTrue(s.deadLetters().isEmpty());
    }

    @Test void exhaustedJobGoesToDeadLettersAndCanNoLongerBeCancelled() {
        String id = s.schedule(() -> { throw new Exception("always"); }, Duration.ZERO, new RetryPolicy(2, Duration.ofSeconds(1)));
        assertEquals(1, s.runDue());
        clock.advance(Duration.ofSeconds(1));
        assertEquals(1, s.runDue());
        assertEquals(List.of(id), s.deadLetters());
        assertEquals(0, s.pending());
        assertFalse(s.cancel(id));
        clock.advance(Duration.ofDays(1));
        assertEquals(0, s.runDue());
    }

    @Test void noRetryPolicyDiesOnFirstFailure() {
        String id = s.schedule(() -> { throw new RuntimeException("x"); }, Duration.ZERO);
        assertEquals(1, s.runDue());
        assertEquals(List.of(id), s.deadLetters());
    }

    @Test void zeroBackoffRetryDoesNotSpinInsideOneCall() {
        int[] calls = {0};
        s.schedule(() -> { calls[0]++; throw new RuntimeException("again"); }, Duration.ZERO, new RetryPolicy(3, Duration.ZERO));
        assertEquals(1, s.runDue());
        assertEquals(1, calls[0]);
        assertEquals(1, s.runDue());
        assertEquals(1, s.runDue());
        assertEquals(0, s.runDue());
        assertEquals(3, calls[0]);
    }

    @Test void jobMayScheduleAndCancelOthers() {
        String[] victim = new String[1];
        s.schedule(() -> { log.add("first"); s.schedule(note("child"), Duration.ZERO); s.cancel(victim[0]); }, Duration.ZERO);
        victim[0] = s.schedule(note("victim"), Duration.ZERO);
        assertEquals(1, s.runDue());                                  // the child waits for the next call, the victim never runs
        assertEquals(List.of("first"), log);
        assertEquals(1, s.runDue());
        assertEquals(List.of("first", "child"), log);
    }

    @Test void argumentValidation() {
        assertThrows(IllegalArgumentException.class, () -> s.schedule(note("x"), Duration.ofSeconds(-1)));
        assertThrows(NullPointerException.class, () -> s.schedule(null, Duration.ZERO));
        assertThrows(NullPointerException.class, () -> s.schedule(note("x"), Duration.ZERO, null));
    }

    @Test void randomOrderMatchesSortedByDueThenId() {
        Random rnd = new Random(42);
        for (int round = 0; round < 50; round++) {
            clock = new TestClock();
            s = new JobScheduler(clock);
            log = new ArrayList<>();
            int n = 1 + rnd.nextInt(40);
            List<long[]> expected = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                long d = rnd.nextInt(10);
                int idx = i;
                s.schedule(() -> log.add("j" + idx), Duration.ofSeconds(d));
                expected.add(new long[]{d, i});
            }
            expected.sort(Comparator.<long[]>comparingLong(x -> x[0]).thenComparingLong(x -> x[1]));
            clock.advance(Duration.ofSeconds(10));
            assertEquals(n, s.runDue());
            List<String> want = new ArrayList<>();
            for (long[] x : expected) want.add("j" + x[1]);
            assertEquals(want, log);
        }
    }

    @Test void manyJobsWithinTimeout() {
        int n = 200_000;
        assertTimeoutPreemptively(Duration.ofSeconds(4), () -> {
            for (int i = 0; i < n; i++) s.schedule(() -> {}, Duration.ofSeconds(i % 1000));
            for (int i = 0; i < n; i += 2) s.cancel("job-" + (i + 1));
            clock.advance(Duration.ofSeconds(1000));
            assertEquals(n / 2, s.runDue());
        });
    }
}
