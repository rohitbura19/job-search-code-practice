package practice.d20261108.h2;

/**
 * Deletes the range [start, end). execute throws IndexOutOfBoundsException (document unchanged) unless 0 <= start <= end <= length.
 * It remembers the deleted text so undo can restore it (careful: StringBuilder.delete silently clamps end).
 */
public final class DeleteCommand implements Command {
    public DeleteCommand(int start, int end) { throw new UnsupportedOperationException("TODO"); }
    @Override public void execute(StringBuilder doc) { throw new UnsupportedOperationException("TODO"); }
    @Override public void undo(StringBuilder doc) { throw new UnsupportedOperationException("TODO"); }
}
