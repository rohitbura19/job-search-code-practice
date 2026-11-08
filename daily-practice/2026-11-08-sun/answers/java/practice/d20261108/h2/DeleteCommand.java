package practice.d20261108.h2;

/** Delete captures the removed text at execute time (state for undo), so redo after undo captures it again. */
public final class DeleteCommand implements Command {
    private final int start, end;
    private String removed = "";

    public DeleteCommand(int start, int end) { this.start = start; this.end = end; }

    @Override public void execute(StringBuilder doc) {
        if (start < 0 || start > end || end > doc.length()) throw new IndexOutOfBoundsException("range " + start + ".." + end);
        removed = doc.substring(start, end);
        doc.delete(start, end);
    }

    @Override public void undo(StringBuilder doc) { doc.insert(start, removed); }
}
