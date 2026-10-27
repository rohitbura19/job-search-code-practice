package practice.d20261027.h2;

/** Captures the removed text at execute time (a command stores the state it needs to reverse itself, not the whole document). */
public final class DeleteCommand implements Command {
    private final TextDocument doc;
    private final int pos, len;
    private String removed;

    public DeleteCommand(TextDocument doc, int pos, int len) {
        this.doc = doc;
        this.pos = pos;
        this.len = len;
    }

    @Override public void execute() { removed = doc.delete(pos, len); }

    @Override public void undo() { doc.insert(pos, removed); }
}
