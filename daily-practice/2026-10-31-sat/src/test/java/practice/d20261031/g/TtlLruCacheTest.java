package practice.d20261031.g;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

class TtlLruCacheTest {
    static final class FakeClock extends Clock {
        volatile long now = 1_000;
        void advance(long ms) { now += ms; }
        @Override public ZoneId getZone() { return ZoneOffset.UTC; }
        @Override public Clock withZone(ZoneId z) { return this; }
        @Override public Instant instant() { return Instant.ofEpochMilli(now); }
        @Override public long millis() { return now; }
    }

    final FakeClock clock = new FakeClock();
    final AtomicInteger calls = new AtomicInteger();

    TtlLruCache<String, String> cache(int max, Map<String, String> db) {
        return new TtlLruCache<>(max, Duration.ofSeconds(10), Duration.ofSeconds(2),
                k -> { calls.incrementAndGet(); return Optional.ofNullable(db.get(k)); }, clock);
    }

    @Test void missThenHit() {
        var c = cache(10, Map.of("a", "1"));
        assertEquals(Optional.of("1"), c.get("a"));
        assertEquals(Optional.of("1"), c.get("a"));
        assertEquals(1, calls.get());
        assertEquals(new CacheStats(1, 1, 1, 0, 0, 0), c.stats());
    }

    @Test void ttlBoundaryIsExclusive() {
        var c = cache(10, Map.of("a", "1"));
        c.get("a");
        clock.advance(9_999);
        c.get("a");
        assertEquals(1, calls.get());
        clock.advance(1);                                  // exactly ttl: expired
        c.get("a");
        assertEquals(2, calls.get());
        assertEquals(1, c.stats().expirations());
    }

    @Test void negativeResultsAreCachedWithTheirOwnTtl() {
        Map<String, String> db = new java.util.HashMap<>();
        var c = cache(10, db);
        assertEquals(Optional.empty(), c.get("x"));
        db.put("x", "now-exists");
        assertEquals(Optional.empty(), c.get("x"));         // still the cached "absent"
        assertEquals(1, calls.get());
        clock.advance(2_000);
        assertEquals(Optional.of("now-exists"), c.get("x"));
        assertEquals(2, calls.get());
    }

    @Test void evictsLeastRecentlyUsed() {
        var c = cache(2, Map.of("a", "1", "b", "2", "c", "3"));
        c.get("a"); c.get("b"); c.get("a");                  // a is now newer than b
        c.get("c");                                          // evicts b
        assertEquals(2, c.size());
        assertEquals(1, c.stats().evictions());
        int before = calls.get();
        c.get("a");
        assertEquals(before, calls.get());                   // a survived
        c.get("b");
        assertEquals(before + 1, calls.get());               // b was evicted
    }

    @Test void loaderFailureIsNotCachedAndWrapsTheCause() {
        AtomicInteger n = new AtomicInteger();
        var c = new TtlLruCache<String, String>(5, Duration.ofSeconds(10), Duration.ofSeconds(2), k -> {
            if (n.incrementAndGet() == 1) throw new IllegalStateException("db down");
            return Optional.of("ok");
        }, clock);
        CacheLoadException ex = assertThrows(CacheLoadException.class, () -> c.get("k"));
        assertInstanceOf(IllegalStateException.class, ex.getCause());
        assertEquals(Optional.of("ok"), c.get("k"));
        assertEquals(1, c.stats().loadFailures());
        assertEquals(1, c.size());
    }

    @Test void invalidateAndPurge() {
        var c = cache(10, Map.of("a", "1", "b", "2"));
        c.get("a"); c.get("b");
        c.invalidate("a");
        assertEquals(1, c.size());
        c.get("a");
        assertEquals(3, calls.get());
        clock.advance(10_000);
        assertEquals(2, c.purgeExpired());
        assertEquals(0, c.size());
        c.invalidate("never-there");                         // no-op
    }

