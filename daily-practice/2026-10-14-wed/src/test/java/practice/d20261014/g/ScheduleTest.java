package practice.d20261014.g;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import static java.time.DayOfWeek.*;
import static org.junit.jupiter.api.Assertions.*;

class ScheduleTest {

    static final ZoneId NY = ZoneId.of("America/New_York");
    static final ZoneId IST = ZoneId.of("Asia/Kolkata");

    static Instant at(ZoneId z, String local) { return LocalDateTime.parse(local).atZone(z).toInstant(); }
    static Window w(Set<DayOfWeek> days, String start, String end) { return new Window(days, LocalTime.parse(start), LocalTime.parse(end)); }
    static Schedule sched(ZoneId z, Window... ws) { return new Schedule(1, z, List.of(ws)); }

    // ---------- plain weekly windows (no DST in Asia/Kolkata) ----------

    @Test @DisplayName("start is inclusive, end is exclusive")
    void boundaries() {
        Schedule s = sched(IST, w(EnumSet.of(WEDNESDAY), "09:00", "17:00"));     // Wed 14 Oct 2026
        assertFalse(s.isAllowed(at(IST, "2026-10-14T08:59:59")));
        assertTrue(s.isAllowed(at(IST, "2026-10-14T09:00:00")));
        assertTrue(s.isAllowed(at(IST, "2026-10-14T16:59:59")));
        assertFalse(s.isAllowed(at(IST, "2026-10-14T17:00:00")));
        assertFalse(s.isAllowed(at(IST, "2026-10-15T10:00:00")));               // Thursday: not a start day
    }

    @Test @DisplayName("midnight crossing, Mon+Tue 23:00-06:00: Tue 02:00 yes, Wed 02:00 yes, Thu 02:00 no")
    void midnight() {
        Schedule s = sched(IST, w(EnumSet.of(MONDAY, TUESDAY), "23:00", "06:00"));
        assertTrue(s.isAllowed(at(IST, "2026-10-12T23:30")));    // Mon night
        assertTrue(s.isAllowed(at(IST, "2026-10-13T02:00")));    // Tue 02:00 belongs to the Mon window
        assertTrue(s.isAllowed(at(IST, "2026-10-14T02:00")));    // Wed 02:00 belongs to the Tue window
        assertFalse(s.isAllowed(at(IST, "2026-10-15T02:00")));   // Thu 02:00: Wed is not a start day
        assertFalse(s.isAllowed(at(IST, "2026-10-12T02:00")));   // Mon 02:00: Sunday is not a start day
        assertFalse(s.isAllowed(at(IST, "2026-10-13T06:00")));   // end exclusive
        assertTrue(s.isAllowed(at(IST, "2026-10-13T05:59")));
        assertFalse(s.isAllowed(at(IST, "2026-10-13T12:00")));
    }

    @Test @DisplayName("several windows are a union; no windows means never allowed")
    void union() {
        Schedule s = sched(IST, w(EnumSet.of(FRIDAY), "01:00", "02:00"), w(EnumSet.of(FRIDAY), "01:30", "03:00"));
        assertTrue(s.isAllowed(at(IST, "2026-10-16T01:15")));
        assertTrue(s.isAllowed(at(IST, "2026-10-16T02:30")));
        assertFalse(s.isAllowed(at(IST, "2026-10-16T03:00")));
        Schedule none = new Schedule(1, IST, List.of());
        assertFalse(none.isAllowed(at(IST, "2026-10-16T01:15")));
        assertEquals(Optional.empty(), none.nextBoundary(at(IST, "2026-10-16T01:15")));
    }

    @Test @DisplayName("the schedule copies its input list and exposes its version")
    void immutable() {
        List<Window> ws = new ArrayList<>(List.of(w(EnumSet.of(WEDNESDAY), "09:00", "17:00")));
        Schedule s = new Schedule(7, IST, ws);
        ws.clear();
        assertEquals(7L, s.version());
        assertTrue(s.isAllowed(at(IST, "2026-10-14T10:00")));
    }

