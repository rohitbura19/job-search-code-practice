package practice.d20261030.g;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class InMemoryBrokerTest {
    MessageBroker b;

    @BeforeEach void setUp() { b = new InMemoryBroker(); }

    static List<String> values(List<Record> rs) { return rs.stream().map(Record::value).toList(); }

    @Test void createTopicValidation() {
        b.createTopic("t", 3);
        assertThrows(BrokerException.class, () -> b.createTopic("t", 3));
        assertThrows(BrokerException.class, () -> b.createTopic("z", 0));
        assertThrows(BrokerException.class, () -> b.createTopic(" ", 1));
        assertThrows(BrokerException.class, () -> b.publish("nope", "k", "v"));
        assertThrows(BrokerException.class, () -> b.publish("t", "k", null));
        assertThrows(BrokerException.class, () -> b.join("g", "c", "nope"));
        assertThrows(BrokerException.class, () -> b.lag("g", "nope"));
    }

    @Test void keyedPublishUsesHashPartitionAndPerPartitionOffsets() {
        b.createTopic("t", 4);
        int p = Math.floorMod("user-7".hashCode(), 4);
        for (int i = 0; i < 5; i++) {
            Record r = b.publish("t", "user-7", "m" + i);
            assertEquals(p, r.partition());
            assertEquals(i, r.offset());
            assertEquals("t", r.topic());
            assertEquals("user-7", r.key());
        }
        Record other = b.publish("t", "polygenelubricants", "x");   // its hashCode is Integer.MIN_VALUE: Math.abs would stay negative
        assertEquals(0, other.partition());
    }

    @Test void nullKeysRoundRobinFromZero() {
        b.createTopic("t", 3);
        List<Integer> parts = new ArrayList<>();
        for (int i = 0; i < 7; i++) parts.add(b.publish("t", null, "v" + i).partition());
        assertEquals(List.of(0, 1, 2, 0, 1, 2, 0), parts);
        b.createTopic("u", 2);
        assertEquals(0, b.publish("u", null, "x").partition());   // counter is per topic
    }

    @Test void singleConsumerPollIsRepeatableUntilCommit() {
        b.createTopic("t", 2);
        for (int i = 0; i < 6; i++) b.publish("t", null, "v" + i);       // p0: v0 v2 v4, p1: v1 v3 v5
        b.join("g", "c1", "t");
        assertEquals(List.of(0, 1), b.assignment("g", "c1", "t"));
        assertEquals(List.of("v0", "v2", "v4", "v1", "v3", "v5"), values(b.poll("g", "c1", "t", 10)));
        assertEquals(List.of("v0", "v2"), values(b.poll("g", "c1", "t", 2)));
        assertEquals(List.of("v0", "v2"), values(b.poll("g", "c1", "t", 2)));    // nothing was committed
        assertEquals(6, b.lag("g", "t"));
        b.commit("g", "c1", "t", 0, 2);
        assertEquals(List.of("v4", "v1"), values(b.poll("g", "c1", "t", 2)));
        assertEquals(4, b.lag("g", "t"));
        assertEquals(2, b.committed("g", "t", 0));
        assertEquals(0, b.committed("g", "t", 1));
        assertEquals(0, b.committed("other", "t", 0));
    }

    @Test void commitRules() {
        b.createTopic("t", 1);
        b.publish("t", null, "a");
        b.publish("t", null, "b");
        b.join("g", "c", "t");
        assertThrows(BrokerException.class, () -> b.commit("g", "c", "t", 0, 3));      // beyond the end
        b.commit("g", "c", "t", 0, 2);
        b.commit("g", "c", "t", 0, 2);                                                  // same value is fine
        assertThrows(BrokerException.class, () -> b.commit("g", "c", "t", 0, 1));      // backwards
        assertThrows(BrokerException.class, () -> b.commit("g", "c", "t", 5, 0));      // not owned / unknown
        assertThrows(BrokerException.class, () -> b.commit("g", "ghost", "t", 0, 2));
        assertThrows(BrokerException.class, () -> b.committed("g", "t", 1));
        assertThrows(BrokerException.class, () -> b.poll("g", "ghost", "t", 1));
        assertThrows(BrokerException.class, () -> b.poll("g", "c", "t", 0));
        assertEquals(List.of(), b.poll("g", "c", "t", 5));
    }

    @Test void assignmentIsSortedRoundRobinAndRebalances() {
        b.createTopic("t", 4);
        b.join("g", "b", "t");
        assertEquals(List.of(0, 1, 2, 3), b.assignment("g", "b", "t"));
        b.join("g", "a", "t");
        b.join("g", "a", "t");                                                          // idempotent
        assertEquals(List.of(0, 2), b.assignment("g", "a", "t"));
        assertEquals(List.of(1, 3), b.assignment("g", "b", "t"));
        b.join("g", "c", "t");
        assertEquals(List.of(0, 3), b.assignment("g", "a", "t"));
        assertEquals(List.of(1), b.assignment("g", "b", "t"));
        assertEquals(List.of(2), b.assignment("g", "c", "t"));
        b.leave("g", "a", "t");
        b.leave("g", "nobody", "t");
        assertEquals(List.of(0, 2), b.assignment("g", "b", "t"));
        assertEquals(List.of(1, 3), b.assignment("g", "c", "t"));
        assertThrows(BrokerException.class, () -> b.assignment("g", "a", "t"));
        b.createTopic("many", 2);
        b.join("g", "x", "many"); b.join("g", "y", "many"); b.join("g", "z", "many");
        assertEquals(List.of(), b.assignment("g", "z", "many"));                        // more consumers than partitions
    }

    @Test void rebalanceResumesFromGroupCommittedOffsets() {
        b.createTopic("t", 2);
        for (int i = 0; i < 6; i++) b.publish("t", null, "v" + i);
        b.join("g", "a", "t");
        b.join("g", "b", "t");
        assertEquals(List.of("v0", "v2", "v4"), values(b.poll("g", "a", "t", 10)));
        b.commit("g", "a", "t", 0, 2);                    // a processed v0 and v2, then crashed having polled but not committed v4
        b.leave("g", "a", "t");
        assertEquals(List.of("v4", "v1", "v3", "v5"), values(b.poll("g", "b", "t", 10)));
    }

    @Test void staleCommitAfterRebalanceIsRejected() {
        b.createTopic("t", 2);
        b.publish("t", null, "x");
        b.join("g", "a", "t");
        b.join("g", "b", "t");                            // b takes partition 1; a keeps 0
        assertThrows(BrokerException.class, () -> b.commit("g", "a", "t", 1, 0));
        b.commit("g", "b", "t", 1, 0);
        b.leave("g", "b", "t");
        b.commit("g", "a", "t", 1, 0);                    // a owns it again
    }

    @Test void groupsAreIndependent() {
        b.createTopic("t", 1);
        b.publish("t", null, "a");
        b.join("g1", "c", "t");
        b.join("g2", "c", "t");
        b.commit("g1", "c", "t", 0, 1);
        assertEquals(0, b.lag("g1", "t"));
        assertEquals(1, b.lag("g2", "t"));
        assertEquals(List.of("a"), values(b.poll("g2", "c", "t", 5)));
        b.createTopic("t2", 1);
        b.join("g1", "c", "t2");                          // same group name on another topic has its own state
        assertEquals(List.of(0), b.assignment("g1", "c", "t2"));
    }

    @Test @Timeout(10) void concurrentPublishersGetGaplessOffsetsPerPartition() throws Exception {
        b.createTopic("t", 4);
        int threads = 8, per = 2000;
        ExecutorService ex = Executors.newFixedThreadPool(threads);
        CountDownLatch go = new CountDownLatch(1);
        List<Future<?>> fs = new ArrayList<>();
        Set<String> seen = ConcurrentHashMap.newKeySet();
        for (int t = 0; t < threads; t++) {
            int id = t;
            fs.add(ex.submit(() -> {
                go.await();
                for (int i = 0; i < per; i++) {
                    Record r = b.publish("t", (i % 3 == 0) ? null : "k" + (i % 5), id + ":" + i);
                    assertTrue(seen.add(r.partition() + "/" + r.offset()), "duplicate slot " + r);
                }
                return null;
            }));
        }
        go.countDown();
        for (Future<?> f : fs) f.get();
        ex.shutdown();
        b.join("g", "c", "t");
        List<Record> all = b.poll("g", "c", "t", Integer.MAX_VALUE);
        assertEquals(threads * per, all.size());
        long[] next = new long[4];
        for (Record r : all) assertEquals(next[r.partition()]++, r.offset());     // gapless, in order
        assertEquals((long) threads * per, b.lag("g", "t"));
    }

    @Test @Timeout(10) void concurrentConsumersInOneGroupProcessEachRecordOnce() throws Exception {
        b.createTopic("t", 4);
        int total = 4000;
        for (int i = 0; i < total; i++) b.publish("t", "k" + i, "m" + i);
        List<String> ids = List.of("c1", "c2", "c3");
        for (String id : ids) b.join("g", id, "t");
        List<String> processed = Collections.synchronizedList(new ArrayList<>());
        ExecutorService ex = Executors.newFixedThreadPool(ids.size());
        List<Future<?>> fs = new ArrayList<>();
        for (String id : ids) {
            fs.add(ex.submit(() -> {
                while (true) {
                    List<Record> batch = b.poll("g", id, "t", 25);
                    if (batch.isEmpty()) return null;
                    batch.forEach(r -> processed.add(r.value()));
                    for (Record r : batch) b.commit("g", id, "t", r.partition(), r.offset() + 1);
                }
            }));
        }
        for (Future<?> f : fs) f.get();
        ex.shutdown();
        assertEquals(total, processed.size());
        assertEquals(total, new HashSet<>(processed).size());
        assertEquals(0, b.lag("g", "t"));
    }
}
