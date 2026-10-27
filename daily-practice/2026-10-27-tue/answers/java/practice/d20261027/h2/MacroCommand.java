package practice.d20261027.h2;

import java.util.ArrayList;
import java.util.List;

/** Composite pattern: a macro is itself a Command, so UndoManager treats it as one history entry. */
public final class MacroCommand implements Command {
    private final List<Command> parts;

    public MacroCommand(List<Command> parts) { this.parts = List.copyOf(parts); }

    @Override public void execute() {
        List<Command> done = new ArrayList<>();
        try {
            for (Command c : parts) {
                c.execute();
                done.add(c);
            }
        } catch (RuntimeException e) {
            for (int i = done.size() - 1; i >= 0; i--) done.get(i).undo();
            throw e;
        }
    }

    @Override public void undo() {
        for (int i = parts.size() - 1; i >= 0; i--) parts.get(i).undo();
    }
}
