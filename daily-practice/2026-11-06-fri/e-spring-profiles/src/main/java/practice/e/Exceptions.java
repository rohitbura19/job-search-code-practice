package practice.e;

public final class Exceptions {
    private Exceptions() {}

    public static class ProfileNotFoundException extends RuntimeException {
        public ProfileNotFoundException(long id) { super("profile " + id + " not found"); }
    }

    public static class DuplicateEmailException extends RuntimeException {
        public DuplicateEmailException(String email) { super("email already registered: " + email); }
    }

    /** The client's If-Match version is not the current one. */
    public static class StaleVersionException extends RuntimeException {
        public StaleVersionException(long id, long expected, long actual) { super("profile " + id + " is at version " + actual + ", not " + expected); }
    }

    /** A PATCH arrived without If-Match. */
    public static class PreconditionRequiredException extends RuntimeException {
        public PreconditionRequiredException() { super("If-Match header is required"); }
    }
}
