package practice.d20261011.g;

/** GIVEN - do not edit. A failure that retrying cannot fix (bad phone number, unsubscribed). */
public class PermanentFailure extends RuntimeException {
    public PermanentFailure(String message) { super(message); }
}