    @Test void singleFlightUnderConcurrency() throws Exception {
        AtomicInteger n = new AtomicInteger();
        var c = new TtlLruCache<String, String>(10, Duration.ofSeconds(10), Duration.ofSeconds(2), k -> {
            n.incrementAndGet();
            try { Thread.sleep(200); } catch (InterruptedException e) { throw new IllegalStateException(e); }
            return Optional.of("v");
        }, clock);
        ExecutorService pool = Executors.newFixedThreadPool(16);
        CountDownLatch go = new CountDownLatch(1);
        List<Future<Optional<String>>> fs = new ArrayList<>();
        for (int i = 0; i < 16; i++) fs.add(pool.submit(() -> { go.await(); return c.get("hot"); }));
        go.countDown();
        for (Future<Optional<String>> f : fs) assertEquals(Optional.of("v"), f.get(5, TimeUnit.SECONDS));
        pool.shutdown();
        assertEquals(1, n.get());
        assertEquals(1, c.stats().loads());
    }

    @Test void waitersOfAFailedFlightAllFail() throws Exception {
        CountDownLatch entered = new CountDownLatch(1), release = new CountDownLatch(1);
        var c = new TtlLruCache<String, String>(10, Duration.ofSeconds(10), Duration.ofSeconds(2), k -> {
            entered.countDown();
            try { release.await(); } catch (InterruptedException e) { throw new IllegalStateException(e); }
            throw new IllegalStateException("boom");
        }, clock);
        ExecutorService pool = Executors.newFixedThreadPool(2);
        Future<?> owner = pool.submit(() -> c.get("k"));
        entered.await();
        Future<?> waiter = pool.submit(() -> c.get("k"));
        Thread.sleep(100);                                    // let the waiter join the flight
        release.countDown();
        for (Future<?> f : List.of(owner, waiter))
            assertInstanceOf(CacheLoadException.class, assertThrows(java.util.concurrent.ExecutionException.class, () -> f.get(5, TimeUnit.SECONDS)).getCause());
        pool.shutdown();
    }

    @Test void invalidateDuringALoadDiscardsItsResult() throws Exception {
        CountDownLatch entered = new CountDownLatch(1), release = new CountDownLatch(1);
        AtomicInteger n = new AtomicInteger();
        var c = new TtlLruCache<String, String>(10, Duration.ofSeconds(10), Duration.ofSeconds(2), k -> {
            int call = n.incrementAndGet();
            if (call == 1) {
                entered.countDown();
                try { release.await(); } catch (InterruptedException e) { throw new IllegalStateException(e); }
            }
            return Optional.of("v" + call);
        }, clock);
        ExecutorService pool = Executors.newSingleThreadExecutor();
        Future<Optional<String>> stale = pool.submit(() -> c.get("k"));
        entered.await();
        c.invalidate("k");
        release.countDown();
        assertEquals(Optional.of("v1"), stale.get(5, TimeUnit.SECONDS));   // the caller that started it still gets its answer
        assertEquals(0, c.size());                                          // but it was not stored
        assertEquals(Optional.of("v2"), c.get("k"));                        // the next read loads fresh data
        pool.shutdown();
    }

    @Test void randomAccessMatchesALruModel() {
        Random r = new Random(9);
        int cap = 5;
        var c = new TtlLruCache<Integer, Integer>(cap, Duration.ofDays(1), Duration.ofDays(1), k -> Optional.of(k * 10), clock);
        LinkedHashMap<Integer, Integer> model = new LinkedHashMap<>(16, 0.75f, true);
        long expectedLoads = 0;
        for (int i = 0; i < 5_000; i++) {
            int k = r.nextInt(9);
            if (model.get(k) == null) {
                expectedLoads++;
                model.put(k, k);
                if (model.size() > cap) model.remove(model.keySet().iterator().next());
            }
            assertEquals(Optional.of(k * 10), c.get(k));
        }
        assertEquals(expectedLoads, c.stats().loads());
        assertEquals(model.size(), c.size());
    }

    @Test void manyOperationsAreFast() {
        var c = new TtlLruCache<Integer, Integer>(1_000, Duration.ofDays(1), Duration.ofDays(1), k -> Optional.of(k), clock);
        assertTimeoutPreemptively(Duration.ofSeconds(5), () -> {
            for (int i = 0; i < 1_000_000; i++) c.get(i % 3_000);
        });
        assertEquals(1_000, c.size());
    }
}
