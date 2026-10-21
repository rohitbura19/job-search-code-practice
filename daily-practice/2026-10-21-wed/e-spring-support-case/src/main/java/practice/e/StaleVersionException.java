package practice.e;

import java.util.UUID;

/** GIVEN - thrown by the service when If-Match does not match the stored @Version. Map it to 409. */
public class StaleVersionException extends RuntimeException {
    public StaleVersionException(UUID id) { super("stale version for case " + id); }
}
