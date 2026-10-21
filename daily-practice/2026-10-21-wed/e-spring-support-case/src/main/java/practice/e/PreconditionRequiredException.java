package practice.e;

/** GIVEN - throw it from the controller when If-Match is missing or unparsable. Map it to 428. */
public class PreconditionRequiredException extends RuntimeException {
    public PreconditionRequiredException(String message) { super(message); }
}
