package practice.e;

/** An exhaustive switch expression: adding a constant without deciding its moves is a compile error. Same-status moves fall out because no list contains the status itself. */
public enum Status {
    OPEN, IN_PROGRESS, RESOLVED, CLOSED;

    public boolean canMoveTo(Status next) {
        return switch (this) {
            case OPEN -> next == IN_PROGRESS || next == CLOSED;
            case IN_PROGRESS -> next == OPEN || next == RESOLVED;
            case RESOLVED -> next == CLOSED || next == OPEN;
            case CLOSED -> false;
        };
    }
}
