package practice.d20261025.g;

public class AliasTakenException extends RuntimeException {
    public AliasTakenException(String alias) { super("alias already taken: " + alias); }
}
