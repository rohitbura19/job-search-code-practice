package practice.d20261027.h2;

import java.util.List;

/** Composite: executes its parts in order and undoes them in reverse. All or nothing: if part k fails on execute, parts 0..k-1 are undone (reverse order) and the exception is rethrown. */
public final class MacroCommand implements Command {
    public MacroCommand(List<Command> parts) { }

    @Override public void execute() { throw new UnsupportedOperationException("TODO"); }

    @Override public void undo() { throw new UnsupportedOperationException("TODO"); }
}
