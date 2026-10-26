package q01.base;

import java.util.*;

// Direction the car faces. Each constant carries how one step changes x and y.
enum Direction {
    N(0, 1), E(1, 0), S(0, -1), W(-1, 0);   // order is clockwise: N -> E -> S -> W

    final int dx, dy;                        // change in x and y for one forward step
    Direction(int dx, int dy) { this.dx = dx; this.dy = dy; }

    // Left = one step anticlockwise in the list (+3 is the same as -1 mod 4, avoids negatives)
    Direction left()  { return values()[(ordinal() + 3) % 4]; }
    // Right = one step clockwise in the list
    Direction right() { return values()[(ordinal() + 1) % 4]; }
}

// The plane the car drives on: knows its size and which cells are blocked.
class Grid {
    private final int width, height;                    // width <= 0 means no boundary (unbounded plane)
    private final Set<String> obstacles = new HashSet<>(); // blocked cells stored as "x,y" for O(1) lookup

    Grid(int width, int height) { this.width = width; this.height = height; }

    // Mark a cell as blocked
    void addObstacle(int x, int y) { obstacles.add(x + "," + y); }

    // A cell is free if it is inside the boundary (when there is one) and not an obstacle
    boolean isFree(int x, int y) {
        boolean inside = width <= 0 || (x >= 0 && x < width && y >= 0 && y < height);
        return inside && !obstacles.contains(x + "," + y);
    }
}

// The car: holds its own state and moves itself on a grid.
class Car {
    private int x, y, distance;   // current position and total cells actually travelled
    private Direction dir;        // direction the car is facing
    private final Grid grid;      // grid it drives on (asked before every step)
    private String blockedAt;     // cell that stopped the car; null while not blocked

    Car(int x, int y, Direction dir, Grid grid) {
        this.x = x; this.y = y; this.dir = dir; this.grid = grid;
    }

    // Reads commands like "F2 R F3 L B1" and executes them one by one
    void run(String commands) {
        for (String cmd : commands.trim().split("\\s+")) {   // split on one or more spaces
            if (cmd.isEmpty()) continue;                     // empty input -> nothing to do
            char c = Character.toUpperCase(cmd.charAt(0));   // accept lower case too
            if (c == 'L') dir = dir.left();                  // turn only, no movement
            else if (c == 'R') dir = dir.right();
            else if (c == 'F') move(Integer.parseInt(cmd.substring(1)), 1);   // forward n cells
            else if (c == 'B') move(Integer.parseInt(cmd.substring(1)), -1);  // backward n cells, same facing
            else throw new IllegalArgumentException("Invalid command: " + cmd);
            if (blockedAt != null) break;                    // rule: once blocked, ignore the remaining commands
        }
    }

    // Moves one cell at a time so the car stops on the last free cell before a block
    private void move(int steps, int sign) {
        for (int i = 0; i < steps; i++) {
            int nx = x + sign * dir.dx, ny = y + sign * dir.dy;   // next cell (sign -1 reverses for B)
            if (!grid.isFree(nx, ny)) { blockedAt = "(" + nx + "," + ny + ")"; return; }
            x = nx; y = ny; distance++;                          // step is valid -> commit it
        }
    }

    // Output format: (x,y) DIR distance=n [BLOCKED at (bx,by)]
    @Override public String toString() {
        return "(" + x + "," + y + ") " + dir + " distance=" + distance
                + (blockedAt != null ? " BLOCKED at " + blockedAt : "");
    }
}

// Entry point: builds grids and cars for the 4 examples and prints the results.
public class CarDemo {
    public static void main(String[] args) {
        // Example 1: unbounded grid -> (3,1) N distance=6
        Car c1 = new Car(0, 0, Direction.N, new Grid(0, 0));
        c1.run("F2 R F3 L B1");
        System.out.println(c1);

        // Example 2: 5x5 grid, obstacle at (2,2) -> (2,1) N distance=3 BLOCKED at (2,2)
        Grid g = new Grid(5, 5);
        g.addObstacle(2, 2);
        Car c2 = new Car(0, 0, Direction.E, g);
        c2.run("F2 L F4 R F1");
        System.out.println(c2);

        // Example 3: hits the top boundary straight away -> (4,4) N distance=0 BLOCKED at (4,5)
        Car c3 = new Car(4, 4, Direction.N, new Grid(5, 5));
        c3.run("F1");
        System.out.println(c3);

        // Example 4: two right turns then left boundary -> (0,4) W distance=4 BLOCKED at (-1,4)
        Car c4 = new Car(1, 1, Direction.S, new Grid(5, 5));
        c4.run("R R F3 L F10");
        System.out.println(c4);
    }
}
