package q01.fu4_vehicle_types;

import java.util.*;

// FOLLOW-UP 4: "Add other vehicle types: a Bike moves 2 cells per F1, a Tank cannot reverse."
// Answer: INHERITANCE + POLYMORPHISM. An abstract Vehicle holds all the shared logic
// (parsing, stepping, blocking). Subclasses only override the small rules that differ.

enum Direction {
    N(0, 1), E(1, 0), S(0, -1), W(-1, 0);

    final int dx, dy;
    Direction(int dx, int dy) { this.dx = dx; this.dy = dy; }

    Direction left()  { return values()[(ordinal() + 3) % 4]; }
    Direction right() { return values()[(ordinal() + 1) % 4]; }
}

class Grid {
    private final int width, height;
    private final Set<String> obstacles = new HashSet<>();

    Grid(int width, int height) { this.width = width; this.height = height; }

    void addObstacle(int x, int y) { obstacles.add(x + "," + y); }

    boolean isFree(int x, int y) {
        boolean inside = width <= 0 || (x >= 0 && x < width && y >= 0 && y < height);
        return inside && !obstacles.contains(x + "," + y);
    }
}

// Base class: the common behaviour of every vehicle
abstract class Vehicle {
    protected int x, y, distance;          // protected: subclasses may read them
    protected Direction dir;
    protected final Grid grid;
    protected String blockedAt;

    Vehicle(int x, int y, Direction dir, Grid grid) {
        this.x = x; this.y = y; this.dir = dir; this.grid = grid;
    }

    // Hooks that subclasses override (the only places they differ)
    abstract String name();
    int cellsPerUnit()   { return 1; }     // default: F1 = 1 cell
    boolean canReverse() { return true; }  // default: B is allowed

    // Same parsing loop as the base question, written once for all vehicles
    final void run(String commands) {
        for (String cmd : commands.trim().split("\\s+")) {
            if (cmd.isEmpty()) continue;
            char c = Character.toUpperCase(cmd.charAt(0));
            if (c == 'L') dir = dir.left();
            else if (c == 'R') dir = dir.right();
            else if (c == 'F') move(Integer.parseInt(cmd.substring(1)), 1);
            else if (c == 'B') {
                if (!canReverse()) throw new UnsupportedOperationException(name() + " cannot reverse");
                move(Integer.parseInt(cmd.substring(1)), -1);
            }
            else throw new IllegalArgumentException("Invalid command: " + cmd);
            if (blockedAt != null) break;
        }
    }

    // n units * cellsPerUnit() cells, still one cell at a time so blocking works
    private void move(int units, int sign) {
        int cells = units * cellsPerUnit();
        for (int i = 0; i < cells; i++) {
            int nx = x + sign * dir.dx, ny = y + sign * dir.dy;
            if (!grid.isFree(nx, ny)) { blockedAt = "(" + nx + "," + ny + ")"; return; }
            x = nx; y = ny; distance++;
        }
    }

    @Override public String toString() {
        return name() + ": (" + x + "," + y + ") " + dir + " distance=" + distance
                + (blockedAt != null ? " BLOCKED at " + blockedAt : "");
    }
}

// Behaves exactly like the original car
class Car extends Vehicle {
    Car(int x, int y, Direction dir, Grid grid) { super(x, y, dir, grid); }
    String name() { return "Car"; }
}

// Faster: every unit is 2 cells
class Bike extends Vehicle {
    Bike(int x, int y, Direction dir, Grid grid) { super(x, y, dir, grid); }
    String name() { return "Bike"; }
    @Override int cellsPerUnit() { return 2; }
}

// Cannot go backwards
class Tank extends Vehicle {
    Tank(int x, int y, Direction dir, Grid grid) { super(x, y, dir, grid); }
    String name() { return "Tank"; }
    @Override boolean canReverse() { return false; }
}

public class VehicleTypesDemo {
    public static void main(String[] args) {
        // One list of the base type -> polymorphism: each object uses its own rules
        List<Vehicle> fleet = List.of(
                new Car(0, 0, Direction.N, new Grid(0, 0)),
                new Bike(0, 0, Direction.N, new Grid(0, 0)),
                new Tank(0, 0, Direction.N, new Grid(0, 0)));

        for (Vehicle v : fleet) {
            try {
                v.run("F2 R F1 B1");
                System.out.println(v);
            } catch (UnsupportedOperationException e) {
                System.out.println(v + "  -> " + e.getMessage());
            }
        }
        // Car: (0,2) E distance=4
        // Bike: (0,4) E distance=8
        // Tank: (1,2) E distance=3  -> Tank cannot reverse
    }
}
