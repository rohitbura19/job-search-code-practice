package practice.d20261024.g;

public interface FlagEvaluator {
    /**
     * Decision order: unknown flag -> false; disabled flag -> false (the kill switch beats the allow-list); user in the allow-list
     * -> true; otherwise bucket(key, userId) < rolloutPercent. A null or blank userId throws IllegalArgumentException.
     */
    boolean isEnabled(String key, String userId);
}
