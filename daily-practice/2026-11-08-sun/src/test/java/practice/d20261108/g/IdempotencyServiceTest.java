package practice.d20261108.g;

import static org.junit.jupiter.api.Assertions.*;

import java.time.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import practice.d20261108.g.Outcome.*;

class IdempotencyServiceTest {
    static class MutableClock extends Clock {
        volatile Instant now = Instant.parse("2026-11-08T10:00:00Z");
        void advance(Duration d) { now = now.plus(d); }
        @Override public ZoneId getZone() { return ZoneOffset.UTC; }
        @Override public Clock withZone(ZoneId zone) { return this; }
        @Override public Instant instant() { return now; }
    }

    private MutableClock clock;
    private IdempotencyService svc;
    private static final StoredResponse OK = new StoredResponse(201, "{\"id\":1}");

    @BeforeEach void setUp() {
        clock = new MutableClock();
        svc = new IdempotencyService(clock, Duration.ofSeconds(30), Duration.ofHours(24));
    }

    private long start(String key, String fp) {
        Outcome o = svc.begin(key, fp);
        return assertInstanceOf(Started.class, o).token();
    }

    @Test void firstCallStartsThenInProgress() {
        start("k", "f1");
        assertInstanceOf(InProgress.class, svc.begin("k", "f1"));
        assertInstanceOf(Mismatch.class, svc.begin("k", "other"));
        assertInstanceOf(Started.class, svc.begin("other-key", "f1"));
    }

    @Test void completeThenReplay() {
        long t = start("k", "f1");
        assertTrue(svc.complete("k", t, OK));
        assertEquals(new Replay(OK), svc.begin("k", "f1"));
        assertEquals(new Replay(OK), svc.begin("k", "f1"));
        assertInstanceOf(Mismatch.class, svc.begin("k", "f2"));
        assertFalse(svc.complete("k", t, new StoredResponse(500, "x")), "already completed");
        assertEquals(new Replay(OK), svc.begin("k", "f1"));
    }

    @Test void wrongTokenAndUnknownKeyAreRejected() {
        long t = start("k", "f");
        assertFalse(svc.complete("k", t + 100, OK));
        assertFalse(svc.complete("nope", 1, OK));
        assertFalse(svc.abort("k", t + 100));
        assertFalse(svc.abort("nope", 1));
        assertInstanceOf(InProgress.class, svc.begin("k", "f"));
    }

    @Test void abortAllowsImmediateRetry() {
        long t = start("k", "f");
        assertTrue(svc.abort("k", t));
        assertEquals(0, svc.size());
        assertNotEquals(t, start("k", "f"));
        long t2 = start("j", "f");
        svc.complete("j", t2, OK);
        assertFalse(svc.abort("j", t2), "completed records cannot be aborted");
    }

    @Test void leaseExpiryAllowsTakeoverAndFencesTheOldWorker() {
        long old = start("k", "f");
        clock.advance(Duration.ofSeconds(29));
        assertInstanceOf(InProgress.class, svc.begin("k", "f"));
        clock.advance(Duration.ofSeconds(1)); // exactly at lease end -> expired
        long fresh = start("k", "f");
        assertTrue(fresh > old);
        assertFalse(svc.complete("k", old, new StoredResponse(500, "stale")));
        assertTrue(svc.complete("k", fresh, OK));
        assertEquals(new Replay(OK), svc.begin("k", "f"));
    }

    @Test void expiredLeaseAllowsDifferentFingerprint() {
        start("k", "f1");
        clock.advance(Duration.ofSeconds(31));
        assertInstanceOf(Started.class, svc.begin("k", "f2"));
    }

    @Test void retentionExpiryStartsFresh() {
        long t = start("k", "f");
        svc.complete("k", t, OK);
        clock.advance(Duration.ofHours(24).minusSeconds(1));
        assertInstanceOf(Replay.class, svc.begin("k", "f"));
        clock.advance(Duration.ofSeconds(1));
        assertInstanceOf(Started.class, svc.begin("k", "f"));
    }

    @Test void retentionStartsAtCompletionNotAtBegin() {
        long t = start("k", "f");
        clock.advance(Duration.ofSeconds(20));
        svc.complete("k", t, OK);
        clock.advance(Duration.ofHours(24).minusSeconds(1));
        assertInstanceOf(Replay.class, svc.begin("k", "f"));
    }

    @Test void purge() {
        start("a", "f"); start("b", "f");
        long c = start("c", "f");
        svc.complete("c", c, OK);
        clock.advance(Duration.ofSeconds(31));
        assertEquals(2, svc.purgeExpired());
        assertEquals(1, svc.size());
        clock.advance(Duration.ofDays(2));
        assertEquals(1, svc.purgeExpired());
        assertEquals(0, svc.size());
    }

    @Test void inputValidation() {
        assertThrows(NullPointerException.class, () -> svc.begin(null, "f"));
        assertThrows(NullPointerException.class, () -> svc.begin("k", null));
        assertThrows(IllegalArgumentException.class, () -> svc.begin("  ", "f"));
        assertThrows(NullPointerException.class, () -> svc.complete("k", 1, null));
    }

    @Test void exactlyOneWinnerUnderContention() throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(16);
        try {
            for (int round = 0; round < 200; round++) {
                String key = "key-" + round;
                CyclicBarrier go = new CyclicBarrier(16);
                List<Future<Outcome>> fs = new ArrayList<>();
                for (int i = 0; i < 16; i++) fs.add(pool.submit(() -> { go.await(); return svc.begin(key, "same"); }));
                int started = 0, inProgress = 0;
                for (Future<Outcome> f : fs) {
                    Outcome o = f.get(5, TimeUnit.SECONDS);
                    if (o instanceof Started) started++; else if (o instanceof InProgress) inProgress++;
                }
                assertEquals(1, started, key);
                assertEquals(15, inProgress, key);
            }
        } finally { pool.shutdownNow(); }
    }

    @Test void processedOnceAcrossRetries() throws Exception {
        AtomicInteger processed = new AtomicInteger();
        ExecutorService pool = Executors.newFixedThreadPool(8);
        try {
            List<Future<Integer>> fs = new ArrayList<>();
            for (int i = 0; i < 400; i++) fs.add(pool.submit(() -> {
                for (int attempt = 0; attempt < 10_000; attempt++) {
                    Outcome o = svc.begin("pay-1", "amount=10");
                    if (o instanceof Started s) {
                        processed.incrementAndGet();
                        svc.complete("pay-1", s.token(), OK);
                        return 201;
                    }
                    if (o instanceof Replay r) return r.response().status();
                    Thread.onSpinWait();
                }
                return -1;
            }));
            for (Future<Integer> f : fs) assertEquals(201, f.get(10, TimeUnit.SECONDS));
            assertEquals(1, processed.get());
        } finally { pool.shutdownNow(); }
    }

    @Test void manyKeys() {
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> {
            for (int i = 0; i < 300_000; i++) {
                long t = start("k" + i, "f");
                svc.complete("k" + i, t, OK);
            }
            assertEquals(300_000, svc.size());
            clock.advance(Duration.ofDays(2));
            assertEquals(300_000, svc.purgeExpired());
        });
    }
}
