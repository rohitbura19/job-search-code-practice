package practice.e;

/** GIVEN. */
public enum CaseStatus {
    OPEN, IN_PROGRESS, RESOLVED, CLOSED;

    public boolean canMoveTo(CaseStatus n) {
        return switch (this) {
            case OPEN -> n == IN_PROGRESS || n == CLOSED;
            case IN_PROGRESS -> n == RESOLVED || n == OPEN;
            case RESOLVED -> n == CLOSED || n == IN_PROGRESS;
            case CLOSED -> false;
        };
    }
}
