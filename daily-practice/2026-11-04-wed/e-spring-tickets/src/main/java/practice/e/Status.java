package practice.e;

public enum Status {
    OPEN, IN_PROGRESS, RESOLVED, CLOSED;

    // TODO: the allowed moves
    //  OPEN        -> IN_PROGRESS, CLOSED
    //  IN_PROGRESS -> OPEN, RESOLVED
    //  RESOLVED    -> CLOSED, OPEN            (reopen)
    //  CLOSED      -> nothing (terminal)
    //  A move to the SAME status is not allowed either. Prefer a switch expression over the enum (no default branch, so a new constant breaks the build).
    public boolean canMoveTo(Status next) { throw new UnsupportedOperationException("TODO"); }
}
