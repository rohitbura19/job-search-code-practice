package practice.d20261110.g;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import org.junit.jupiter.api.Test;

class CalendarServiceTest {
    static final Instant T0 = Instant.parse("2026-11-10T09:00:00Z");
    static Instant at(int minute) { return T0.plus(Duration.ofMinutes(minute)); }
    static TimeRange r(int from, int to) { return new TimeRange(at(from), at(to)); }

    CalendarService cal = new CalendarService();

    @Test void scheduleAssignsSequentialIdsAndIncludesOrganizer() {
        Event e = cal.schedule("ann", Set.of("bob"), r(0, 60), "standup");
        assertEquals("e1", e.id());
        assertEquals(Set.of("ann", "bob"), e.participants());
        assertEquals("ann", e.organizer());
        assertEquals("e2", cal.schedule("cy", Set.of(), r(0, 60), "solo").id());
    }

    @Test void overlapIsHalfOpenAndDetectedForAnyParticipant() {
        cal.schedule("ann", Set.of("bob"), r(60, 120), "a");
        cal.schedule("bob", Set.of(), r(0, 60), "touching before is fine");
        cal.schedule("bob", Set.of(), r(120, 180), "touching after is fine");
        ConflictException ex = assertThrows(ConflictException.class, () -> cal.schedule("zed", Set.of("bob"), r(90, 100), "clash"));
        assertEquals("bob", ex.user());
        assertEquals("e1", ex.conflictingEventId());
        assertThrows(ConflictException.class, () -> cal.schedule("ann", Set.of(), r(0, 61), "overlaps start"));
        assertThrows(ConflictException.class, () -> cal.schedule("ann", Set.of(), r(119, 130), "overlaps end"));
        assertThrows(ConflictException.class, () -> cal.schedule("ann", Set.of(), r(30, 200), "contains"));
        assertThrows(ConflictException.class, () -> cal.schedule("ann", Set.of(), r(60, 120), "identical"));
    }

    @Test void failedScheduleIsAtomicAndConsumesNoId() {
        cal.schedule("bob", Set.of(), r(0, 60), "busy");
        assertThrows(ConflictException.class, () -> cal.schedule("ann", Set.of("bob", "cy"), r(30, 90), "x"));
        assertEquals(List.of(), cal.agenda("ann", r(0, 600)));
        assertEquals(List.of(), cal.agenda("cy", r(0, 600)));
        assertEquals("e2", cal.schedule("ann", Set.of("cy"), r(30, 90), "y").id());
    }

    @Test void conflictReportsFirstBusyParticipantAlphabetically() {
        cal.schedule("zoe", Set.of(), r(0, 60), "z");
        cal.schedule("bob", Set.of(), r(0, 60), "b");
        assertEquals("bob", assertThrows(ConflictException.class, () -> cal.schedule("amy", Set.of("zoe", "bob"), r(10, 20), "t")).user());
    }

    @Test void invalidRangeRejectedByTimeRange() {
        assertThrows(IllegalArgumentException.class, () -> r(10, 10));
        assertThrows(IllegalArgumentException.class, () -> r(20, 10));
    }

    @Test void cancel() {
        Event e = cal.schedule("ann", Set.of("bob"), r(0, 60), "a");
        assertTrue(cal.cancel(e.id()));
        assertFalse(cal.cancel(e.id()));
        assertFalse(cal.cancel("nope"));
        assertEquals(List.of(), cal.agenda("bob", r(0, 600)));
        assertEquals("e2", cal.schedule("bob", Set.of("ann"), r(0, 60), "again").id());
    }

    @Test void agendaIncludesEventsStartedBeforeTheWindow() {
        cal.schedule("ann", Set.of(), r(0, 60), "early");
        cal.schedule("ann", Set.of(), r(60, 90), "mid");
        cal.schedule("ann", Set.of(), r(200, 260), "late");
        assertEquals(List.of("early", "mid"), titles(cal.agenda("ann", r(30, 100))));
        assertEquals(List.of("mid"), titles(cal.agenda("ann", r(60, 100))));         // window starts exactly when "early" ends
        assertEquals(List.of("late"), titles(cal.agenda("ann", r(259, 300))));
        assertEquals(List.of(), cal.agenda("ann", r(90, 200)));
        assertEquals(List.of(), cal.agenda("nobody", r(0, 600)));
    }

    @Test void reschedule() {
        Event e = cal.schedule("ann", Set.of("bob"), r(0, 60), "a");
        cal.schedule("bob", Set.of(), r(100, 160), "b");
        Event moved = cal.reschedule(e.id(), r(30, 90));                          // overlaps only itself
        assertEquals(r(30, 90), moved.range());
        assertEquals(List.of("a", "b"), titles(cal.agenda("bob", r(0, 600))));
        assertThrows(ConflictException.class, () -> cal.reschedule(e.id(), r(120, 150)));
        assertEquals(r(30, 90), cal.agenda("ann", r(0, 600)).get(0).range());      // old slot kept
        assertThrows(NoSuchElementException.class, () -> cal.reschedule("e99", r(0, 10)));
        cal.schedule("cy", Set.of("ann"), r(0, 30), "fits in the vacated minutes");
    }

