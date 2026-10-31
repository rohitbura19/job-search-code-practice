package practice.e;

public class StaleVersionException extends RuntimeException {
    public StaleVersionException(long id, long expected, long actual) { super("note " + id + ": expected version " + expected + " but found " + actual); }
}