    @Test @DisplayName("an invalid Window is rejected (GIVEN record)")
    void windowValidation() {
        assertThrows(IllegalArgumentException.class, () -> w(EnumSet.of(MONDAY), "12:00", "12:00"));
        assertThrows(IllegalArgumentException.class, () -> w(EnumSet.noneOf(DayOfWeek.class), "01:00", "02:00"));
    }

    // ---------- nextBoundary ----------

    @Test @DisplayName("nextBoundary is strictly after now")
    void nextBoundary() {
        Schedule s = sched(IST, w(EnumSet.of(MONDAY, TUESDAY), "23:00", "06:00"));
        assertEquals(at(IST, "2026-10-12T23:00").toString(), s.nextBoundary(at(IST, "2026-10-12T12:00")).get().toString());
        assertEquals(at(IST, "2026-10-13T06:00").toString(), s.nextBoundary(at(IST, "2026-10-12T23:00")).get().toString());
        assertEquals(at(IST, "2026-10-13T23:00").toString(), s.nextBoundary(at(IST, "2026-10-13T06:00")).get().toString());
        // after Wed 06:00 the next start is next Monday 23:00 (5 days ahead)
        assertEquals(at(IST, "2026-10-19T23:00").toString(), s.nextBoundary(at(IST, "2026-10-14T06:00")).get().toString());
    }

    @Test @DisplayName("isAllowed does not change between now and the next boundary (random schedules, 1 minute resolution)")
    void constantUntilBoundary() {
        Random rnd = new Random(14);
        for (int t = 0; t < 300; t++) {
            Schedule s = randomSchedule(rnd, IST, 0, 24);
            for (int q = 0; q < 10; q++) {
                Instant now = Instant.parse("2026-10-01T00:00:00Z").plusSeconds(60L * rnd.nextInt(60 * 24 * 40));
                Instant next = s.nextBoundary(now).orElseThrow();
                assertTrue(next.isAfter(now));
                boolean v = s.isAllowed(now);
                for (Instant p = now; p.isBefore(next); p = p.plusSeconds(60 * 29 + 17))
                    assertEquals(v, s.isAllowed(p), "changed before the boundary at " + p);
                assertEquals(v, s.isAllowed(next.minusSeconds(1)));
            }
        }
    }

    // ---------- DST, America/New_York ----------

    @Test @DisplayName("spring forward (Sun 8 Mar 2026): window 01:30-03:30 local is ONE real hour")
    void springForward() {
        Schedule s = sched(NY, w(EnumSet.of(SUNDAY), "01:30", "03:30"));
        assertFalse(s.isAllowed(Instant.parse("2026-03-08T06:29:00Z")));
        assertTrue(s.isAllowed(Instant.parse("2026-03-08T06:30:00Z")));
        assertTrue(s.isAllowed(Instant.parse("2026-03-08T07:29:00Z")));
        assertFalse(s.isAllowed(Instant.parse("2026-03-08T07:30:00Z")));
    }

    @Test @DisplayName("spring forward: a start inside the gap (02:30) moves forward to 03:30 EDT = 07:30Z")
    void gapStart() {
        Schedule s = sched(NY, w(EnumSet.of(SUNDAY), "02:30", "04:00"));
        assertEquals("2026-03-08T07:30:00Z", s.nextBoundary(Instant.parse("2026-03-08T06:00:00Z")).get().toString());
        assertFalse(s.isAllowed(Instant.parse("2026-03-08T07:29:00Z")));
        assertTrue(s.isAllowed(Instant.parse("2026-03-08T07:30:00Z")));
    }

    static int allowedMinutes(Schedule s, Instant from, Instant to) {
        int n = 0;
        for (Instant t = from; t.isBefore(to); t = t.plusSeconds(60)) if (s.isAllowed(t)) n++;
        return n;
    }

