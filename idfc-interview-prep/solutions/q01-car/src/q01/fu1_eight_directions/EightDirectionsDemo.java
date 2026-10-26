package q01.fu1_eight_directions;

import java.util.*;

// FOLLOW-UP 1: "Support 8 directions and 45-degree turns. How much code changes?"
// Answer: only the enum. Car and Grid are the same as the base version,
// because Car only ever asks the enum for dx/dy, left() and right().

// 8 directions in clockwise order. Each step of left()/right() is now 45 degrees.
enum Direction {
    N(0, 1), NE(1, 1), E(1, 0), SE(1, -1), S(0, -1), SW(-1, -1), W(-1, 0), NW(-1, 1);

    final int dx, dy;                         // a diagonal step changes both x and y
    Direction(int dx, int dy) { this.dx = dx; this.dy = dy; }

    private static final int COUNT = values().length;   // 8 - no hard-coded "% 4" any more

    Direction left()  { return values()[(ordinal() + COUNT - 1) % COUNT]; }  // 45 deg anticlockwise
    Direction right() { return values()[(ordinal() + 1) % COUNT]; }          // 45 deg clockwise
}

// Same as base: size + obstacles
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

// Same as base. L/R now mean 45 degrees because the enum changed, not the car.
class Car {
    private int x, y, distance;
    private Direction dir;
    private final Grid grid;
    private String blockedAt;

    Car(int x, int y, Direction dir, Grid grid) {
        this.x = x; this.y = y; this.dir = dir; this.grid = grid;
    }

    void run(String commands) {
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
            // Talking point: a diagonal step between two obstacles "cuts the corner".
            // If the rules forbid that, also check (x+dx, y) and (x, y+dy) here.
            if (!grid.isFree(nx, ny)) { blockedAt = "(" + nx + "," + ny + ")"; return; }
            x = nx; y = ny; distance++;
        }
    }

    @Override public String toString() {
        return "(" + x + "," + y + ") " + dir + " distance=" + distance
                + (blockedAt != null ? " BLOCKED at " + blockedAt : "");
    }
}

public class EightDirectionsDemo {
    public static void main(String[] args) {
        // R = 45 deg now: N -> NE, then F3 moves diagonally to (3,3)
        Car c1 = new Car(0, 0, Direction.N, new Grid(0, 0));
        c1.run("R F3");
        System.out.println(c1);              // (3,3) NE distance=3

        // Two lefts = 90 deg: N -> NW -> W
        Car c2 = new Car(5, 5, Direction.N, new Grid(0, 0));
        c2.run("L L F2 R F1");
        System.out.println(c2);              // (2,6) NW distance=3

        // Diagonal into an obstacle
        Grid g = new Grid(5, 5);
        g.addObstacle(2, 2);
        Car c3 = new Car(0, 0, Direction.NE, g);
        c3.run("F4");
        System.out.println(c3);              // (1,1) NE distance=1 BLOCKED at (2,2)
    }
}
