package practice.d20261012.g;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class RateLimiterTest {

    static final long SEC = 1_000_000_000L;
    final long[] now = {0};                                   // fake clock in nanoseconds: no sleeps in tests

    InMemoryTokenBucketLimiter limiter(LimitResolver r) { return new InMemoryTokenBucketLimiter(r, () -> now[0]); }
    InMemoryTokenBucketLimiter limiter(long capacity, double perSecond) { return limiter(k -> new Limit(capacity, perSecond)); }

    @Test @DisplayName("burst of `capacity` is allowed, then denied; remaining counts down")
    void burstThenDeny() {
        InMemoryTokenBucketLimiter l = limiter(3, 1.0);
        assertEquals(Long.valueOf(2), Long.valueOf(l.tryAcquire("k").remaining()));
        assertEquals(Long.valueOf(1), Long.valueOf(l.tryAcquire("k").remaining()));
        Decision third = l.tryAcquire("k");
        assertTrue(third.allowed());
        assertEquals(Long.valueOf(0), Long.valueOf(third.remaining()));
        Decision fourth = l.tryAcquire("k");
        assertFalse(fourth.allowed());
        assertEquals(Long.valueOf(0), Long.valueOf(fourth.remaining()));
    }

    @Test @DisplayName("denied decision carries a retry-after of about the time until one token exists")
    void retryAfter() {
        InMemoryTokenBucketLimiter l = limiter(2, 1.0);       // 1 token per second
        l.tryAcquire("k"); l.tryAcquire("k");
        Decision d = l.tryAcquire("k");
        assertFalse(d.allowed());
        long ns = d.retryAfter().toNanos();
        assertTrue(ns >= 990_000_000L && ns <= 1_010_000_000L, "retryAfter was " + d.retryAfter());
        now[0] = SEC / 2;                                     // half a token has refilled: wait half a second
        long ns2 = l.tryAcquire("k").retryAfter().toNanos();
        assertTrue(ns2 >= 490_000_000L && ns2 <= 510_000_000L, "retryAfter was " + ns2);
        assertEquals(Duration.ZERO, l.tryAcquire("other").retryAfter());
    }

    @Test @DisplayName("tokens refill with time and never exceed capacity")
    void refill() {
        InMemoryTokenBucketLimiter l = limiter(2, 1.0);
        l.tryAcquire("k"); l.tryAcquire("k");
        assertFalse(l.tryAcquire("k").allowed());
        now[0] = SEC;                                         // one token back
        assertTrue(l.tryAcquire("k").allowed());
        assertFalse(l.tryAcquire("k").allowed());
        now[0] = 3_600 * SEC;                                 // an hour idle: capped at capacity 2, not 3600
        assertTrue(l.tryAcquire("k").allowed());
        assertTrue(l.tryAcquire("k").allowed());
        assertFalse(l.tryAcquire("k").allowed());
    }

    @Test @DisplayName("keys are isolated")
    void isolation() {
        InMemoryTokenBucketLimiter l = limiter(1, 0.001);
        assertTrue(l.tryAcquire("a").allowed());
        assertFalse(l.tryAcquire("a").allowed());
        assertTrue(l.tryAcquire("b").allowed());
    }

    @Test @DisplayName("per-plan limits come from the resolver")
    void perPlan() {
        InMemoryTokenBucketLimiter l = limiter(k -> k.startsWith("pro:") ? new Limit(5, 1) : new Limit(1, 1));
        assertTrue(l.tryAcquire("free:1").allowed());
        assertFalse(l.tryAcquire("free:1").allowed());
        for (int i = 0; i < 5; i++) assertTrue(l.tryAcquire("pro:1").allowed(), "call " + i);
        assertFalse(l.tryAcquire("pro:1").allowed());
    }

    @Test @DisplayName("50 threads x 200 calls against capacity 1,000 with no refill: exactly 1,000 allowed")
    void concurrency() throws Exception {
        InMemoryTokenBucketLimiter l = limiter(1_000, 0.0);
        AtomicInteger allowed = new AtomicInteger();
        ExecutorService pool = Executors.newFixedThreadPool(50);
        CountDownLatch go = new CountDownLatch(1);
        List<java.util.concurrent.Future<?>> fs = new ArrayList<>();
        for (int t = 0; t < 50; t++) fs.add(pool.submit(() -> {
            go.await();
            for (int i = 0; i < 200; i++) if (l.tryAcquire("hot").allowed()) allowed.incrementAndGet();
            return null;
        }));
        go.countDown();
        try {
            for (java.util.concurrent.Future<?> f : fs) f.get(10, TimeUnit.SECONDS);
        } finally {
            pool.shutdownNow();
        }
        assertEquals(1_000, allowed.get());
    }

    @Test @DisplayName("TokenBucket directly: starts full, take() is the whole refill + decrement step")
    void bucketDirect() {
        TokenBucket b = new TokenBucket(new Limit(2, 2.0), 100);
        assertTrue(b.take(100).allowed());
        assertTrue(b.take(100).allowed());
        assertFalse(b.take(100).allowed());
        assertTrue(b.take(100 + SEC / 2).allowed());          // 2 tokens/s -> 1 token after half a second
    }

    // ---------- Decorator ----------

    @Test @DisplayName("FailOpenLimiter: passes the delegate's decision through when healthy")
    void failOpenHealthy() {
        Decision denied = new Decision(false, 0, Duration.ofSeconds(2));
        assertSame(denied, new FailOpenLimiter(k -> denied).tryAcquire("k"));
    }

    @Test @DisplayName("FailOpenLimiter: allows when the delegate throws")
    void failOpenBroken() {
        RateLimiter broken = k -> { throw new IllegalStateException("redis down"); };
        Decision d = new FailOpenLimiter(broken).tryAcquire("k");
        assertTrue(d.allowed());
        assertEquals(Duration.ZERO, d.retryAfter());
    }

    // ---------- Chain ----------

    @Test @DisplayName("ChainedLimiter: all allow -> allowed with the smallest remaining")
    void chainAllAllow() {
        RateLimiter a = k -> new Decision(true, 10, Duration.ZERO);
        RateLimiter b = k -> new Decision(true, 3, Duration.ZERO);
        Decision d = new ChainedLimiter(List.of(a, b)).tryAcquire("k");
        assertTrue(d.allowed());
        assertEquals(Long.valueOf(3), Long.valueOf(d.remaining()));
    }

    @Test @DisplayName("ChainedLimiter: first denial wins and later limiters are not asked")
    void chainFirstDenial() {
        AtomicInteger asked = new AtomicInteger();
        Decision no = new Decision(false, 0, Duration.ofSeconds(7));
        RateLimiter first = k -> no;
        RateLimiter second = k -> { asked.incrementAndGet(); return new Decision(true, 1, Duration.ZERO); };
        Decision d = new ChainedLimiter(List.of(first, second)).tryAcquire("k");
        assertSame(no, d);
        assertEquals(0, asked.get());
    }

    @Test @DisplayName("ChainedLimiter: per-IP then per-key, both real token buckets")
    void chainReal() {
        InMemoryTokenBucketLimiter perIp = limiter(2, 0.001), perKey = limiter(5, 0.001);
        ChainedLimiter chain = new ChainedLimiter(List.of(perIp, perKey));
        assertTrue(chain.tryAcquire("x").allowed());
        assertTrue(chain.tryAcquire("x").allowed());
        assertFalse(chain.tryAcquire("x").allowed());
    }
}
