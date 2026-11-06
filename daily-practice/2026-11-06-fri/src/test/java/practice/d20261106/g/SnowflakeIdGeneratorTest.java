package practice.d20261106.g;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.Test;

class SnowflakeIdGeneratorTest {
    private static final long EPOCH = 1_700_000_000_000L;

    @Test void decodesParts() {
        long[] now = {EPOCH + 5_000};
        SnowflakeIdGenerator g = new SnowflakeIdGenerator(37, EPOCH, () -> now[0]);
        long a = g.nextId(), b = g.nextId();
        assertEquals(now[0], SnowflakeIdGenerator.timestampOf(a, EPOCH));
        assertEquals(37, SnowflakeIdGenerator.workerOf(a));
        assertEquals(0, SnowflakeIdGenerator.sequenceOf(a));
        assertEquals(1, SnowflakeIdGenerator.sequenceOf(b));
        assertTrue(b > a);
        now[0]++;
        long c = g.nextId();
        assertEquals(0, SnowflakeIdGenerator.sequenceOf(c));
        assertTrue(c > b);
    }

    @Test void rejectsBadWorker() {
        assertThrows(IllegalArgumentException.class, () -> new SnowflakeIdGenerator(-1, EPOCH, () -> EPOCH));
        assertThrows(IllegalArgumentException.class, () -> new SnowflakeIdGenerator(1024, EPOCH, () -> EPOCH));
        assertDoesNotThrow(() -> new SnowflakeIdGenerator(1023, EPOCH, () -> EPOCH));
        assertDoesNotThrow(() -> new SnowflakeIdGenerator(0, EPOCH, () -> EPOCH));
    }

    @Test void fullSequenceWaitsForNextMillisecond() {
        AtomicLong calls = new AtomicLong();
        long base = EPOCH + 10;
        // clock is stuck at `base` for the first 4100 reads, then advances by 1 ms
        SnowflakeIdGenerator g = new SnowflakeIdGenerator(1, EPOCH, () -> calls.incrementAndGet() > 4100 ? base + 1 : base);
        long prev = -1;
        Set<Long> seen = new HashSet<>();
        for (int i = 0; i < 4096; i++) {
            long id = g.nextId();
            assertTrue(id > prev);
            assertEquals(i, SnowflakeIdGenerator.sequenceOf(id));
            assertTrue(seen.add(id));
            prev = id;
        }
        long next = assertTimeoutPreemptively(Duration.ofSeconds(2), g::nextId);
        assertEquals(base + 1, SnowflakeIdGenerator.timestampOf(next, EPOCH));
        assertEquals(0, SnowflakeIdGenerator.sequenceOf(next));
        assertTrue(next > prev);
    }

    @Test void clockGoingBackwardsThrowsAndRecovers() {
        long[] now = {EPOCH + 100};
        SnowflakeIdGenerator g = new SnowflakeIdGenerator(2, EPOCH, () -> now[0]);
        long a = g.nextId();
        now[0] = EPOCH + 99;
        assertThrows(IllegalStateException.class, g::nextId);
        now[0] = EPOCH + 100;
        long b = g.nextId();
        assertTrue(b > a, "same ms again: sequence continues, no duplicate");
        now[0] = EPOCH - 1;
        assertThrows(IllegalStateException.class, () -> new SnowflakeIdGenerator(2, EPOCH, () -> EPOCH - 1).nextId());
    }

    @Test void differentWorkersNeverCollide() {
        SnowflakeIdGenerator a = new SnowflakeIdGenerator(1, EPOCH, () -> EPOCH + 1), b = new SnowflakeIdGenerator(2, EPOCH, () -> EPOCH + 1);
        assertNotEquals(a.nextId(), b.nextId());
    }

    @Test void concurrentIdsAreUniqueAndPerThreadIncreasing() throws Exception {
        SnowflakeIdGenerator g = new SnowflakeIdGenerator(5, 0, System::currentTimeMillis);
        int threads = 8, per = 20_000;
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        List<Future<long[]>> fs = new ArrayList<>();
        for (int t = 0; t < threads; t++) fs.add(pool.submit(() -> {
            long[] ids = new long[per];
            for (int i = 0; i < per; i++) ids[i] = g.nextId();
            return ids;
        }));
        Set<Long> all = new HashSet<>();
        for (Future<long[]> f : fs) {
            long[] ids = f.get(20, TimeUnit.SECONDS);
            for (int i = 0; i < ids.length; i++) {
                if (i > 0) assertTrue(ids[i] > ids[i - 1]);
                assertTrue(all.add(ids[i]), "duplicate id");
            }
        }
        pool.shutdown();
        assertEquals(threads * per, all.size());
    }
}
