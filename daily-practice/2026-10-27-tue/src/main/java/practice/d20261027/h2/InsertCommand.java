package practice.d20261027.h2;

public final class InsertCommand implements Command {
    public InsertCommand(TextDocument doc, int pos, String text) { }

    @Override public void execute() { throw new UnsupportedOperationException("TODO"); }

    @Override public void undo() { throw new UnsupportedOperationException("TODO"); }
}
