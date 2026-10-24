package practice.d20261024.g;

import java.nio.charset.StandardCharsets;
import java.util.zip.CRC32;

/**
 * Sticky bucketing: bucket = CRC32(key + ":" + userId) mod 100. Hashing the key too gives each flag an independent split of the
 * users; the same user always lands in the same bucket, and raising the percentage only adds users (20% set is a subset of 50%).
 * String.hashCode would also be stable but spreads similar ids poorly. O(len) per call, one map lookup, no allocation of state.
 */
public final class DefaultFlagEvaluator implements FlagEvaluator {
    private final FlagStore store;

    public DefaultFlagEvaluator(FlagStore store) { this.store = store; }

    @Override public boolean isEnabled(String key, String userId) {
        if (userId == null || userId.isBlank()) throw new IllegalArgumentException("userId");
        return store.find(key).map(f -> decide(f, userId)).orElse(false);
    }

    private static boolean decide(Flag f, String userId) {
        if (!f.enabled()) return false;
        if (f.allowList().contains(userId)) return true;
        return bucket(f.key(), userId) < f.rolloutPercent();
    }

    static int bucket(String key, String userId) {
        CRC32 crc = new CRC32();
        crc.update((key + ":" + userId).getBytes(StandardCharsets.UTF_8));
        return (int) (crc.getValue() % 100);
    }
}
