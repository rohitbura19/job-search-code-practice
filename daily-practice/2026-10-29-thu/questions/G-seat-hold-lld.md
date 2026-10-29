> Code: `src/main/java/practice/d20261029/g/InMemorySeatHoldService.java` (the interface `SeatHoldService` and the types `Hold`, `Booking`, `SeatState`, `HoldConfig`, `HoldException` are given; read the Javadoc of the interface: it is the contract). Test: `InMemorySeatHoldServiceTest` (movable `Clock`, no sleeps; includes a 50-thread race and a 16-thread overlap test). Sketch classes first in `my-designs/G-classes-sketch.md`. **40 min (sketch 10, code 30).**

# G. LLD: seat holds (the F1 feature)

Implement `InMemorySeatHoldService(Clock, HoldConfig, Map<String, Set<String>> seatsByEvent)`:
- `hold`: all-or-nothing, expiring after `ttl`; hold ids `H1`, `H2`... (only successful holds consume an id).
- `confirm`: idempotent (same booking on retry), `EXPIRED` / `NOT_FOUND` / `NOT_OWNER` as documented.
- `release`: forgiving for unknown, expired, released, confirmed holds; `NOT_OWNER` for someone else's active hold.
- `state`, `availableCount`: an expired hold must not count as held; `availableCount` should not scan all seats.

Design constraints: time only from the `Clock` (the tests move it); different events must not contend on one lock; no background thread required. Write the sketch first: which fields, which lock, how expired holds are found without scanning every hold on every call.
