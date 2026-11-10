package practice.d20261110.g;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

/**
 * Per-user TreeMap<start, Event>: the user's events never overlap, so ordering by start also orders by end and an overlap check against a new
 * range needs only two neighbours (floorEntry(start) and ceilingEntry(start)) -> O(log n). schedule validates every participant (sorted) first and mutates
 * afterwards, which makes it all-or-nothing without rollback. freeSlots collects the clipped busy ranges of all users, sorts by start, merges, and
 * emits the gaps: O(B log B) for B busy ranges in the window (a k-way merge of the already sorted lists would be O(B log k)).
 * Thread-safety: one lock per calendar acquired in alphabetical user order (no deadlock), or a single-writer actor per user partition.
 */
public class CalendarService {
    private final Map<String, TreeMap<Instant, Event>> calendars = new HashMap<>();
    private final Map<String, Event> events = new HashMap<>();
    private int nextId = 1;

    public Event schedule(String organizer, Set<String> attendees, TimeRange range, String title) {
        TreeSet<String> people = new TreeSet<>(attendees);
        people.add(organizer);
        checkFree(people, range, null);
        Event e = new Event("e" + nextId++, title, range, organizer, Set.copyOf(people));
        store(e);
        return e;
    }

    public boolean cancel(String eventId) {
        Event e = events.remove(eventId);
        if (e == null) return false;
        for (String u : e.participants()) calendars.get(u).remove(e.range().start());
        return true;
    }

    public Event reschedule(String eventId, TimeRange newRange) {
        Event old = events.get(eventId);
        if (old == null) throw new NoSuchElementException(eventId);
        checkFree(new TreeSet<>(old.participants()), newRange, eventId);
        cancel(eventId);
        Event moved = new Event(old.id(), old.title(), newRange, old.organizer(), old.participants());
        store(moved);
        return moved;
    }

    public List<Event> agenda(String user, TimeRange window) {
        TreeMap<Instant, Event> cal = calendars.get(user);
        if (cal == null) return List.of();
        List<Event> out = new ArrayList<>();
        Map.Entry<Instant, Event> before = cal.lowerEntry(window.start());
        if (before != null && before.getValue().range().overlaps(window)) out.add(before.getValue());
        for (Event e : cal.subMap(window.start(), true, window.end(), false).values()) out.add(e);
        return out;
    }

    public List<TimeRange> freeSlots(Set<String> users, TimeRange window, Duration minLength) {
        List<TimeRange> busy = new ArrayList<>();
        for (String u : users) for (Event e : agenda(u, window)) busy.add(e.range());
        busy.sort(Comparator.comparing(TimeRange::start));
        List<TimeRange> free = new ArrayList<>();
        Instant cursor = window.start();
        for (TimeRange b : busy) {
            if (b.start().isAfter(cursor)) addIfLong(free, cursor, b.start(), minLength);
            if (b.end().isAfter(cursor)) cursor = b.end();
        }
        if (window.end().isAfter(cursor)) addIfLong(free, cursor, window.end(), minLength);
        return free;
    }

    public Optional<Instant> firstFree(Set<String> users, Instant notBefore, Duration length, Duration horizon) {
        return freeSlots(users, new TimeRange(notBefore, notBefore.plus(horizon)), length).stream().findFirst().map(TimeRange::start);
    }

    private static void addIfLong(List<TimeRange> out, Instant from, Instant to, Duration min) {
        if (!Duration.between(from, to).minus(min).isNegative() && to.isAfter(from)) out.add(new TimeRange(from, to));
    }

    private void checkFree(SortedSet<String> people, TimeRange range, String ignoreEventId) {
        for (String u : people) {
            TreeMap<Instant, Event> cal = calendars.get(u);
            if (cal == null) continue;
            Map.Entry<Instant, Event> prev = cal.floorEntry(range.start());
            if (prev != null && !prev.getValue().id().equals(ignoreEventId) && prev.getValue().range().overlaps(range))
                throw new ConflictException(u, prev.getValue().id());
            // any event starting inside the range (first one that is not the ignored event)
            for (Event next : cal.subMap(range.start(), false, range.end(), false).values())
                if (!next.id().equals(ignoreEventId)) throw new ConflictException(u, next.id());
        }
    }

    private void store(Event e) {
        events.put(e.id(), e);
        for (String u : e.participants()) calendars.computeIfAbsent(u, k -> new TreeMap<>()).put(e.range().start(), e);
    }
}
