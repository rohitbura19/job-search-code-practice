package practice.d20261108.h2;

/** Inserts text at pos. execute throws IndexOutOfBoundsException (document unchanged) if pos is not in [0, length]. */
public final class InsertCommand implements Command {
    public InsertCommand(int pos, String text) { throw new UnsupportedOperationException("TODO"); }
    @Override public void execute(StringBuilder doc) { throw new UnsupportedOperationException("TODO"); }
    @Override public void undo(StringBuilder doc) { throw new UnsupportedOperationException("TODO"); }
}
