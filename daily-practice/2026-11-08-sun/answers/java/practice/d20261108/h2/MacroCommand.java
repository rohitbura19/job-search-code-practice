package practice.d20261108.h2;

import java.util.List;

/** Composite of commands with all-or-nothing execute (compensating undo of the completed prefix). */
public final class MacroCommand implements Command {
    private final List<Command> steps;

    public MacroCommand(List<Command> steps) { this.steps = List.copyOf(steps); }

    @Override public void execute(StringBuilder doc) {
        int done = 0;
        try {
            for (Command c : steps) { c.execute(doc); done++; }
        } catch (RuntimeException e) {
            for (int i = done - 1; i >= 0; i--) steps.get(i).undo(doc);
            throw e;
        }
    }

    @Override public void undo(StringBuilder doc) {
        for (int i = steps.size() - 1; i >= 0; i--) steps.get(i).undo(doc);
    }
}
