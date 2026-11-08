package practice.d20261108.h2;

/** Insert remembers nothing but its own arguments: undo removes exactly text.length() chars at pos. */
public final class InsertCommand implements Command {
    private final int pos;
    private final String text;

    public InsertCommand(int pos, String text) { this.pos = pos; this.text = java.util.Objects.requireNonNull(text); }

    @Override public void execute(StringBuilder doc) {
        if (pos < 0 || pos > doc.length()) throw new IndexOutOfBoundsException("pos " + pos);
        doc.insert(pos, text);
    }

    @Override public void undo(StringBuilder doc) { doc.delete(pos, pos + text.length()); }
}