    @Test void freeSlots() {
        cal.schedule("ann", Set.of(), r(60, 120), "a");
        cal.schedule("bob", Set.of(), r(100, 150), "b");
        cal.schedule("bob", Set.of(), r(240, 300), "c");
        Set<String> both = Set.of("ann", "bob");
        assertEquals(List.of(r(0, 60), r(150, 240), r(300, 480)), cal.freeSlots(both, r(0, 480), Duration.ofMinutes(30)));
        assertEquals(List.of(r(150, 240), r(300, 480)), cal.freeSlots(both, r(0, 480), Duration.ofMinutes(61)));
        assertEquals(List.of(r(10, 60)), cal.freeSlots(both, r(10, 80), Duration.ofMinutes(10)));      // clipped to the window
        assertEquals(List.of(), cal.freeSlots(both, r(70, 140), Duration.ofMinutes(1)));
        assertEquals(List.of(r(0, 480)), cal.freeSlots(Set.of("nobody"), r(0, 480), Duration.ofMinutes(480)));
        assertEquals(List.of(r(0, 60), r(120, 480)), cal.freeSlots(Set.of("ann"), r(0, 480), Duration.ZERO));
    }

    @Test void firstFree() {
        cal.schedule("ann", Set.of(), r(0, 60), "a");
        cal.schedule("bob", Set.of(), r(30, 120), "b");
        assertEquals(Optional.of(at(120)), cal.firstFree(Set.of("ann", "bob"), at(0), Duration.ofMinutes(45), Duration.ofHours(8)));
        assertEquals(Optional.empty(), cal.firstFree(Set.of("ann", "bob"), at(0), Duration.ofMinutes(45), Duration.ofMinutes(150)));
        assertEquals(Optional.of(at(10)), cal.firstFree(Set.of("zed"), at(10), Duration.ofMinutes(5), Duration.ofHours(1)));
    }

    @Test void randomisedAgainstMinuteBitmap() {
        Random rnd = new Random(14);
        String[] users = {"a", "b", "c", "d"};
        int horizon = 300;
        Map<String, boolean[]> busy = new HashMap<>();
        for (String u : users) busy.put(u, new boolean[horizon]);
        for (int i = 0; i < 400; i++) {
            Set<String> who = new TreeSet<>();
            for (String u : users) if (rnd.nextInt(3) == 0) who.add(u);
            if (who.isEmpty()) who.add(users[rnd.nextInt(users.length)]);
            String org = who.iterator().next();
            int from = rnd.nextInt(horizon - 1), to = from + 1 + rnd.nextInt(Math.min(40, horizon - from - 1) + 0);
            if (to > horizon) to = horizon;
            if (to <= from) to = from + 1;
            boolean clash = false;
            for (String u : who) for (int m = from; m < to; m++) clash |= busy.get(u)[m];
            try {
                cal.schedule(org, who, r(from, to), "x");
                assertFalse(clash, "expected a conflict for " + who + " " + from + "-" + to);
                for (String u : who) for (int m = from; m < to; m++) busy.get(u)[m] = true;
            } catch (ConflictException ex) {
                assertTrue(clash, "unexpected conflict");
            }
        }
        for (int t = 0; t < 100; t++) {
            Set<String> who = new TreeSet<>();
            for (String u : users) if (rnd.nextBoolean()) who.add(u);
            if (who.isEmpty()) who.add("a");
            int min = rnd.nextInt(6), from = rnd.nextInt(100), to = from + 1 + rnd.nextInt(horizon - from - 1);
            List<TimeRange> exp = new ArrayList<>();
            int start = -1;
            for (int m = from; m <= to; m++) {
                boolean free = m < to;
                if (free) for (String u : who) free &= !busy.get(u)[m];
                if (free && start < 0) start = m;
                if (!free && start >= 0) { if (m - start >= min && m > start) exp.add(r(start, m)); start = -1; }
            }
            assertEquals(exp, cal.freeSlots(who, r(from, to), Duration.ofMinutes(min)), who + " " + from + "-" + to + " min " + min);
        }
    }

    @Test void largeLoadWithinTimeout() {
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> {
            CalendarService c = new CalendarService();
            for (int i = 0; i < 100_000; i++) c.schedule("u" + (i % 1000), Set.of("u" + ((i + 1) % 1000)), r(i * 10, i * 10 + 5), "e");
            assertEquals(200, c.agenda("u0", r(0, 100_000_000)).size());
            assertFalse(c.freeSlots(Set.of("u0", "u1", "u2"), r(0, 1_000_000), Duration.ofMinutes(1)).isEmpty());
        });
    }

    private static List<String> titles(List<Event> l) { return l.stream().map(Event::title).toList(); }
}
