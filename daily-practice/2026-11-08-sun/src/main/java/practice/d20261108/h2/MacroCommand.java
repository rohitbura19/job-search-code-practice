package practice.d20261108.h2;

import java.util.List;

/**
 * Runs the steps in order; undo runs them in reverse. Atomic: if step i throws during execute, steps 0..i-1 are undone
 * (document restored) and the exception is rethrown. The list is copied defensively.
 */
public final class MacroCommand implements Command {
    public MacroCommand(List<Command> steps) { throw new UnsupportedOperationException("TODO"); }
    @Override public void execute(StringBuilder doc) { throw new UnsupportedOperationException("TODO"); }
    @Override public void undo(StringBuilder doc) { throw new UnsupportedOperationException("TODO"); }
}
