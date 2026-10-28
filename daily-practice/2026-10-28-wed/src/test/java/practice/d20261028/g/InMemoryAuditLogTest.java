package practice.d20261028.g;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InMemoryAuditLogTest {
    static final class TestClock extends Clock {
        private volatile Instant now = Instant.parse("2026-10-28T04:00:00Z");
        void advance(Duration d) { now = now.plus(d); }
        void set(Instant t) { now = t; }
        @Override public ZoneId getZone() { return ZoneId.of("UTC"); }
        @Override public Clock withZone(ZoneId z) { return this; }
        @Override public Instant instant() { return now; }
    }

    TestClock clock;
    AuditLog log;

    @BeforeEach void setUp() { clock = new TestClock(); log = new InMemoryAuditLog(clock); }

    @Test void chainStartsAtGenesisAndLinks() {
        AuditEvent a = log.append("asha", "LOGIN", "web");
        AuditEvent b = log.append("ben", "READ", "report-1");
        assertEquals(1, a.seq());
        assertEquals(AuditChain.GENESIS, a.prevHash());
        assertEquals(2, b.seq());
        assertEquals(a.hash(), b.prevHash());
        assertEquals(AuditChain.hash(2, "ben", "READ", "report-1", clock.instant(), a.hash()), b.hash());
        assertEquals(2, log.size());
        assertEquals(64, a.hash().length());
    }

    @Test void verifyAcceptsRealChainAndEmpty() {
        assertTrue(AuditChain.verify(List.of()));
        for (int i = 0; i < 20; i++) { log.append("u" + i % 3, "A", "r" + i % 4); clock.advance(Duration.ofSeconds(1)); }
        assertTrue(AuditChain.verify(log.query(AuditQuery.first(100))));
        List<AuditEvent> all = log.query(AuditQuery.first(100));
        assertTrue(AuditChain.verify(all.subList(5, 12)), "a middle segment chains internally");
    }

    @Test void verifyDetectsEveryKindOfTampering() {
        for (int i = 0; i < 6; i++) { log.append("u", "A" + i, "r"); clock.advance(Duration.ofSeconds(1)); }
        List<AuditEvent> all = new ArrayList<>(log.query(AuditQuery.first(100)));
        // 1 edit a field
        List<AuditEvent> edited = new ArrayList<>(all);
        AuditEvent e = all.get(2);
        edited.set(2, new AuditEvent(e.seq(), "mallory", e.action(), e.resource(), e.at(), e.prevHash(), e.hash()));
        assertFalse(AuditChain.verify(edited));
        // 2 edit and re-hash that single event: the next prevHash no longer matches
        AuditEvent forged = new AuditEvent(e.seq(), "mallory", e.action(), e.resource(), e.at(), e.prevHash(),
                AuditChain.hash(e.seq(), "mallory", e.action(), e.resource(), e.at(), e.prevHash()));
        edited.set(2, forged);
        assertFalse(AuditChain.verify(edited));
        // 3 delete a middle event
        List<AuditEvent> gap = new ArrayList<>(all);
        gap.remove(3);
        assertFalse(AuditChain.verify(gap));
        // 4 swap two events
        List<AuditEvent> swapped = new ArrayList<>(all);
        Collections.swap(swapped, 1, 2);
        assertFalse(AuditChain.verify(swapped));
        // 5 first event claiming seq 1 with a non-genesis prevHash
        AuditEvent f = all.get(0);
        assertFalse(AuditChain.verify(List.of(new AuditEvent(1, f.actor(), f.action(), f.resource(), f.at(), "abc",
                AuditChain.hash(1, f.actor(), f.action(), f.resource(), f.at(), "abc")))));
        // untouched list still verifies
        assertTrue(AuditChain.verify(all));
    }

    @Test void separatorInValuesCannotShiftFields() {
        AuditEvent a = log.append("a|b", "c", "d");
        AuditEvent b = new AuditEvent(1, "a", "b|c", "d", a.at(), AuditChain.GENESIS, a.hash());
        assertFalse(AuditChain.verify(List.of(b)));
    }

    @Test void timeNeverGoesBackwards() {
        AuditEvent a = log.append("u", "A", "r");
        clock.set(a.at().minusSeconds(60));
        AuditEvent b = log.append("u", "B", "r");
        assertEquals(a.at(), b.at());
        assertTrue(AuditChain.verify(List.of(a, b)));
    }

    @Test void rejectsBadInput() {
        assertThrows(IllegalArgumentException.class, () -> log.append(null, "A", "r"));
        assertThrows(IllegalArgumentException.class, () -> log.append("u", " ", "r"));
        assertThrows(IllegalArgumentException.class, () -> log.append("u", "A", ""));
        assertThrows(IllegalArgumentException.class, () -> log.query(AuditQuery.first(0)));
        assertEquals(0, log.size());
    }

    @Test void queryFiltersAndOrder() {
        log.append("asha", "LOGIN", "web");          // 1
        log.append("ben", "READ", "report-1");       // 2
        log.append("asha", "READ", "report-1");      // 3
        log.append("asha", "READ", "report-2");      // 4
        log.append("cy", "READ", "report-1");        // 5
        assertEquals(List.of(1L, 3L, 4L), seqs(log.query(AuditQuery.first(10).byActor("asha"))));
        assertEquals(List.of(2L, 3L, 5L), seqs(log.query(AuditQuery.first(10).onResource("report-1"))));
        assertEquals(List.of(3L), seqs(log.query(AuditQuery.first(10).byActor("asha").onResource("report-1"))));
        assertEquals(List.of(), seqs(log.query(AuditQuery.first(10).byActor("nobody"))));
        assertEquals(List.of(1L, 2L), seqs(log.query(AuditQuery.first(2))));
        assertEquals(List.of(4L, 5L), seqs(log.query(AuditQuery.first(10).after(3))));
        assertEquals(List.of(), seqs(log.query(AuditQuery.first(10).after(99))));
        assertThrows(UnsupportedOperationException.class, () -> log.query(AuditQuery.first(10)).add(null));
    }

    @Test void timeRangeIsHalfOpen() {
        Instant t0 = clock.instant();
        for (int i = 0; i < 5; i++) { log.append("u", "A", "r"); clock.advance(Duration.ofMinutes(1)); } // at t0 + 0..4 min
        assertEquals(List.of(2L, 3L), seqs(log.query(AuditQuery.first(10).between(t0.plus(Duration.ofMinutes(1)), t0.plus(Duration.ofMinutes(3))))));
        assertEquals(List.of(4L, 5L), seqs(log.query(AuditQuery.first(10).between(t0.plus(Duration.ofMinutes(3)), null))));
        assertEquals(List.of(1L), seqs(log.query(AuditQuery.first(10).between(null, t0.plus(Duration.ofMinutes(1))))));
        assertEquals(List.of(), seqs(log.query(AuditQuery.first(10).between(t0.plusSeconds(61), t0.plusSeconds(62)))));
    }

    @Test void keysetPagingWalksEverythingExactlyOnce() {
        for (int i = 0; i < 103; i++) { log.append("u" + i % 5, "A", "r"); clock.advance(Duration.ofSeconds(1)); }
        List<Long> got = new ArrayList<>();
        AuditQuery q = AuditQuery.first(10).byActor("u2");
        while (true) {
            List<AuditEvent> page = log.query(q);
            if (page.isEmpty()) break;
            page.forEach(e -> got.add(e.seq()));
            q = q.after(page.get(page.size() - 1).seq());
        }
        assertEquals(21, got.size());
        assertEquals(got.stream().sorted().toList(), got);
        assertEquals(new HashSet<>(got).size(), got.size());
    }

    @Test void concurrentAppendsKeepAValidChain() throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(8);
        List<Future<?>> fs = new ArrayList<>();
        for (int t = 0; t < 8; t++) {
            int id = t;
            fs.add(pool.submit(() -> { for (int i = 0; i < 1000; i++) log.append("t" + id, "A", "r" + i % 7); }));
        }
        for (Future<?> f : fs) f.get(20, TimeUnit.SECONDS);
        pool.shutdown();
        assertEquals(8000, log.size());
        List<AuditEvent> all = log.query(AuditQuery.first(8000));
        assertEquals(8000, all.size());
        assertEquals(8000L, all.get(7999).seq());
        assertTrue(AuditChain.verify(all));
    }

    @Test void rareActorQueriesDoNotScanTheLog() {
        int n = 200_000;
        for (int i = 0; i < n; i++) log.append("actor-" + (i % 2000), "A", "res-" + (i % 3000));
        log.append("rare", "A", "res-x");
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> {
            for (int i = 0; i < 5000; i++) {
                assertEquals(1, log.query(AuditQuery.first(5).byActor("rare")).size());
                assertEquals(5, log.query(AuditQuery.first(5).byActor("actor-" + i % 2000).after(150_000)).size());
                assertEquals(5, log.query(AuditQuery.first(5).after(n - 100)).size());
            }
        });
    }

    static List<Long> seqs(List<AuditEvent> es) { return es.stream().map(AuditEvent::seq).toList(); }
}
