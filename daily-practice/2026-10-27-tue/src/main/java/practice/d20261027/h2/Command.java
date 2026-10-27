package practice.d20261027.h2;

/** A reversible action on a TextDocument. undo() is only called after a successful execute() and must restore the exact previous state. */
public interface Command {
    void execute();
    void undo();
}
