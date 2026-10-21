package practice.e;

/** GIVEN - thrown by the service for an illegal status move. Map it to 409. */
public class InvalidTransitionException extends RuntimeException {
    public InvalidTransitionException(CaseStatus from, CaseStatus to) { super("cannot move from " + from + " to " + to); }
}
