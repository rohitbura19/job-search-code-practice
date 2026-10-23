# Q01. Car Movement on an XY Plane

**Duration:** 25 minutes · **Language:** Java · **Style:** OOP, with the logic explained afterwards

## Problem Statement

A car is placed on an XY grid. It has a position `(x, y)` and faces one of four directions: `N`, `E`, `S` or `W`. It is controlled by a string of commands separated by spaces:

| Command | Meaning |
|---|---|
| `F<n>` | Move forward `n` cells in the direction the car is facing |
| `B<n>` | Move backward `n` cells without turning |
| `L` | Turn left 90° without moving |
| `R` | Turn right 90° without moving |

`N` increases `y`, `E` increases `x`, `S` decreases `y` and `W` decreases `x`.

The grid can be unbounded, or bounded with width `W` and height `H`, so valid cells are `0 ≤ x < W` and `0 ≤ y < H`. A bounded grid can also contain **obstacles**. The car moves **one cell at a time**. If the next cell is outside the grid or is an obstacle, the car stays on its last valid cell, the move is reported as `BLOCKED at (x,y)`, and the remaining commands are ignored.

Print the final position, the direction, the number of cells actually travelled, and the blocked cell if there was one.

## Expected Output Format

```
(<x>,<y>) <dir> distance=<cells> [BLOCKED at (<bx>,<by>)]
```

## Examples

**Example 1: unbounded grid**
```
start (0,0) N, commands "F2 R F3 L B1"
Output: (3,1) N distance=6
```
F2 moves to (0,2). R turns to face E. F3 moves to (3,2). L turns to face N. B1 moves to (3,1).

**Example 2: 5×5 grid with an obstacle at (2,2)**
```
start (0,0) E, commands "F2 L F4 R F1"
Output: (2,1) N distance=3 BLOCKED at (2,2)
```
F2 moves to (2,0). L turns to face N. During F4 the car reaches (2,1), and the next cell (2,2) is the obstacle, so it stops. `R F1` is ignored.

**Example 3: hits the boundary immediately**
```
5×5 grid, start (4,4) N, commands "F1"
Output: (4,4) N distance=0 BLOCKED at (4,5)
```

**Example 4**
```
5×5 grid, start (1,1) S, commands "R R F3 L F10"
Output: (0,4) W distance=4 BLOCKED at (-1,4)
```

## Expected Design (what the panel looks for)

- `enum Direction { N, E, S, W }` with `left()`, `right()`, `dx()` and `dy()`. Don't use a chain of `if/else` statements for turning.
- `record Position(int x, int y)` with `move(Direction d, int step)`.
- `Grid`: either `UnboundedGrid` or `BoundedGrid(width, height, Set<Position> obstacles)`, with `isFree(Position)`.
- `Car`: holds its position, direction and distance travelled, plus `moveForward(n)`, `moveBackward(n)`, `turnLeft()` and `turnRight()`.
- `Command` interface with `execute(Car)`, implemented by `Forward`, `Backward`, `TurnLeft` and `TurnRight`, and a `CommandParser` that turns the string into a list of commands. This is the Command pattern.
- A custom exception or result type for "blocked".

## Constraints

- The command string can be empty, which means the car doesn't move.
- `n` is between 1 and 10^6. On an unbounded grid, use `long` coordinates or explain the overflow risk.
- An invalid command such as `X3` or `F` throws `IllegalArgumentException` with a clear message.
- An obstacle on the starting cell is invalid input.

## Edge cases the interviewer will ask about

`B` into an obstacle behind the car · four `L` commands in a row · `F0` · lower-case commands · extra spaces · very large `n` on an unbounded grid. On the last one: can you jump straight to the target, and when must you still step one cell at a time?

## Follow-ups

1. Support 8 directions (NE, SE, …) and 45° turns. How much code changes? (Ideally only the enum.)
2. Several cars on the same grid, which must not collide. Who owns the occupancy map?
3. Add other vehicle types: a `Bike` that moves 2 cells per `F1`, or a `Tank` that can't move backward. Would you use inheritance or strategy?
4. `undo()` for the last command. (The Command pattern makes this easy.)
5. Add a command `G x y` that goes to a target cell by the shortest path around obstacles (BFS).
