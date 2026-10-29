package practice.d20261029.g;

import static org.junit.jupiter.api.Assertions.*;

import java.time.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import practice.d20261029.g.HoldException.Reason;

class InMemorySeatHoldServiceTest {
    static final class MutableClock extends Clock {
        volatile Instant now = Instant.parse("2026-10-29T04:00:00Z");
        @Override public ZoneId getZone() { return ZoneOffset.UTC; }
        @Override public Clock withZone(ZoneId z) { return this; }
        @Override public Instant instant() { return now; }
        void advance(Duration d) { now = now.plus(d); }
    }

    MutableClock clock;
    SeatHoldService svc;

    @BeforeEach void setUp() {
        clock = new MutableClock();
        svc = new InMemorySeatHoldService(clock, new HoldConfig(Duration.ofMinutes(5), 4),
                Map.of("e1", Set.of("A1", "A2", "A3", "A4", "A5", "A6"), "e2", Set.of("A1")));
    }

    static void assertReason(Reason r, Runnable action) {
        HoldException e = assertThrows(HoldException.class, action::run);
        assertEquals(r, e.reason());
    }

    @Test void holdMarksSeatsHeldAndCounts() {
        Hold h = svc.hold("e1", Set.of("A1", "A2"), "u1");
        assertEquals("H1", h.holdId());
        assertEquals(clock.now.plus(Duration.ofMinutes(5)), h.expiresAt());
        assertEquals(SeatState.HELD, svc.state("e1", "A1"));
        assertEquals(SeatState.AVAILABLE, svc.state("e1", "A3"));
        assertEquals(4, svc.availableCount("e1"));
        assertEquals(1, svc.availableCount("e2"));
    }

    @Test void holdIsAllOrNothing() {
        svc.hold("e1", Set.of("A1"), "u1");
        assertReason(Reason.SEAT_UNAVAILABLE, () -> svc.hold("e1", Set.of("A2", "A1"), "u2"));
        assertEquals(SeatState.AVAILABLE, svc.state("e1", "A2"));
        assertReason(Reason.UNKNOWN_SEAT, () -> svc.hold("e1", Set.of("A2", "Z9"), "u2"));
        assertEquals(SeatState.AVAILABLE, svc.state("e1", "A2"));
        assertEquals(5, svc.availableCount("e1"));
    }

    @Test void validation() {
        assertThrows(IllegalArgumentException.class, () -> svc.hold("e1", Set.of(), "u1"));
        assertReason(Reason.TOO_MANY_SEATS, () -> svc.hold("e1", Set.of("A1", "A2", "A3", "A4", "A5"), "u1"));
        assertReason(Reason.UNKNOWN_EVENT, () -> svc.hold("nope", Set.of("A1"), "u1"));
        assertReason(Reason.UNKNOWN_EVENT, () -> svc.availableCount("nope"));
        assertReason(Reason.UNKNOWN_SEAT, () -> svc.state("e1", "Z9"));
    }

    @Test void holdExpiresExactlyAtTtl() {
        svc.hold("e1", Set.of("A1"), "u1");
        clock.advance(Duration.ofMinutes(5).minusMillis(1));
        assertEquals(SeatState.HELD, svc.state("e1", "A1"));
        assertReason(Reason.SEAT_UNAVAILABLE, () -> svc.hold("e1", Set.of("A1"), "u2"));
        clock.advance(Duration.ofMillis(1));
        assertEquals(SeatState.AVAILABLE, svc.state("e1", "A1"));
        assertEquals(6, svc.availableCount("e1"));
        Hold h2 = svc.hold("e1", Set.of("A1"), "u2");
        assertEquals("H2", h2.holdId());
    }

    @Test void confirmBooksSeatsAndIsIdempotent() {
        Hold h = svc.hold("e1", Set.of("A1", "A2"), "u1");
        Booking b = svc.confirm(h.holdId(), "u1");
        assertEquals("B1", b.bookingId());
        assertEquals(Set.of("A1", "A2"), b.seats());
        assertEquals(SeatState.BOOKED, svc.state("e1", "A1"));
        assertSame(b, svc.confirm(h.holdId(), "u1"));
        assertReason(Reason.NOT_OWNER, () -> svc.confirm(h.holdId(), "u2"));
        clock.advance(Duration.ofHours(1));
        assertEquals(SeatState.BOOKED, svc.state("e1", "A1"));       // booked seats never expire
        assertSame(b, svc.confirm(h.holdId(), "u1"));
        assertEquals(4, svc.availableCount("e1"));
    }

