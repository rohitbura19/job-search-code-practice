package practice.d20261110.g;

/** Given. Thrown when a participant already has an overlapping event. */
public class ConflictException extends RuntimeException {
    private final String user;
    private final String conflictingEventId;

    public ConflictException(String user, String conflictingEventId) {
        super(user + " is busy (" + conflictingEventId + ")");
        this.user = user;
        this.conflictingEventId = conflictingEventId;
    }
    public String user() { return user; }
    public String conflictingEventId() { return conflictingEventId; }
}