    @Test @DisplayName("fall back (Sun 1 Nov 2026): window 00:30-02:30 local lasts THREE real hours")
    void fallBack() {
        Schedule s = sched(NY, w(EnumSet.of(SUNDAY), "00:30", "02:30"));
        assertEquals(180, allowedMinutes(s, Instant.parse("2026-11-01T03:00:00Z"), Instant.parse("2026-11-01T10:00:00Z")));
    }

    @Test @DisplayName("Australia/Lord_Howe (30-minute DST shift, Sun 4 Oct 2026): window 01:45-03:00 is 45 real minutes")
    void lordHowe() {
        ZoneId lh = ZoneId.of("Australia/Lord_Howe");
        Schedule s = sched(lh, w(EnumSet.of(SUNDAY), "01:45", "03:00"));
        assertEquals(45, allowedMinutes(s, Instant.parse("2026-10-03T14:00:00Z"), Instant.parse("2026-10-03T20:00:00Z")));
    }

    @Test @DisplayName("a 22:00-06:00 window crossing the spring-forward night: 8 wall-clock hours, 7 real hours")
    void nightAcrossDst() {
        Schedule s = sched(NY, w(EnumSet.of(SATURDAY), "22:00", "06:00"));
        assertEquals(7 * 60, allowedMinutes(s, Instant.parse("2026-03-08T00:00:00Z"), Instant.parse("2026-03-08T14:00:00Z")));
    }

    // ---------- oracle over a whole year ----------

    static Schedule randomSchedule(Random rnd, ZoneId z, int minHour, int maxHour) {
        List<Window> ws = new ArrayList<>();
        for (int i = 0, n = 1 + rnd.nextInt(3); i < n; i++) {
            Set<DayOfWeek> days = EnumSet.noneOf(DayOfWeek.class);
            for (DayOfWeek d : DayOfWeek.values()) if (rnd.nextInt(3) == 0) days.add(d);
            if (days.isEmpty()) days.add(DayOfWeek.of(1 + rnd.nextInt(7)));
            LocalTime a = LocalTime.of(minHour + rnd.nextInt(maxHour - minHour), rnd.nextInt(60));
            LocalTime b = LocalTime.of(minHour + rnd.nextInt(maxHour - minHour), rnd.nextInt(60));
            if (a.equals(b)) b = b.plusMinutes(1).isBefore(LocalTime.of(maxHour - 1, 59)) ? b.plusMinutes(1) : b.minusMinutes(1);
            ws.add(new Window(days, a, b));
        }
        return new Schedule(1, z, ws);
    }

    /** Wall-clock oracle: valid when no window boundary falls inside a DST gap or overlap. */
    static boolean oracle(List<Window> ws, ZoneId z, Instant t) {
        LocalDateTime l = t.atZone(z).toLocalDateTime();
        for (Window w : ws) {
            LocalTime now = l.toLocalTime();
            if (!w.crossesMidnight()) {
                if (w.days().contains(l.getDayOfWeek()) && !now.isBefore(w.start()) && now.isBefore(w.end())) return true;
            } else {
                if (w.days().contains(l.getDayOfWeek()) && !now.isBefore(w.start())) return true;
                if (w.days().contains(l.getDayOfWeek().minus(1)) && now.isBefore(w.end())) return true;
            }
        }
        return false;
    }

    @Test @DisplayName("every minute of 2026 in New York against a wall-clock oracle (boundaries kept clear of the DST hour)")
    void yearOracleNewYork() {
        Random rnd = new Random(14);
        for (int round = 0; round < 3; round++) {
            List<Window> ws = windowsOf(rnd, 4, 24);                    // boundaries between 04:00 and 23:59
            Schedule s = new Schedule(1, NY, ws);
            assertTimeoutPreemptively(Duration.ofSeconds(30), () -> {
                for (Instant t = Instant.parse("2026-01-01T05:00:00Z"); t.isBefore(Instant.parse("2027-01-01T05:00:00Z")); t = t.plusSeconds(60))
                    assertEquals(oracle(ws, NY, t), s.isAllowed(t), "differs at " + t);
            });
        }
    }

