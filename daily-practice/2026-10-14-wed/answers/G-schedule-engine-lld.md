# Answers: G-schedule-engine-lld

(Spoilers. Open only after your attempt.)

**Reference** (compiled and run; every test line below is real output)
```java
record Window(Set<DayOfWeek> days, LocalTime start, LocalTime end) {
    Window {                                                      // compact constructor validates once
        days = Set.copyOf(days);
        if (days.isEmpty()) throw new IllegalArgumentException("no days");
        if (start.equals(end)) throw new IllegalArgumentException("start == end is ambiguous; use two rules for 24h");
    }
    boolean crossesMidnight() { return end.isBefore(start); }
}

final class Schedule {                                            // immutable value object
    private final long version; private final ZoneId zone; private final List<Window> windows;
    Schedule(long version, ZoneId zone, List<Window> windows) { this.version = version; this.zone = zone; this.windows = List.copyOf(windows); }

    private Instant startOf(LocalDate d, Window w) { return d.atTime(w.start()).atZone(zone).toInstant(); }
    private Instant endOf(LocalDate d, Window w) {
        return (w.crossesMidnight() ? d.plusDays(1) : d).atTime(w.end()).atZone(zone).toInstant();
    }
    boolean isAllowed(Instant now) {                              // pure function of (rules, now)
        LocalDate today = now.atZone(zone).toLocalDate();
        for (int back = 0; back <= 1; back++) {                   // a window that started yesterday may still be running
            LocalDate d = today.minusDays(back);
            for (Window w : windows) {
                if (!w.days().contains(d.getDayOfWeek())) continue;     // a window belongs to the day it STARTS on
                if (!now.isBefore(startOf(d, w)) && now.isBefore(endOf(d, w))) return true;   // [start, end)
            }
        }
        return false;
    }
    Optional<Instant> nextBoundary(Instant now) {                 // next start/end instant strictly after now (within 8 days)
        LocalDate today = now.atZone(zone).toLocalDate(); Instant best = null;
        for (int off = -1; off <= 8; off++) {
            LocalDate d = today.plusDays(off);
            for (Window w : windows) {
                if (!w.days().contains(d.getDayOfWeek())) continue;
                for (Instant t : new Instant[]{startOf(d, w), endOf(d, w)})
                    if (t.isAfter(now) && (best == null || t.isBefore(best))) best = t;
            }
        }
        return Optional.ofNullable(best);
    }
}

interface ChargingConstraint { boolean allows(Instant t); }                      // Specification pattern
record AllOf(List<ChargingConstraint> parts) implements ChargingConstraint {     // Composite: all must say yes
    public boolean allows(Instant t) { return parts.stream().allMatch(c -> c.allows(t)); }
}
```
(`Schedule`, `Window` and the tests were compiled and run; the small `AllOf` composite at the end is straightforward but was not run.)

Verified results (America/New_York, windows as stated):
- Spring forward (Sun 8 Mar 2026): window Sun 01:30 to 03:30 local is only ONE real hour. `06:29Z` false, `06:30Z` true, `07:29Z` true, `07:30Z` false.
- Window Sun 02:30 to 04:00 on that day: 02:30 does not exist; Java moves the start forward by the gap, so the first boundary is `07:30Z` (03:30 EDT).
- Fall back (Sun 1 Nov 2026): window Sun 00:30 to 02:30 lasts THREE real hours (180 minutes counted by stepping minute by minute).
- Midnight crossing, Mon and Tue 23:00 to 06:00: Tue 02:00 allowed (Mon window), Wed 02:00 allowed (Tue window), Thu 02:00 NOT allowed (Wed is not a start day).
- `new Window(MONDAY, 12:00, 12:00)` is rejected with a clear message instead of silently meaning "empty" or "24 h".

DST decisions to state out loud: `LocalDateTime.atZone` resolves a gap by shifting forward and an overlap by choosing the earlier offset; that is a deliberate, documented choice, not an accident. Compute boundaries from the local date and zone each time, never by adding 24 h to an `Instant`.

Patterns: Value Object (immutable `Schedule`, `Window`), Specification + Composite (`ChargingConstraint`, `AllOf`) so tariff and curtailment plug in without touching schedules, Strategy (how to resolve gap/overlap if the product wants another rule), Factory (build from the shadow JSON and validate), State (device reports `PENDING -> APPLIED`).

Concurrency: the object is immutable, so it is safely shared across threads. On the device, replacing the active schedule is a single atomic reference swap (`AtomicReference<Schedule>` or a volatile field), and the version check (`incoming.version() > current.version()`) makes late or duplicate deliveries harmless. A timer thread sleeps until `nextBoundary` and re-evaluates; on a clock jump it re-evaluates from scratch (pure function), never trusts a saved flag.

Test plan: table-driven tests for every weekday and boundary (start inclusive, end exclusive); the DST cases above for several zones, including a zone with a 30-minute shift (Australia/Lord_Howe) and one with no DST (Asia/Kolkata); a leap day; overlapping windows (union behaviour); `nextBoundary` when nothing is scheduled returns empty; property test comparing `isAllowed` against a brute-force minute-by-minute oracle over a year; version-ordering test (older version ignored); an explicit `Instant` parameter or injected `Clock` so no test sleeps.

Principal-level changes to discuss: `nextBoundary` returns the next start or end even when two windows touch (one ends at 06:00, another begins at 06:00); merge windows first so you do not wake for a no-op change; precompute the next 30 days of transition instants on the cloud and ship them, so a tiny device does not need a full tz database; the 8-day scan is simple but wasteful, an indexed list of boundaries per weekday makes it O(rules); keep the schedule JSON versioned (`schemaVersion`) so old firmware can ignore unknown fields.
