package practice.d20261027.g;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InMemoryLockServiceTest {
    static final class TestClock extends Clock {
        private volatile Instant now = Instant.parse("2026-10-27T04:00:00Z");
        void advance(Duration d) { now = now.plus(d); }
        @Override public ZoneId getZone() { return ZoneId.of("UTC"); }
        @Override public Clock withZone(ZoneId z) { return this; }
        @Override public Instant instant() { return now; }
    }

    static final Duration TTL = Duration.ofSeconds(10);
    TestClock clock;
    LockService svc;

    @BeforeEach void setUp() {
        clock = new TestClock();
        svc = new InMemoryLockService(clock);
    }

    @Test void firstAcquireGetsTokenOneAndAnExpiry() {
        Lease l = svc.tryAcquire("job-1", "A", TTL).orElseThrow();
        assertEquals(1, l.fencingToken());
        assertEquals("A", l.owner());
        assertEquals(clock.instant().plus(TTL), l.expiresAt());
        assertEquals(Optional.of(l), svc.current("job-1"));
    }

    @Test void liveLeaseBlocksEveryoneIncludingTheOwner() {
        svc.tryAcquire("r", "A", TTL).orElseThrow();
        assertTrue(svc.tryAcquire("r", "B", TTL).isEmpty());
        assertTrue(svc.tryAcquire("r", "A", TTL).isEmpty());
        assertTrue(svc.tryAcquire("other", "B", TTL).isPresent());
    }

    @Test void expiryFreesTheResourceAndTheTokenGrows() {
        Lease a = svc.tryAcquire("r", "A", TTL).orElseThrow();
        clock.advance(TTL.minusMillis(1));
        assertTrue(svc.tryAcquire("r", "B", TTL).isEmpty());
        clock.advance(Duration.ofMillis(1));           // exactly at expiresAt the lease is dead
        assertTrue(svc.current("r").isEmpty());
        Lease b = svc.tryAcquire("r", "B", TTL).orElseThrow();
        assertEquals(a.fencingToken() + 1, b.fencingToken());
    }

    @Test void releaseFreesAndNextTokenIsHigher() {
        Lease a = svc.tryAcquire("r", "A", TTL).orElseThrow();
        assertTrue(svc.release(a));
        assertFalse(svc.release(a));                    // second release is a no-op
        Lease b = svc.tryAcquire("r", "B", TTL).orElseThrow();
        assertEquals(2, b.fencingToken());
    }

    @Test void staleHolderCannotReleaseOrRenewAfterBeingSuperseded() {
        Lease a = svc.tryAcquire("r", "A", TTL).orElseThrow();
        clock.advance(TTL.plusSeconds(1));
        Lease b = svc.tryAcquire("r", "B", TTL).orElseThrow();
        assertFalse(svc.release(a));
        assertTrue(svc.renew(a, TTL).isEmpty());
        assertEquals(Optional.of(b), svc.current("r"));
    }

    @Test void renewExtendsFromNowAndKeepsTheToken() {
        Lease a = svc.tryAcquire("r", "A", TTL).orElseThrow();
        clock.advance(Duration.ofSeconds(6));
        Lease again = svc.renew(a, TTL).orElseThrow();
        assertEquals(a.fencingToken(), again.fencingToken());
        assertEquals(clock.instant().plus(TTL), again.expiresAt());
        clock.advance(Duration.ofSeconds(6));          // past the original expiry, within the renewed one
        assertTrue(svc.tryAcquire("r", "B", TTL).isEmpty());
    }

    @Test void cannotRenewAnExpiredOrReleasedLease() {
        Lease a = svc.tryAcquire("r", "A", TTL).orElseThrow();
        clock.advance(TTL);
        assertTrue(svc.renew(a, TTL).isEmpty());
        Lease b = svc.tryAcquire("r", "B", TTL).orElseThrow();
        svc.release(b);
        assertTrue(svc.renew(b, TTL).isEmpty());
    }

    @Test void argumentValidation() {
        assertThrows(NullPointerException.class, () -> svc.tryAcquire(null, "A", TTL));
        assertThrows(NullPointerException.class, () -> svc.tryAcquire("r", null, TTL));
        assertThrows(NullPointerException.class, () -> svc.tryAcquire("r", "A", null));
        assertThrows(IllegalArgumentException.class, () -> svc.tryAcquire("r", "A", Duration.ZERO));
        assertThrows(IllegalArgumentException.class, () -> svc.tryAcquire("r", "A", Duration.ofSeconds(-1)));
        assertThrows(NullPointerException.class, () -> svc.release(null));
        assertTrue(svc.current("never-seen").isEmpty());
    }

    @Test void onlyOneOfManyConcurrentAcquirersWins() throws Exception {
        int n = 32;
        ExecutorService pool = Executors.newFixedThreadPool(n);
        CountDownLatch go = new CountDownLatch(1);
        AtomicInteger wins = new AtomicInteger();
        List<Future<?>> fs = new java.util.ArrayList<>();
        for (int i = 0; i < n; i++) {
            String owner = "w" + i;
            fs.add(pool.submit(() -> {
                go.await();
                if (svc.tryAcquire("hot", owner, TTL).isPresent()) wins.incrementAndGet();
                return null;
            }));
        }
        go.countDown();
        for (Future<?> f : fs) f.get(5, TimeUnit.SECONDS);
        pool.shutdownNow();
        assertEquals(1, wins.get());
    }

    @Test void tokensAreUniqueAndIncreasingUnderConcurrentChurn() throws Exception {
        int n = 8, rounds = 500;
        ExecutorService pool = Executors.newFixedThreadPool(n);
        ConcurrentLinkedQueue<Long> tokens = new ConcurrentLinkedQueue<>();
        List<Future<?>> fs = new java.util.ArrayList<>();
        for (int i = 0; i < n; i++) {
            String owner = "w" + i;
            fs.add(pool.submit(() -> {
                for (int r = 0; r < rounds; r++) {
                    svc.tryAcquire("hot", owner, TTL).ifPresent(l -> {
                        tokens.add(l.fencingToken());
                        svc.release(l);
                    });
                }
                return null;
            }));
        }
        for (Future<?> f : fs) f.get(10, TimeUnit.SECONDS);
        pool.shutdownNow();
        assertEquals(tokens.size(), tokens.stream().distinct().count());
        assertEquals((long) tokens.size(), tokens.stream().mapToLong(Long::longValue).max().orElse(0));
    }
}
