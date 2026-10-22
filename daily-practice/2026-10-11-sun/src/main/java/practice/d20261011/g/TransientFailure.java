package practice.d20261011.g;

/** GIVEN - do not edit. A failure that may succeed on retry or on another provider (timeout, 5xx). */
public class TransientFailure extends RuntimeException {
    public TransientFailure(String message) { super(message); }
}
