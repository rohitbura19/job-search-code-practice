package practice.d20261108.h2;

/** A reversible edit of a text document. execute may capture state needed by undo; both must be repeatable (redo re-executes). */
public interface Command {
    void execute(StringBuilder doc);
    void undo(StringBuilder doc);
}
