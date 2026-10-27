package practice.d20261027.h2;

/** Undo of an insert is the delete of exactly the inserted range. O(len) each. */
public final class InsertCommand implements Command {
    private final TextDocument doc;
    private final int pos;
    private final String text;

    public InsertCommand(TextDocument doc, int pos, String text) {
        this.doc = doc;
        this.pos = pos;
        this.text = text;
    }

    @Override public void execute() { doc.insert(pos, text); }

    @Override public void undo() { doc.delete(pos, text.length()); }
}
