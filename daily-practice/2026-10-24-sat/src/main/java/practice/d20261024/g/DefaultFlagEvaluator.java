package practice.d20261024.g;

public final class DefaultFlagEvaluator implements FlagEvaluator {
    private final FlagStore store;

    public DefaultFlagEvaluator(FlagStore store) { this.store = store; }

    // TODO: follow the decision order in FlagEvaluator's Javadoc. Bucketing must be deterministic across JVM runs and servers,
    // independent per flag, and must only ADD users when the percentage grows. Do not use Random or Object.hashCode.
    @Override public boolean isEnabled(String key, String userId) { throw new UnsupportedOperationException("TODO"); }
}
