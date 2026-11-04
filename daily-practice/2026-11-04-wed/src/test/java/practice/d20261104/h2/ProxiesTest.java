package practice.d20261104.h2;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

class ProxiesTest {
    interface Catalog {
        @Memo String title(int id);
        @Memo String find(String name, Integer limit);
        @Memo String maybeNull(String k);
        String uncached(int id);
        String load(String path) throws IOException;
        @Memo String slow(String k) throws IOException;
        @Memo String flaky(String k);
        void ping();
    }

    static class Impl implements Catalog {
        final AtomicInteger calls = new AtomicInteger();
        final AtomicInteger failuresLeft = new AtomicInteger();
        public String title(int id) { calls.incrementAndGet(); return "T" + id; }
        public String find(String name, Integer limit) { calls.incrementAndGet(); return name + ":" + limit; }
        public String maybeNull(String k) { calls.incrementAndGet(); return null; }
        public String uncached(int id) { if (failuresLeft.getAndDecrement() > 0) throw new IllegalStateException("boom " + calls.get()); calls.incrementAndGet(); return "U" + id; }
        public String load(String path) throws IOException { calls.incrementAndGet(); throw new IOException("disk " + path); }
        public String slow(String k) throws IOException {
            calls.incrementAndGet();
            try { Thread.sleep(50); } catch (InterruptedException e) { throw new IOException(e); }
            return "S" + k;
        }
        public String flaky(String k) { if (failuresLeft.getAndDecrement() > 0) throw new IllegalStateException("flaky"); calls.incrementAndGet(); return "F" + k; }
        public void ping() { calls.incrementAndGet(); }
        @Override public String toString() { return "Impl!"; }
    }

    @Test void memoizingCachesPerMethodAndArguments() {
        Impl raw = new Impl();
        Catalog c = Proxies.memoizing(Catalog.class, raw);
        assertEquals("T1", c.title(1));
        assertEquals("T1", c.title(1));
        assertEquals("T2", c.title(2));
        assertEquals(2, raw.calls.get());
        assertEquals("a:null", c.find("a", null));
        assertEquals("a:null", c.find("a", null));
        assertEquals("a:3", c.find("a", 3));
        assertEquals(4, raw.calls.get());
    }

    @Test void nonAnnotatedMethodsAndObjectMethodsPassThrough() {
        Impl raw = new Impl();
        Catalog c = Proxies.memoizing(Catalog.class, raw);
        c.uncached(1); c.uncached(1); c.ping(); c.ping();
        assertEquals(4, raw.calls.get());
        assertEquals("Impl!", c.toString());
    }

    @Test void nullResultsAreCachedToo() {
        Impl raw = new Impl();
        Catalog c = Proxies.memoizing(Catalog.class, raw);
        assertNull(c.maybeNull("k"));
        assertNull(c.maybeNull("k"));
        assertEquals(1, raw.calls.get());
    }

    @Test void exceptionsAreUnwrappedAndNotCached() {
        Impl raw = new Impl();
        Catalog c = Proxies.memoizing(Catalog.class, raw);
        IOException e = assertThrows(IOException.class, () -> c.load("/x"));
        assertEquals("disk /x", e.getMessage());
        raw.failuresLeft.set(1);
        assertThrows(IllegalStateException.class, () -> c.flaky("a"));    // a failure is not cached ...
        assertEquals("Fa", c.flaky("a"));                                // ... so the next call runs the target again
        assertEquals("Fa", c.flaky("a"));                                // ... and that success is cached
        assertEquals(2, raw.calls.get());                                // load + one successful flaky
    }

    @Test void concurrentCallsRunTheTargetOnce() throws Exception {
        Impl raw = new Impl();
        Catalog c = Proxies.memoizing(Catalog.class, raw);
        CountDownLatch go = new CountDownLatch(1);
        List<Thread> ts = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            Thread t = new Thread(() -> { try { go.await(); assertEquals("Sx", c.slow("x")); } catch (Exception e) { throw new AssertionError(e); } });
            ts.add(t); t.start();
        }
        go.countDown();
        for (Thread t : ts) t.join();
        assertEquals(1, raw.calls.get());
    }

    @Test void retryingSucceedsAfterTransientFailures() {
        Impl raw = new Impl();
        raw.failuresLeft.set(2);
        Catalog c = Proxies.retrying(Catalog.class, raw, 3);
        assertEquals("U7", c.uncached(7));
        assertEquals(-1, raw.failuresLeft.get(), "three attempts were made: two failed, the third succeeded");
    }

    @Test void retryingRethrowsTheLastExceptionAfterMaxAttempts() {
        Impl raw = new Impl();
        raw.failuresLeft.set(10);
        Catalog c = Proxies.retrying(Catalog.class, raw, 3);
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> c.uncached(1));
        assertTrue(e.getMessage().startsWith("boom"));
        assertEquals(7, raw.failuresLeft.get(), "exactly 3 attempts were made");
    }

    @Test void checkedExceptionsAreNotRetriedAndKeepTheirType() {
        Impl raw = new Impl();
        Catalog c = Proxies.retrying(Catalog.class, raw, 5);
        assertThrows(IOException.class, () -> c.load("/a"));
        assertEquals(1, raw.calls.get());
    }

    @Test void singleAttemptMeansNoRetry() {
        Impl raw = new Impl();
        raw.failuresLeft.set(1);
        Catalog c = Proxies.retrying(Catalog.class, raw, 1);
        assertThrows(IllegalStateException.class, () -> c.uncached(1));
    }

    @Test void composeRetryInsideMemo() {
        Impl raw = new Impl();
        raw.failuresLeft.set(1);
        Catalog c = Proxies.memoizing(Catalog.class, Proxies.retrying(Catalog.class, raw, 2));
        assertEquals("T5", c.title(5));
        assertEquals("T5", c.title(5));
        assertEquals(1, raw.calls.get());
    }

    @Test void argumentChecks() {
        assertThrows(IllegalArgumentException.class, () -> Proxies.memoizing(Impl.class, new Impl()));
        assertThrows(IllegalArgumentException.class, () -> Proxies.retrying(Impl.class, new Impl(), 2));
        assertThrows(IllegalArgumentException.class, () -> Proxies.retrying(Catalog.class, new Impl(), 0));
    }

    @Test void cacheHitsAreFast() {
        Impl raw = new Impl();
        Catalog c = Proxies.memoizing(Catalog.class, raw);
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> {
            for (int i = 0; i < 500_000; i++) c.title(i % 100);
        });
        assertEquals(100, raw.calls.get());
    }
}
