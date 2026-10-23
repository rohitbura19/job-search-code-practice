# Q08. Apartment Notice Ticker Board

**Duration:** 30 minutes · **Language:** Java · **Source:** IDFC L2 round, per LeetCode ("notification banner system for an apartment, like a rotating stock-market ticker board; ask clarifying questions")

## Problem Statement

An apartment lobby has a display board that rotates through notices. Each call to `next(now)` returns the notice to show in the next slot.

Rules:

1. A notice has `id`, `text`, `priority` (`EMERGENCY > HIGH > NORMAL`) and an active window `[start, end)`. It is **active** when `start ≤ now < end`.
2. If **any EMERGENCY** notice is active, the board rotates **only among the emergency notices**.
3. Otherwise it rotates **round-robin** through every active notice, in the order **priority first, then posting order**.
4. Round-robin continues from the last notice shown. It picks the first active notice that comes after it in the order above, wrapping around to the start. If notices expired or were added in between, still pick the next one in that order.
5. If nothing is active, return `Optional.empty()`. The board shows a default screen.

## API

```java
enum Priority { EMERGENCY, HIGH, NORMAL }
record Notice(String id, String text, Priority priority, long start, long end) {}
class NoticeBoard {
    void post(Notice n)              // a duplicate id -> IllegalArgumentException
    boolean remove(String id)
    Optional<Notice> next(long now)
}
```

## Example

```
post N1 NORMAL "Water cut 10-12"  [0,100)
post N2 HIGH   "Lift maintenance" [0,50)
post N3 NORMAL "Diwali party"     [20,100)

next(10) -> N2      // active: N2, N1 (N3 not started)
next(15) -> N1
next(20) -> N3      // N3 is now active and comes after N1
next(25) -> N2      // wrap around
next(55) -> N1      // N2 expired at 50
next(60) -> N3

post E1 EMERGENCY "Fire drill" [70,80)
next(70) -> E1
next(75) -> E1      // only emergencies while one is active
next(80) -> N1      // E1 expired (end is exclusive)
next(100) -> empty
```

## Expected Design

- A comparator on `(priority, postSequence)` and a stored cursor of the last shown key. Don't store an index, because indexes shift when notices expire.
- For many notices, use a `TreeSet<Notice>` with a `higher(lastShown)` lookup, plus a min-heap on `end` for cheap removal of expired notices.
- `Clock` or `now` is passed in so tests can control time.
- Keep the rotation **policy** behind an interface such as `RotationStrategy`, so a weighted rotation can be added later.

## Clarifying questions to ask before coding (the panel expects these)

How many screens are there? Is there a fixed duration per slot? Who can post (an admin console)? Are notices scheduled ahead of time? Is it multilingual? Does it need a delivery guarantee if the board is offline?

## Follow-ups

1. **Weighted rotation:** HIGH notices appear twice as often as NORMAL ones.
2. Several boards (one per tower), each with its own set of notices plus shared global ones.
3. Push changes to the boards: polling vs SSE/WebSocket vs MQTT. (Relate this to your IoT/MQTT device experience.)
4. Persistence and APIs: `POST /notices`, `GET /boards/{id}/current`, and the DB schema.
5. Thread safety while an admin posts during a rotation.
