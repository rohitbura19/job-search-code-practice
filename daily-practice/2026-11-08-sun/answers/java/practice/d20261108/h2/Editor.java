package practice.d20261108.h2;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Command pattern + two history stacks. A command is recorded only after execute succeeded, so a failing command leaves
 * both the document and the history untouched. Undo history is a bounded deque (oldest evicted at maxHistory).
 * All operations O(1) plus the cost of the command itself.
 */
public final class Editor {
    private final StringBuilder doc;
    private final int maxHistory;
    private final Deque<Command> undo = new ArrayDeque<>(), redo = new ArrayDeque<>();

    public Editor(String initial, int maxHistory) {
        if (maxHistory < 1) throw new IllegalArgumentException("maxHistory");
        this.doc = new StringBuilder(initial);
        this.maxHistory = maxHistory;
    }

    public Editor(String initial) { this(initial, Integer.MAX_VALUE); }

    public void run(Command c) {
        c.execute(doc);
        undo.addLast(c);
        if (undo.size() > maxHistory) undo.removeFirst();
        redo.clear();
    }

    public boolean undo() {
        if (undo.isEmpty()) return false;
        Command c = undo.removeLast();
        c.undo(doc);
        redo.addLast(c);
        return true;
    }

    public boolean redo() {
        if (redo.isEmpty()) return false;
        Command c = redo.removeLast();
        c.execute(doc);
        undo.addLast(c);
        return true;
    }

    public String text() { return doc.toString(); }
    public int undoDepth() { return undo.size(); }
    public int redoDepth() { return redo.size(); }
}
