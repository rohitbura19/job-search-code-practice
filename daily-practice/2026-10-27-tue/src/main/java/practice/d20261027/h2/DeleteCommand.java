package practice.d20261027.h2;

/** Must remember WHAT it removed (the state needed to undo), captured at execute time. */
public final class DeleteCommand implements Command {
    public DeleteCommand(TextDocument doc, int pos, int len) { }

    @Override public void execute() { throw new UnsupportedOperationException("TODO"); }

    @Override public void undo() { throw new UnsupportedOperationException("TODO"); }
}
