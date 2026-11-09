package practice.d20261109.g;

/** Source of unique non-negative ids (a DB sequence, a Snowflake generator, a Redis counter...). Thread-safe. */
public interface IdGenerator {
    long nextId();
}
