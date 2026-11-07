package practice.e;

public final class Exceptions {
    private Exceptions() {}

    public static class NotFoundException extends RuntimeException {
        public NotFoundException(String m) { super(m); }
    }

    /** Business rule violations: HTTP 409. */
    public static class ConflictException extends RuntimeException {
        public ConflictException(String m) { super(m); }
    }
}
