package practice.d20261111.h2;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

class ObjectPoolTest {
    final AtomicInteger made = new AtomicInteger(), resets = new AtomicInteger();

    ObjectPool<StringBuilder> pool(int cap) {
        return new ObjectPool<>(cap, () -> { made.incrementAndGet(); return new StringBuilder(); }, sb -> { resets.incrementAndGet(); sb.setLength(0); });
    }

    @Test void createsLazilyUpToCapacityThenEmpty() {
        ObjectPool<StringBuilder> p = pool(2);
        assertEquals(0, made.get());
        assertEquals(2, p.available());
        ObjectPool.Lease<StringBuilder> a = p.tryAcquire().orElseThrow();
        ObjectPool.Lease<StringBuilder> b = p.tryAcquire().orElseThrow();
        assertNotSame(a.get(), b.get());
        assertEquals(2, p.inUse());
        assertEquals(0, p.available());
        assertTrue(p.tryAcquire().isEmpty());
        assertEquals(2, made.get());
    }

    @Test void closeResetsAndRecycles() {
        ObjectPool<StringBuilder> p = pool(1);
        StringBuilder first;
        try (ObjectPool.Lease<StringBuilder> l = p.tryAcquire().orElseThrow()) {
            first = l.get();
            first.append("dirty");
        }
        assertEquals(1, resets.get());
        assertEquals(0, p.inUse());
        try (ObjectPool.Lease<StringBuilder> l = p.tryAcquire().orElseThrow()) {
            assertSame(first, l.get());
            assertEquals("", l.get().toString());
        }
        assertEquals(1, made.get());
    }

    @Test void closeIsIdempotentAndGetAfterCloseFails() {
        ObjectPool<StringBuilder> p = pool(1);
        ObjectPool.Lease<StringBuilder> l = p.tryAcquire().orElseThrow();
        l.close();
        l.close();
        assertEquals(1, resets.get());
        assertEquals(0, p.inUse());
        assertEquals(1, p.available());
        assertThrows(IllegalStateException.class, l::get);
        // a stale lease must not free someone else's object
        ObjectPool.Lease<StringBuilder> next = p.tryAcquire().orElseThrow();
        l.close();
        assertEquals(1, p.inUse());
        assertTrue(p.tryAcquire().isEmpty());
        next.close();
    }

    @Test void poolCloseDropsIdleAndRejectsNewButOutstandingLeaseStaysValid() {
        ObjectPool<StringBuilder> p = pool(2);
        ObjectPool.Lease<StringBuilder> out = p.tryAcquire().orElseThrow();
        p.tryAcquire().orElseThrow().close();                 // one idle object
        p.close();
        p.close();
        assertTrue(p.tryAcquire().isEmpty());
        assertEquals(0, p.available());
        out.get().append("still usable");
        int resetsBefore = resets.get();
        out.close();
        assertEquals(resetsBefore, resets.get(), "no reset after the pool is closed");
        assertEquals(0, p.inUse());
    }

    @Test void constructorValidation() {
        assertThrows(IllegalArgumentException.class, () -> new ObjectPool<>(0, StringBuilder::new, x -> {}));
        assertThrows(NullPointerException.class, () -> new ObjectPool<>(1, null, x -> {}));
        assertThrows(NullPointerException.class, () -> new ObjectPool<StringBuilder>(1, StringBuilder::new, null));
    }

    @Test void concurrentUseNeverExceedsCapacity() throws Exception {
        ObjectPool<AtomicInteger> p = new ObjectPool<>(3, AtomicInteger::new, x -> x.set(0));
        AtomicInteger maxSeen = new AtomicInteger(), granted = new AtomicInteger();
        ExecutorService ex = Executors.newFixedThreadPool(8);
        List<Future<?>> fs = new ArrayList<>();
        for (int t = 0; t < 8; t++) fs.add(ex.submit(() -> {
            for (int i = 0; i < 20_000; i++) {
                Optional<ObjectPool.Lease<AtomicInteger>> l = p.tryAcquire();
                if (l.isEmpty()) continue;
                try (ObjectPool.Lease<AtomicInteger> lease = l.get()) {
                    granted.incrementAndGet();
                    int now = lease.get().incrementAndGet();
                    if (now != 1) maxSeen.set(now);          // two threads held the same object
                    maxSeen.accumulateAndGet(0, Math::max);
                }
            }
        }));
        for (Future<?> f : fs) f.get(20, TimeUnit.SECONDS);
        ex.shutdown();
        assertTrue(granted.get() > 0);
        assertEquals(0, maxSeen.get(), "an object was leased twice at once");
        assertEquals(0, p.inUse());
    }
}
