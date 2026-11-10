# G - LLD of F1: `CalendarService` (sketch 8 min, code 22 min) - `g/*.java`, test `CalendarServiceTest`

Given: `TimeRange` (half-open `[start, end)`), `Event`, `ConflictException`. Implement `CalendarService` (in memory):

- `schedule(organizer, attendees, range, title)`: all participants (organizer included) must be free, otherwise `ConflictException(user, eventId)` for the alphabetically first busy participant, and **nothing** changes (no partial booking, no id consumed). Ids are `e1`, `e2`, ...
- `cancel(eventId)` -> false if unknown. `reschedule(eventId, newRange)` is atomic and an event never conflicts with its own old slot.
- `agenda(user, window)`: events overlapping the window (including ones that started before it), sorted by start.
- `freeSlots(users, window, minLength)`: maximal common free ranges inside the window of at least `minLength`. `firstFree(users, notBefore, length, horizon)`: earliest start or empty.

Before coding, sketch in `my-designs/G-classes-sketch.md`: per-user data structure, why two neighbour checks suffice for overlap, how you keep it all-or-nothing, locking order if it were multi-threaded. Performance: 100,000 events in 3 s.
