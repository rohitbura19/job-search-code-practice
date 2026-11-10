package practice.d20261110.g;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * In-memory calendar for many users. Single-threaded contract is enough for the tests, but state changes must be all-or-nothing
 * (a failed schedule/reschedule leaves every calendar untouched) and the design should say how it would be made thread-safe.
 *
 *  - schedule(organizer, attendees, range, title): participants = organizer + attendees. If ANY participant has an overlapping event ->
 *    ConflictException(user, eventId of the clash) (check participants in alphabetical order so the result is deterministic) and nothing changes.
 *    Otherwise store it and return the Event with ids "e1", "e2", ... (a failed attempt does not consume an id).
 *  - cancel(eventId): remove from every participant's calendar; false if unknown.
 *  - reschedule(eventId, newRange): move atomically; the event's own old slot never conflicts with itself; unknown id -> NoSuchElementException;
 *    on conflict throw ConflictException and keep the old slot. Returns the updated Event.
 *  - agenda(user, window): the user's events overlapping the window, sorted by start (then id).
 *  - freeSlots(users, window, minLength): maximal ranges inside the window where ALL users are free, each at least minLength long, ascending.
 *  - firstFree(users, notBefore, length, horizon): earliest start >= notBefore of a slot of that length for all users within notBefore + horizon, or empty.
 * Aim: schedule/cancel O(log n) per participant, agenda O(log n + k), freeSlots a merge of the users' sorted busy lists (no per-minute scanning).
 */
public class CalendarService {
    public CalendarService() {}

    public Event schedule(String organizer, Set<String> attendees, TimeRange range, String title) { throw new UnsupportedOperationException("TODO"); }
    public boolean cancel(String eventId) { throw new UnsupportedOperationException("TODO"); }
    public Event reschedule(String eventId, TimeRange newRange) { throw new UnsupportedOperationException("TODO"); }
    public List<Event> agenda(String user, TimeRange window) { throw new UnsupportedOperationException("TODO"); }
    public List<TimeRange> freeSlots(Set<String> users, TimeRange window, Duration minLength) { throw new UnsupportedOperationException("TODO"); }
    public Optional<java.time.Instant> firstFree(Set<String> users, java.time.Instant notBefore, Duration length, Duration horizon) { throw new UnsupportedOperationException("TODO"); }
}
