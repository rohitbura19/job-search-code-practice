package practice.d20261027.h2;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Two stacks (ArrayDeque): undo and redo. A new command invalidates the redo branch. Bounded history drops from the bottom
 * of the undo stack (pollLast). All operations O(1). Not thread-safe by design: guard it with the owner's lock or confine to a thread.
 */
public final class UndoManager {
    private final int maxHistory;
    private final Deque<Command> undo = new ArrayDeque<>();
    private final Deque<Command> redo = new ArrayDeque<>();

    public UndoManager(int maxHistory) {
        if (maxHistory < 1) throw new IllegalArgumentException("maxHistory");
        this.maxHistory = maxHistory;
    }

    public void execute(Command c) {
        c.execute();
        undo.push(c);
        redo.clear();
        if (undo.size() > maxHistory) undo.pollLast();
    }

    public boolean undo() {
        if (undo.isEmpty()) return false;
        Command c = undo.pop();
        c.undo();
        redo.push(c);
        return true;
    }

    public boolean redo() {
        if (redo.isEmpty()) return false;
        Command c = redo.pop();
        c.execute();
        undo.push(c);
        return true;
    }

    public boolean canUndo() { return !undo.isEmpty(); }

    public boolean canRedo() { return !redo.isEmpty(); }
}