    @Test void confirmFailures() {
        Hold h = svc.hold("e1", Set.of("A1"), "u1");
        assertReason(Reason.NOT_OWNER, () -> svc.confirm(h.holdId(), "u2"));
        assertReason(Reason.NOT_FOUND, () -> svc.confirm("H99", "u1"));
        clock.advance(Duration.ofMinutes(5));
        assertReason(Reason.EXPIRED, () -> svc.confirm(h.holdId(), "u1"));
        assertEquals(SeatState.AVAILABLE, svc.state("e1", "A1"));
    }

    @Test void expiredHoldCannotBeConfirmedEvenIfNobodyElseTookTheSeat() {
        Hold h = svc.hold("e1", Set.of("A1"), "u1");
        clock.advance(Duration.ofMinutes(6));
        assertReason(Reason.EXPIRED, () -> svc.confirm(h.holdId(), "u1"));
        Hold h2 = svc.hold("e1", Set.of("A1"), "u2");
        assertReason(Reason.EXPIRED, () -> svc.confirm(h.holdId(), "u1"));
        assertEquals("u2", svc.confirm(h2.holdId(), "u2").userId());
    }

    @Test void releaseFreesSeatsAndIsForgiving() {
        Hold h = svc.hold("e1", Set.of("A1", "A2"), "u1");
        assertReason(Reason.NOT_OWNER, () -> svc.release(h.holdId(), "u2"));
        svc.release(h.holdId(), "u1");
        assertEquals(6, svc.availableCount("e1"));
        svc.release(h.holdId(), "u1");
        svc.release("H99", "u1");
        Hold h2 = svc.hold("e1", Set.of("A1"), "u2");
        svc.confirm(h2.holdId(), "u2");
        svc.release(h2.holdId(), "u2");                                // confirmed: no-op
        assertEquals(SeatState.BOOKED, svc.state("e1", "A1"));
        assertReason(Reason.NOT_FOUND, () -> svc.confirm(h.holdId(), "u1"));
    }

    @Test void manyHoldsExpireInOrderOfTheirDeadline() {
        svc.hold("e1", Set.of("A1"), "u1");
        clock.advance(Duration.ofMinutes(2));
        svc.hold("e1", Set.of("A2"), "u2");
        clock.advance(Duration.ofMinutes(3));          // first expired, second has 2 minutes left
        assertEquals(SeatState.AVAILABLE, svc.state("e1", "A1"));
        assertEquals(SeatState.HELD, svc.state("e1", "A2"));
        assertEquals(5, svc.availableCount("e1"));
    }

    @Test void fiftyThreadsFightForOneSeatExactlyOneWins() throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(50);
        CountDownLatch go = new CountDownLatch(1);
        AtomicInteger wins = new AtomicInteger(), losses = new AtomicInteger();
        List<Future<?>> fs = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            String user = "u" + i;
            fs.add(pool.submit(() -> {
                go.await();
                try { svc.hold("e2", Set.of("A1"), user); wins.incrementAndGet(); }
                catch (HoldException e) { assertEquals(Reason.SEAT_UNAVAILABLE, e.reason()); losses.incrementAndGet(); }
                return null;
            }));
        }
        go.countDown();
        for (Future<?> f : fs) f.get(5, TimeUnit.SECONDS);
        pool.shutdown();
        assertEquals(1, wins.get());
        assertEquals(49, losses.get());
        assertEquals(0, svc.availableCount("e2"));
    }

    @Test void overlappingHoldsNeverDoubleBookUnderContention() throws Exception {
        Set<String> seats = new HashSet<>();
        for (int i = 0; i < 40; i++) seats.add("S" + i);
        svc = new InMemorySeatHoldService(clock, new HoldConfig(Duration.ofMinutes(5), 4), Map.of("big", seats));
        ExecutorService pool = Executors.newFixedThreadPool(16);
        List<Future<List<Hold>>> fs = new ArrayList<>();
        for (int t = 0; t < 16; t++) {
            int seed = t;
            fs.add(pool.submit(() -> {
                Random rnd = new Random(seed);
                List<Hold> mine = new ArrayList<>();
                for (int i = 0; i < 200; i++) {
                    Set<String> pick = new HashSet<>();
                    for (int k = 1 + rnd.nextInt(3); k > 0; k--) pick.add("S" + rnd.nextInt(40));
                    try { mine.add(svc.hold("big", pick, "u" + seed)); } catch (HoldException ignored) {}
                }
                return mine;
            }));
        }
        Set<String> seen = new HashSet<>();
        int total = 0;
        for (Future<List<Hold>> f : fs) for (Hold h : f.get(10, TimeUnit.SECONDS)) for (String s : h.seats()) { assertTrue(seen.add(s), "seat held twice: " + s); total++; }
        pool.shutdown();
        assertEquals(40 - total, svc.availableCount("big"));
    }
}
