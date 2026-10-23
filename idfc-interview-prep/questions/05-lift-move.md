# Q05. Lift Controller: implement `move()`

**Duration:** 30 minutes · **Language:** Java · **Source:** IDFC onsite design round, per LeetCode ("Lift class, write move() to tell the sensor up or down")

## Problem Statement

Model a single lift in a building with floors `minFloor..maxFloor`. The hardware is reached through a `LiftMotor` interface:

```java
interface LiftMotor { void up(); void down(); void openDoor(); }
```

People can add requests at any time with `request(int floor)`. These come from inside the lift or from a floor button.

A controller calls `move()` once per tick. Each call performs **exactly one** action and returns it:

| Situation | Action | Returned |
|---|---|---|
| There is a pending request for the current floor | Serve it (remove it) and call `openDoor()` | `OPEN` |
| No pending requests | The lift becomes idle | `IDLE` |
| Already moving UP and a request is above | Move up one floor (`up()`) | `UP` |
| Already moving UP and nothing is above | Reverse and move down one floor | `DOWN` |
| Moving DOWN | Same rules, mirrored | `DOWN` / `UP` |
| Idle with requests pending | Head toward the **nearest** request. On a tie, go **UP** | `UP` / `DOWN` |

This is the **LOOK** algorithm: keep going in the current direction while requests lie ahead, then reverse.

## API

```java
enum Action { UP, DOWN, OPEN, IDLE }
class Lift {
    Lift(int minFloor, int maxFloor, int startFloor, LiftMotor motor)
    void request(int floor)      // out of range -> IllegalArgumentException; duplicates ignored
    Action move()
    int currentFloor()
}
```

## Examples

**Example 1**
```
Lift at 0 (idle). request(5), request(2)
move() x8 -> UP, UP, OPEN(2), UP, UP, UP, OPEN(5), IDLE
then request(3), request(7)   // at floor 5: both are 2 away, tie -> UP
move() x9 -> UP, UP, OPEN(7), DOWN, DOWN, DOWN, DOWN, OPEN(3), IDLE
currentFloor() == 3
```

**Example 2: a request arrives while the lift passes a floor**
```
Lift at 4. request(6), request(1)  // 6 is 2 away, 1 is 3 away -> UP
move() -> UP        (now at 5)
request(5)          // added while the lift is at 5
move() x9 -> OPEN(5), UP, OPEN(6), DOWN, DOWN, DOWN, DOWN, DOWN, OPEN(1)
```

## Expected Design

- `Direction` enum (`UP`, `DOWN`, `IDLE`) and `Action` enum.
- Pending requests in a `TreeSet<Integer>`, so `higher()` and `lower()` find the next stop in O(log n). Explain why a `List` would be O(n).
- `LiftMotor` is injected, which allows a fake motor in tests (dependency inversion).
- `move()` stays small. Pull out helpers such as `hasRequestsAbove()` and `nearestDirection()`.

## Edge cases to discuss

A request for the floor the lift is on while idle (`OPEN` on the next move) · requests at `minFloor` and `maxFloor` · lots of duplicate requests · the lift must never move past the floor limits.

## Follow-ups

1. Hall calls with a direction (`call(floor, UP)`): only stop when the call is in the direction of travel.
2. **Several lifts** with a `Dispatcher` that picks a lift (nearest, least loaded). Use the Strategy pattern.
3. Door and lift states (`DOOR_OPEN`, `MOVING`, `MAINTENANCE`) as a **State** pattern.
4. Capacity and weight sensor: skip hall calls when the lift is full.
5. Thread safety: buttons are pressed on other threads while `move()` runs on the controller thread.
