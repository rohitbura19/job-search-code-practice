# Q01: Car on an XY plane, solution and follow-ups

Question: [`../../questions/01-car-xy-plane.md`](../../questions/01-car-xy-plane.md)

Each file is standalone, with its own package and its own `main`. To run them in IntelliJ, mark `src` as a source root.

| File | What it answers | OOP idea to name | Diagram |
|---|---|---|---|
| `src/q01/base/CarDemo.java` | **The 25-minute solution** (about 85 lines) | Encapsulation, an enum with behaviour, composition, `toString()` override | [01-base](diagrams/01-base.png) |
| `src/q01/fu1_eight_directions/EightDirectionsDemo.java` | 8 directions and 45° turns | Open/closed: only the enum changes | [02](diagrams/02-fu1-eight-directions.png) |
| `src/q01/fu2_undo/UndoDemo.java` | `undo()` of the last command | **Command pattern**, an interface, a stack (`Deque`) | [03](diagrams/03-fu2-undo.png) |
| `src/q01/fu3_multiple_cars/MultipleCarsDemo.java` | Several cars that must not collide | Shared state owned by `Grid` (an occupancy map) | [04](diagrams/04-fu3-multiple-cars.png) |
| `src/q01/fu4_vehicle_types/VehicleTypesDemo.java` | Bike (2 cells per unit) and Tank (can't reverse) | **Inheritance and polymorphism**, an abstract class, hook methods | [05](diagrams/05-fu4-vehicle-types.png) |
| `src/q01/fu5_go_to_target/GoToTargetDemo.java` | `G x y`, the shortest path around obstacles | BFS, single responsibility (`PathFinder`) | [06](diagrams/06-fu5-go-to-target.png) |

## Expected output (all verified)

```
base:  (3,1) N distance=6
       (2,1) N distance=3 BLOCKED at (2,2)
       (4,4) N distance=0 BLOCKED at (4,5)
       (0,4) W distance=4 BLOCKED at (-1,4)
fu1:   (3,3) NE distance=3 | (2,6) NW distance=3 | (1,1) NE distance=1 BLOCKED at (2,2)
fu2:   (3,2) E distance=5 -> undo -> (0,2) E distance=8 -> undo -> (0,2) N distance=8
       (0,2) N distance=2 BLOCKED at (0,3) -> undo -> (0,0) N distance=4 -> undo() = false
fu3:   A: (5,2) N distance=7 BLOCKED at (5,3) by car B | B: (5,3) N distance=3
fu4:   Car: (0,2) E distance=4 | Bike: (0,4) E distance=8 | Tank: (1,2) E distance=3 -> Tank cannot reverse
fu5:   (4,0) S distance=12 | (0,0) N distance=0 BLOCKED at (4,4) unreachable
```

## One-line answers

- **8 directions:** list them clockwise in the enum and use `% values().length`. `Car` doesn't change.
- **Undo:** each command implements `execute()` and `undo()` and is pushed onto a stack. A move remembers how many cells it actually moved.
- **Several cars:** `Grid` owns `occupiedBy`, so a car asks it before every step and updates it after.
- **Vehicle types:** an abstract `Vehicle` has a `final run()`. Subclasses override only `cellsPerUnit()` and `canReverse()`.
- **Go to (x, y):** BFS from the car, storing each cell's parent. The first time the target is reached is the shortest path. Rebuild the path backwards from the target.
- **Complexity:**
  - Base: O(total steps).
  - BFS: O(W×H) time and space.

Diagrams are in `diagrams/` as PNG for viewing and SVG for the PDF. `tools/make.py` regenerates them (requires Graphviz).
