package q01.fu3_multiple_cars;

import java.util.*;

// FOLLOW-UP 3: "Several cars on the same grid that must not collide. Who owns the occupancy map?"
// Answer: the Grid. It is the one shared object, so it tracks which cell each car is on.
// A car asks the grid before every step and tells the grid when it moves.

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
    private final Map<String, String> occupiedBy = new HashMap<>();   // "x,y" -> car id

    Grid(int width, int height) { this.width = width; this.height = height; }

    void addObstacle(int x, int y) { obstacles.add(key(x, y)); }

    // Called when a car is placed; two cars cannot start on the same cell
    void place(String carId, int x, int y) {
        if (!isFree(x, y)) throw new IllegalArgumentException("cell " + key(x, y) + " is not free");
        occupiedBy.put(key(x, y), carId);
    }

    // Moves a car's marker from one cell to the next
    void moveCar(String carId, int fromX, int fromY, int toX, int toY) {
        occupiedBy.remove(key(fromX, fromY));
        occupiedBy.put(key(toX, toY), carId);
    }

    // Free = inside the boundary, not an obstacle, and no car on it
    boolean isFree(int x, int y) {
        boolean inside = width <= 0 || (x >= 0 && x < width && y >= 0 && y < height);
        String k = key(x, y);
        return inside && !obstacles.contains(k) && !occupiedBy.containsKey(k);
    }

    // Used only for a nicer message: which car is in the way
    String whoIsAt(int x, int y) { return occupiedBy.get(key(x, y)); }

    private static String key(int x, int y) { return x + "," + y; }
}

class Car {
    private final String id;
    private int x, y, distance;
    private Direction dir;
    private final Grid grid;
    private String blockedAt;

    Car(String id, int x, int y, Direction dir, Grid grid) {
        this.id = id; this.x = x; this.y = y; this.dir = dir; this.grid = grid;
        grid.place(id, x, y);                          // register on the shared grid
    }

    void run(String commands) {
        blockedAt = null;                              // each run starts unblocked
        for (String cmd : commands.trim().split("\\s+")) {
            if (cmd.isEmpty()) continue;
            char c = Character.toUpperCase(cmd.charAt(0));
            if (c == 'L') dir = dir.left();
            else if (c == 'R') dir = dir.right();
            else if (c == 'F') move(Integer.parseInt(cmd.substring(1)), 1);
            else if (c == 'B') move(Integer.parseInt(cmd.substring(1)), -1);
            else throw new IllegalArgumentException("Invalid command: " + cmd);
            if (blockedAt != null) break;
        }
    }

    private void move(int steps, int sign) {
        for (int i = 0; i < steps; i++) {
            int nx = x + sign * dir.dx, ny = y + sign * dir.dy;
            if (!grid.isFree(nx, ny)) {
                String other = grid.whoIsAt(nx, ny);
                blockedAt = "(" + nx + "," + ny + ")" + (other != null ? " by car " + other : "");
                return;
            }
            grid.moveCar(id, x, y, nx, ny);            // keep the shared map up to date
            x = nx; y = ny; distance++;
        }
    }

    @Override public String toString() {
        return id + ": (" + x + "," + y + ") " + dir + " distance=" + distance
                + (blockedAt != null ? " BLOCKED at " + blockedAt : "");
    }
}

public class MultipleCarsDemo {
    public static void main(String[] args) {
        Grid g = new Grid(10, 10);
        Car a = new Car("A", 0, 0, Direction.E, g);
        Car b = new Car("B", 5, 0, Direction.N, g);

        // Cars take turns. A drives towards B's start cell, but B has already left it.
        b.run("F3");                     // B: (5,3)
        a.run("F5 L F5");                // A: (5,0) then up the same column, stops under B
        System.out.println(a);           // A: (5,2) N distance=7 BLOCKED at (5,3) by car B
        System.out.println(b);           // B: (5,3) N distance=3
    }
}
