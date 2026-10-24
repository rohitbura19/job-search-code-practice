package practice.d20261024.g;

import java.util.Set;

/** An immutable flag definition. rolloutPercent is 0..100; allowList users are on whenever the flag is enabled. */
public record Flag(String key, boolean enabled, int rolloutPercent, Set<String> allowList) {
    public Flag {
        if (key == null || key.isBlank()) throw new IllegalArgumentException("key");
        if (rolloutPercent < 0 || rolloutPercent > 100) throw new IllegalArgumentException("rolloutPercent");
        allowList = Set.copyOf(allowList);
    }

    public static Flag on(String key) { return new Flag(key, true, 100, Set.of()); }

    public static Flag rollout(String key, int percent) { return new Flag(key, true, percent, Set.of()); }
}
