package practice.e;

import java.util.UUID;

/** GIVEN - thrown by the service. Map it to 404. */
public class CaseNotFoundException extends RuntimeException {
    public CaseNotFoundException(UUID id) { super("case not found: " + id); }
}
