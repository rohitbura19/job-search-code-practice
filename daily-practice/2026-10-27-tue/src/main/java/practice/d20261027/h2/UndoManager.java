package practice.d20261027.h2;

/**
 * Invoker with history. execute(c): runs c; if it throws, nothing changes (history untouched, exception propagates). On success c goes
 * on the undo stack and the redo stack is cleared. At most maxHistory commands are kept (the oldest is dropped).
 * undo()/redo() return false when there is nothing to do. redo() re-executes the command.
 */
public final class UndoManager {
    public UndoManager(int maxHistory) { }

    public void execute(Command c) { throw new UnsupportedOperationException("TODO"); }

    public boolean undo() { throw new UnsupportedOperationException("TODO"); }

    public boolean redo() { throw new UnsupportedOperationException("TODO"); }

    public boolean canUndo() { throw new UnsupportedOperationException("TODO"); }

    public boolean canRedo() { throw new UnsupportedOperationException("TODO"); }
}
