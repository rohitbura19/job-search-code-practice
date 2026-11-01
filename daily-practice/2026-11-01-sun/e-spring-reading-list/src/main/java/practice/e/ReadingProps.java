package practice.e;

/**
 * TODO: immutable @ConfigurationProperties record, prefix "reading", validated.
 *   maxTags: default 5, must be >= 1;  maxPageSize: default 50, must be >= 1.
 * Defaults must apply when nothing is configured; invalid values must stop the context. (The app class has @ConfigurationPropertiesScan.)
 */
public record ReadingProps(int maxTags, int maxPageSize) {}