    static List<Window> windowsOf(Random rnd, int minHour, int maxHour) {
        List<Window> ws = new ArrayList<>();
        for (int i = 0, n = 1 + rnd.nextInt(3); i < n; i++) {
            Set<DayOfWeek> days = EnumSet.noneOf(DayOfWeek.class);
            for (DayOfWeek d : DayOfWeek.values()) if (rnd.nextInt(3) == 0) days.add(d);
            if (days.isEmpty()) days.add(DayOfWeek.of(1 + rnd.nextInt(7)));
            int h1 = minHour + rnd.nextInt(maxHour - minHour), h2 = minHour + rnd.nextInt(maxHour - minHour);
            LocalTime a = LocalTime.of(h1, rnd.nextInt(60)), b = LocalTime.of(h2, rnd.nextInt(60));
            if (a.equals(b)) b = b.plusMinutes(1);
            ws.add(new Window(days, a, b));
        }
        return ws;
    }

    @Test @DisplayName("every minute of 2026 in Kolkata against the oracle, any boundaries including midnight")
    void yearOracleKolkata() {
        Random rnd = new Random(15);
        for (int round = 0; round < 3; round++) {
            List<Window> ws = windowsOf(rnd, 0, 24);
            Schedule s = new Schedule(1, IST, ws);
            assertTimeoutPreemptively(Duration.ofSeconds(30), () -> {
                for (Instant t = Instant.parse("2026-01-01T00:00:00Z"); t.isBefore(Instant.parse("2027-01-01T00:00:00Z")); t = t.plusSeconds(60))
                    assertEquals(oracle(ws, IST, t), s.isAllowed(t), "differs at " + t);
            });
        }
    }

    // ---------- constraints, holder ----------

    @Test @DisplayName("AllOf: every part must allow; empty allows; a Schedule is itself a constraint")
    void allOf() {
        Instant t = Instant.parse("2026-10-14T10:00:00Z");
        ChargingConstraint yes = x -> true, no = x -> false;
        assertTrue(new AllOf(List.of(yes, yes)).allows(t));
        assertFalse(new AllOf(List.of(yes, no, yes)).allows(t));
        assertTrue(new AllOf(List.of()).allows(t));
        Schedule s = sched(IST, w(EnumSet.of(WEDNESDAY), "09:00", "17:00"));         // 10:00Z = 15:30 IST: inside
        assertTrue(new AllOf(List.of(s, yes)).allows(t));
        assertFalse(new AllOf(List.of(s, no)).allows(t));
        assertFalse(new AllOf(List.of(s)).allows(Instant.parse("2026-10-14T13:00:00Z")));   // 18:30 IST: outside
    }

    @Test @DisplayName("holder: only a strictly newer version replaces the current one")
    void holder() {
        ScheduleHolder h = new ScheduleHolder();
        assertNull(h.current());
        Schedule v2 = new Schedule(2, IST, List.of()), v1 = new Schedule(1, IST, List.of()), v3 = new Schedule(3, IST, List.of());
        assertTrue(h.offer(v2));
        assertSame(v2, h.current());
        assertFalse(h.offer(v1));
        assertFalse(h.offer(new Schedule(2, IST, List.of())));
        assertSame(v2, h.current());
        assertTrue(h.offer(v3));
        assertSame(v3, h.current());
    }

    @Test @DisplayName("holder: concurrent deliveries in random order always end on the highest version")
    void holderConcurrent() throws Exception {
        ScheduleHolder h = new ScheduleHolder();
        List<Schedule> all = new ArrayList<>();
        for (int v = 1; v <= 2000; v++) all.add(new Schedule(v, IST, List.of()));
        java.util.Collections.shuffle(all, new Random(14));
        Thread[] ts = new Thread[8];
        for (int i = 0; i < ts.length; i++) {
            final int from = i * 250;
            ts[i] = new Thread(() -> { for (int k = from; k < from + 250; k++) h.offer(all.get(k)); });
            ts[i].start();
        }
        for (Thread t : ts) t.join();
        assertEquals(2000L, h.current().version());
    }
}
