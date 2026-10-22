package practice.d20261013.g;

/** GIVEN. Strategy: decides from the statistics whether the campaign must abort. */
public interface AbortPolicy {
    boolean shouldAbort(Stats s);
}
