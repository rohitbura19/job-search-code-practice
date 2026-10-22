package practice.d20261012.h2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class SingletonTest {

    /** Runs the task on `threads` threads released at the same moment and returns every result. */
    static <T> List<T> hammer(int threads, Callable<T> task) throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        List<T> out = new ArrayList<>();
        try {
            CountDownLatch go = new CountDownLatch(1);
            List<Future<T>> fs = new ArrayList<>();
            for (int i = 0; i < threads; i++) fs.add(pool.submit(() -> { go.await(); return task.call(); }));
            go.countDown();
            for (Future<T> f : fs) out.add(f.get(10, TimeUnit.SECONDS));
        } finally {
            pool.shutdownNow();
        }
        return out;
    }

    @Test @DisplayName("holder singleton: one instance, constructed once, even under 64 racing threads")
    void holder() throws Exception {
        List<HolderSingleton> all = hammer(64, HolderSingleton::getInstance);
        Set<HolderSingleton> distinct = new HashSet<>(all);
        assertEquals(1, distinct.size());
        assertSame(HolderSingleton.getInstance(), all.get(0));
        assertEquals(1, HolderSingleton.CREATED.get());
    }

    @Test @DisplayName("double-checked locking: one instance, constructed once, under 64 racing threads")
    void dcl() throws Exception {
        List<DclSingleton> all = hammer(64, DclSingleton::getInstance);
        assertEquals(1, new HashSet<>(all).size());
        assertSame(DclSingleton.getInstance(), all.get(0));
        assertEquals(1, DclSingleton.CREATED.get());
    }

    @Test @DisplayName("double-checked locking: the instance field must be volatile")
    void dclVolatile() {
        Field found = null;
        for (Field f : DclSingleton.class.getDeclaredFields())
            if (f.getType() == DclSingleton.class && Modifier.isStatic(f.getModifiers())) found = f;
        assertNotNull(found);
        assertTrue(Modifier.isVolatile(found.getModifiers()), "without volatile another thread can see a half-constructed object");
    }

    @Test @DisplayName("enum singleton: next() hands out 1..N exactly once each under 32 threads x 500 calls")
    void enumSequence() throws Exception {
        List<Long> all = new ArrayList<>();
        for (List<Long> part : hammer(32, () -> {
            List<Long> mine = new ArrayList<>();
            for (int i = 0; i < 500; i++) mine.add(EnumSingleton.INSTANCE.next());
            return mine;
        })) all.addAll(part);
        assertEquals(16_000, new HashSet<>(all).size());
        long max = 0, min = Long.MAX_VALUE;
        for (long v : all) { max = Math.max(max, v); min = Math.min(min, v); }
        assertEquals(1L, min);
        assertEquals(16_000L, max);
        assertSame(EnumSingleton.INSTANCE, EnumSingleton.valueOf("INSTANCE"));
    }
}
