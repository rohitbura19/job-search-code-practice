package practice.d20261102.g;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

class TokenBucketLimiterTest {
    final MutableClock clock = new MutableClock();

    @Test void drainsThenDeniesWithRetryAfter() {
        RateLimiter l = new TokenBucketLimiter(clock, 3, Duration.ofSeconds(1));
        assertEquals(new Decision(true, 2, Duration.ZERO), l.check("k"));
        assertEquals(new Decision(true, 1, Duration.ZERO), l.check("k"));
        assertEquals(new Decision(true, 0, Duration.ZERO), l.check("k"));
        assertEquals(new Decision(false, 0, Duration.ofSeconds(1)), l.check("k"));
    }

    @Test void refillsOneTokenPerInterval() {
        RateLimiter l = new TokenBucketLimiter(clock, 2, Duration.ofSeconds(1));
        l.check("k"); l.check("k");
        clock.advance(Duration.ofMillis(400));
        assertEquals(new Decision(false, 0, Duration.ofMillis(600)), l.check("k"));
        clock.advance(Duration.ofMillis(600));
        assertTrue(l.check("k").allowed());
        assertFalse(l.check("k").allowed());
    }

    @Test void fractionsOfATokenAreNotLost() {
        RateLimiter l = new TokenBucketLimiter(clock, 1, Duration.ofSeconds(1));
        l.check("k");
        for (int i = 0; i < 9; i++) { clock.advance(Duration.ofMillis(100)); assertFalse(l.check("k").allowed()); } // 900 ms, polled often
        clock.advance(Duration.ofMillis(100));
        assertTrue(l.check("k").allowed());
    }

    @Test void refillIsCappedAtCapacity() {
        RateLimiter l = new TokenBucketLimiter(clock, 3, Duration.ofSeconds(1));
        l.check("k"); l.check("k"); l.check("k");
        clock.advance(Duration.ofDays(30));
        assertEquals(2, l.check("k").remaining());
        assertTrue(l.check("k").allowed());
        assertTrue(l.check("k").allowed());
        assertFalse(l.check("k").allowed());
    }

    @Test void keysAreIndependent() {
        RateLimiter l = new TokenBucketLimiter(clock, 1, Duration.ofSeconds(10));
        assertTrue(l.check("a").allowed());
        assertFalse(l.check("a").allowed());
        assertTrue(l.check("b").allowed());
    }

    @Test void clockGoingBackwardsDoesNotMintTokens() {
        RateLimiter l = new TokenBucketLimiter(clock, 1, Duration.ofSeconds(1));
        l.check("k");
        clock.advance(Duration.ofSeconds(-5));
        assertFalse(l.check("k").allowed());
        clock.advance(Duration.ofSeconds(5));
        assertFalse(l.check("k").allowed());
        clock.advance(Duration.ofSeconds(1));
        assertTrue(l.check("k").allowed());
    }

    @Test void evictIdleKeepsDrainedBucketsOnly() {
        RateLimiter l = new TokenBucketLimiter(clock, 2, Duration.ofSeconds(1));
        l.check("drained"); l.check("drained");
        l.check("partial");
        assertEquals(2, l.trackedKeys());
        l.evictIdle();
        assertEquals(2, l.trackedKeys());
        clock.advance(Duration.ofSeconds(1));
        l.evictIdle();                       // partial refilled to full -> dropped; drained has 1 of 2
        assertEquals(1, l.trackedKeys());
        clock.advance(Duration.ofSeconds(1));
        l.evictIdle();
        assertEquals(0, l.trackedKeys());
        assertEquals(1, l.check("drained").remaining());   // a dropped key behaves like a new one
    }

    @Test void invalidArguments() {
        assertThrows(IllegalArgumentException.class, () -> new TokenBucketLimiter(clock, 0, Duration.ofSeconds(1)));
        assertThrows(IllegalArgumentException.class, () -> new TokenBucketLimiter(clock, 1, Duration.ZERO));
    }

    @Test void concurrentChecksNeverOverAdmit() throws Exception {
        RateLimiter l = new TokenBucketLimiter(clock, 100, Duration.ofHours(1));
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

    @Test void evictionRacingWithChecksLosesNothing() throws Exception {
        RateLimiter l = new TokenBucketLimiter(clock, 50, Duration.ofHours(1));
        ExecutorService pool = Executors.newFixedThreadPool(9);
        AtomicInteger allowed = new AtomicInteger();
        CountDownLatch go = new CountDownLatch(1);
        for (int t = 0; t < 8; t++) pool.submit(() -> {
            go.await();
            for (int i = 0; i < 100; i++) if (l.check("k").allowed()) allowed.incrementAndGet();
            return null;
        });
        pool.submit(() -> { go.await(); for (int i = 0; i < 2000; i++) l.evictIdle(); return null; });
        go.countDown();
        pool.shutdown();
        assertTrue(pool.awaitTermination(10, TimeUnit.SECONDS));
        assertEquals(50, allowed.get());
    }

    @Test void manyChecksWithinTimeout() {
        RateLimiter l = new TokenBucketLimiter(clock, 5, Duration.ofMillis(1));
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> {
            for (int i = 0; i < 1_000_000; i++) { l.check("key" + (i % 1000)); if (i % 50 == 0) clock.advance(Duration.ofMillis(1)); }
        });
        assertEquals(1000, l.trackedKeys());
    }
}
