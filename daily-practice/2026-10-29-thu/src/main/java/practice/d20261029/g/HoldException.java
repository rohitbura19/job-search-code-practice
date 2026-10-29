package practice.d20261029.g;

public class HoldException extends RuntimeException {
    public enum Reason { UNKNOWN_EVENT, UNKNOWN_SEAT, SEAT_UNAVAILABLE, TOO_MANY_SEATS, NOT_FOUND, EXPIRED, NOT_OWNER }

    private final Reason reason;

    public HoldException(Reason reason, String message) { super(message); this.reason = reason; }

    public Reason reason() { return reason; }
}
