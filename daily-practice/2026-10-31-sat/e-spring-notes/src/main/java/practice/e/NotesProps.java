package practice.e;

/**
 * TODO: immutable @ConfigurationProperties record, prefix "notes", validated.
 *   maxPerOwner: default 100, must be >= 1;  maxPageSize: default 50, must be >= 1.
 * Defaults must apply when nothing is configured; invalid values must stop the context.
 */
public record NotesProps(int maxPerOwner, int maxPageSize) {}
