package practice.d20261105.g;

/** Given. */
public class CouponException extends RuntimeException {
    public enum Reason { UNKNOWN, NOT_STARTED, EXPIRED, MIN_NOT_MET, EXHAUSTED, USER_LIMIT }

    private final Reason reason;

    public CouponException(Reason reason) { super(reason.name()); this.reason = reason; }

    public Reason reason() { return reason; }
}
