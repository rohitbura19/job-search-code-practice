package q01.fu5_go_to_target;

import java.util.*;

// FOLLOW-UP 5: "Add a command G x y: drive to (x,y) by the shortest path around obstacles."
// Answer: BFS (breadth-first search) on the grid. On an unweighted grid, BFS finds
// the shortest path. Store each cell's parent so the path can be rebuilt backwards.
// Needs a bounded grid, otherwise an unreachable target would search forever.

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

    Grid(int width, int height) {
        if (width <= 0 || height <= 0) throw new IllegalArgumentException("BFS needs a bounded grid");
        this.width = width; this.height = height;
    }

    void addObstacle(int x, int y) { obstacles.add(x + "," + y); }

    boolean isFree(int x, int y) {
        boolean inside = x >= 0 && x < width && y >= 0 && y < height;
        return inside && !obstacles.contains(x + "," + y);
    }
}

// Shortest-path helper, kept separate from Car (single responsibility)
class PathFinder {
    // Returns the directions to drive, in order; empty if already there; null if unreachable
    static List<Direction> shortestPath(Grid grid, int sx, int sy, int tx, int ty) {
        Map<String, String> parent = new HashMap<>();      // cell -> cell we came from
        Map<String, Direction> stepTo = new HashMap<>();   // cell -> direction used to enter it
        Deque<int[]> queue = new ArrayDeque<>();
        queue.add(new int[]{sx, sy});
        parent.put(sx + "," + sy, null);                   // start has no parent; also marks visited

        while (!queue.isEmpty()) {
            int[] cur = queue.poll();
            if (cur[0] == tx && cur[1] == ty) break;       // first time we reach target = shortest
            for (Direction d : Direction.values()) {
                int nx = cur[0] + d.dx, ny = cur[1] + d.dy;
                String k = nx + "," + ny;
                if (grid.isFree(nx, ny) && !parent.containsKey(k)) {
                    parent.put(k, cur[0] + "," + cur[1]);
                    stepTo.put(k, d);
                    queue.add(new int[]{nx, ny});
                }
            }
        }

        String target = tx + "," + ty;
        if (!parent.containsKey(target)) return null;     // never reached

        // Walk back from target to start, then reverse
        LinkedList<Direction> path = new LinkedList<>();
        for (String k = target; parent.get(k) != null; k = parent.get(k)) path.addFirst(stepTo.get(k));
        return path;
    }
}

class Car {
    private int x, y, distance;
    private Direction dir;
    private final Grid grid;
    private String blockedAt;

    Car(int x, int y, Direction dir, Grid grid) {
        this.x = x; this.y = y; this.dir = dir; this.grid = grid;
    }

    // Same as base, plus "G x y" (G and its two numbers are three tokens)
    void run(String commands) {
        String[] t = commands.trim().split("\\s+");
        for (int i = 0; i < t.length; i++) {
            if (t[i].isEmpty()) continue;
            char c = Character.toUpperCase(t[i].charAt(0));
            if (c == 'L') dir = dir.left();
            else if (c == 'R') dir = dir.right();
            else if (c == 'F') move(Integer.parseInt(t[i].substring(1)), 1);
            else if (c == 'B') move(Integer.parseInt(t[i].substring(1)), -1);
            else if (c == 'G') { goTo(Integer.parseInt(t[i + 1]), Integer.parseInt(t[i + 2])); i += 2; }
            else throw new IllegalArgumentException("Invalid command: " + t[i]);
            if (blockedAt != null) break;
        }
    }

    // Finds the shortest path, then drives it; the car ends up facing its last move
    private void goTo(int tx, int ty) {
        List<Direction> path = PathFinder.shortestPath(grid, x, y, tx, ty);
        if (path == null) { blockedAt = "(" + tx + "," + ty + ") unreachable"; return; }
        for (Direction d : path) { dir = d; x += d.dx; y += d.dy; distance++; }
    }

    private void move(int steps, int sign) {
        for (int i = 0; i < steps; i++) {
            int nx = x + sign * dir.dx, ny = y + sign * dir.dy;
            if (!grid.isFree(nx, ny)) { blockedAt = "(" + nx + "," + ny + ")"; return; }
            x = nx; y = ny; distance++;
        }
    }

    @Override public String toString() {
        return "(" + x + "," + y + ") " + dir + " distance=" + distance
                + (blockedAt != null ? " BLOCKED at " + blockedAt : "");
    }
}

public class GoToTargetDemo {
    public static void main(String[] args) {
        // A wall at x=2 for y=0..3 forces the car to go round the top
        Grid g = new Grid(5, 5);
        for (int y = 0; y <= 3; y++) g.addObstacle(2, y);

        Car c = new Car(0, 0, Direction.N, g);
        c.run("G 4 0");
        System.out.println(c);           // (4,0) S distance=12  (up 4, right 4, down 4)

        // Target fully surrounded -> unreachable
        Grid g2 = new Grid(5, 5);
        g2.addObstacle(3, 4); g2.addObstacle(4, 3);
        Car c2 = new Car(0, 0, Direction.N, g2);
        c2.run("G 4 4");
        System.out.println(c2);          // (0,0) N distance=0 BLOCKED at (4,4) unreachable
    }
}
