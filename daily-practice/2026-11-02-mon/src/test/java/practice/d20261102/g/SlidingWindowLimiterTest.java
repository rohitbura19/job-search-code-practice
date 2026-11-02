package practice.d20261102.g;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

class SlidingWindowLimiterTest {
    final MutableClock clock = new MutableClock();

    @Test void admitsLimitThenDeniesWithRetryAfter() {
        RateLimiter l = new SlidingWindowLimiter(clock, 2, Duration.ofSeconds(10));
        assertEquals(new Decision(true, 1, Duration.ZERO), l.check("k"));
        clock.advance(Duration.ofSeconds(1));
        assertEquals(new Decision(true, 0, Duration.ZERO), l.check("k"));
        assertEquals(new Decision(false, 0, Duration.ofSeconds(9)), l.check("k"));
    }

    @Test void entryExpiresExactlyAtWindowEnd() {
        RateLimiter l = new SlidingWindowLimiter(clock, 1, Duration.ofSeconds(10));
        assertTrue(l.check("k").allowed());
        clock.advance(Duration.ofMillis(9_999));
        assertEquals(new Decision(false, 0, Duration.ofMillis(1)), l.check("k"));
        clock.advance(Duration.ofMillis(1));
        assertTrue(l.check("k").allowed());
    }

    @Test void deniedChecksAreNotRecorded() {
        RateLimiter l = new SlidingWindowLimiter(clock, 1, Duration.ofSeconds(10));
        l.check("k");
        for (int i = 0; i < 5; i++) { clock.advance(Duration.ofSeconds(1)); assertFalse(l.check("k").allowed()); }
        clock.advance(Duration.ofSeconds(5));     // t = 10 s after the only admitted check
        assertTrue(l.check("k").allowed());
    }

    @Test void noBoundaryBurstUnlikeFixedWindow() {
        RateLimiter l = new SlidingWindowLimiter(clock, 3, Duration.ofSeconds(10));
        clock.advance(Duration.ofSeconds(9));
        for (int i = 0; i < 3; i++) assertTrue(l.check("k").allowed());
        clock.advance(Duration.ofSeconds(2));      // a fixed window would have reset here
        assertFalse(l.check("k").allowed());
    }

    @Test void keysAreIndependent() {
        RateLimiter l = new SlidingWindowLimiter(clock, 1, Duration.ofSeconds(10));
        assertTrue(l.check("a").allowed());
        assertTrue(l.check("b").allowed());
        assertFalse(l.check("a").allowed());
    }

    @Test void evictIdleDropsOnlyExpiredLogs() {
        RateLimiter l = new SlidingWindowLimiter(clock, 2, Duration.ofSeconds(10));
        l.check("old");
        clock.advance(Duration.ofSeconds(6));
        l.check("new");
        assertEquals(2, l.trackedKeys());
        clock.advance(Duration.ofSeconds(5));
        l.evictIdle();
        assertEquals(1, l.trackedKeys());
        clock.advance(Duration.ofSeconds(5));
        l.evictIdle();
        assertEquals(0, l.trackedKeys());
    }

    @Test void invalidArguments() {
        assertThrows(IllegalArgumentException.class, () -> new SlidingWindowLimiter(clock, 0, Duration.ofSeconds(1)));
        assertThrows(IllegalArgumentException.class, () -> new SlidingWindowLimiter(clock, 1, Duration.ofNanos(1)));
    }

    @Test void concurrentChecksNeverOverAdmit() throws Exception {
        RateLimiter l = new SlidingWindowLimiter(clock, 100, Duration.ofHours(1));
        ExecutorService pool = Executors.newFixedThreadPool(32);
        AtomicInteger allowed = new AtomicInteger();
        CountDownLatch go = new CountDownLatch(1);
        for (int t = 0; t < 32; t++) pool.submit(() -> {
            go.await();
            for (int i = 0; i < 50; i++) if (l.check("hot").allowed()) allowed.incrementAndGet();
            return null;
        });
        go.countDown();
        pool.shutdown();
        assertTrue(pool.awaitTermination(10, TimeUnit.SECONDS));
        assertEquals(100, allowed.get());
    }

    @Test void manyChecksWithinTimeout() {
        RateLimiter l = new SlidingWindowLimiter(clock, 5, Duration.ofMillis(100));
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> {
            for (int i = 0; i < 1_000_000; i++) { l.check("key" + (i % 1000)); if (i % 50 == 0) clock.advance(Duration.ofMillis(1)); }
        });
    }
}
