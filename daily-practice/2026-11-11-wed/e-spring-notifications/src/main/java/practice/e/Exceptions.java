package practice.e;

public final class Exceptions {
    private Exceptions() {}
    public static class NotFoundException extends RuntimeException { public NotFoundException(String m) { super(m); } }
    public static class InvalidTargetException extends RuntimeException { public InvalidTargetException(String m) { super(m); } }
}
