package q01.fu2_undo;

import java.util.*;

// FOLLOW-UP 2: "Add undo() for the last command."
// Idea: turn every command into an object (Command pattern) that knows how to
// execute AND how to reverse itself. Keep executed commands on a stack.

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

// Every command can do and undo itself
interface Command {
    void execute(Car car);
    void undo(Car car);
}

// Turn: undo is simply the opposite turn
class TurnCommand implements Command {
    private final boolean left;
    TurnCommand(boolean left) { this.left = left; }

    public void execute(Car car) { if (left) car.turnLeft(); else car.turnRight(); }
    public void undo(Car car)    { if (left) car.turnRight(); else car.turnLeft(); }
}

// Move: remembers how many cells it REALLY moved (it may have been blocked early),
// so undo goes back exactly that many cells, not the requested number.
class MoveCommand implements Command {
    private final int steps, sign;     // sign +1 = forward, -1 = backward
    private int moved;                 // filled in by execute()

    MoveCommand(int steps, int sign) { this.steps = steps; this.sign = sign; }

    public void execute(Car car) { moved = car.move(steps, sign); }
    public void undo(Car car)    { car.move(moved, -sign); }   // cells we came from are always free
}

// Car exposes small actions; it no longer parses text itself
class Car {
    private int x, y, distance;
    private Direction dir;
    private final Grid grid;
    private String blockedAt;

    Car(int x, int y, Direction dir, Grid grid) {
        this.x = x; this.y = y; this.dir = dir; this.grid = grid;
    }

    void turnLeft()  { dir = dir.left(); }
    void turnRight() { dir = dir.right(); }
    boolean isBlocked() { return blockedAt != null; }
    void clearBlocked() { blockedAt = null; }

    // Returns the number of cells actually moved (needed for undo)
    int move(int steps, int sign) {
        int done = 0;
        for (int i = 0; i < steps; i++) {
            int nx = x + sign * dir.dx, ny = y + sign * dir.dy;
            if (!grid.isFree(nx, ny)) { blockedAt = "(" + nx + "," + ny + ")"; break; }
            x = nx; y = ny; done++;
        }
        distance += done;
        return done;
    }

    @Override public String toString() {
        return "(" + x + "," + y + ") " + dir + " distance=" + distance
                + (blockedAt != null ? " BLOCKED at " + blockedAt : "");
    }
}

// Runs commands and keeps history for undo
class Controller {
    private final Car car;
    private final Deque<Command> history = new ArrayDeque<>();   // stack: last command on top

    Controller(Car car) { this.car = car; }

    void run(String commands) {
        for (String cmd : commands.trim().split("\\s+")) {
            if (cmd.isEmpty()) continue;
            Command c = parse(cmd);
            c.execute(car);
            history.push(c);                    // remember it so it can be undone
            if (car.isBlocked()) break;
        }
    }

    // Reverses the most recent command; returns false if there is nothing to undo
    boolean undo() {
        if (history.isEmpty()) return false;
        history.pop().undo(car);
        car.clearBlocked();                     // after undo the car is no longer stuck
        return true;
    }

    private Command parse(String cmd) {
        char c = Character.toUpperCase(cmd.charAt(0));
        if (c == 'L') return new TurnCommand(true);
        if (c == 'R') return new TurnCommand(false);
        if (c == 'F') return new MoveCommand(Integer.parseInt(cmd.substring(1)), 1);
        if (c == 'B') return new MoveCommand(Integer.parseInt(cmd.substring(1)), -1);
        throw new IllegalArgumentException("Invalid command: " + cmd);
    }

    Car car() { return car; }
}

public class UndoDemo {
    public static void main(String[] args) {
        Controller ctl = new Controller(new Car(0, 0, Direction.N, new Grid(0, 0)));
        ctl.run("F2 R F3");
        System.out.println(ctl.car());   // (3,2) E distance=5
        ctl.undo();                      // undo F3
        System.out.println(ctl.car());   // (0,2) E distance=8 (distance counts every cell driven)
        ctl.undo();                      // undo R
        System.out.println(ctl.car());   // (0,2) N distance=8

        // Undo after a block goes back only the cells really moved
        Grid g = new Grid(5, 5);
        g.addObstacle(0, 3);
        Controller ctl2 = new Controller(new Car(0, 0, Direction.N, g));
        ctl2.run("F10");
        System.out.println(ctl2.car());  // (0,2) N distance=2 BLOCKED at (0,3)
        ctl2.undo();
        System.out.println(ctl2.car());  // (0,0) N distance=4
        System.out.println(ctl2.undo()); // false - nothing left to undo
    }
}
