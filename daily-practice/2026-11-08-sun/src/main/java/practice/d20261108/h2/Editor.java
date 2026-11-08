package practice.d20261108.h2;

/**
 * Document with undo/redo. run() executes a command and, only if it succeeds, records it and clears the redo history.
 * At most maxHistory commands are remembered for undo (oldest dropped). undo()/redo() return false when there is nothing to do.
 */
public final class Editor {
    public Editor(String initial, int maxHistory) { throw new UnsupportedOperationException("TODO"); }
    public Editor(String initial) { throw new UnsupportedOperationException("TODO"); }
    public void run(Command c) { throw new UnsupportedOperationException("TODO"); }
    public boolean undo() { throw new UnsupportedOperationException("TODO"); }
    public boolean redo() { throw new UnsupportedOperationException("TODO"); }
    public String text() { throw new UnsupportedOperationException("TODO"); }
    public int undoDepth() { throw new UnsupportedOperationException("TODO"); }
    public int redoDepth() { throw new UnsupportedOperationException("TODO"); }
}
