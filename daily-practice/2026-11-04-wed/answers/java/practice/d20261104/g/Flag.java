package practice.d20261104.g;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/** Immutable flag definition: validation plus deep immutable copies in the compact constructor. */
public record Flag(String key, boolean enabled, Set<String> allowUsers, Map<String, Set<String>> attrRules, int percent) {
    public Flag {
        Objects.requireNonNull(key, "key");
        if (key.isBlank()) throw new IllegalArgumentException("blank key");
        if (percent < 0 || percent > 100) throw new IllegalArgumentException("percent " + percent);
        allowUsers = allowUsers == null ? Set.of() : Set.copyOf(allowUsers);
        Map<String, Set<String>> copy = new HashMap<>();
        if (attrRules != null) attrRules.forEach((k, v) -> copy.put(k, Set.copyOf(v)));
        attrRules = Map.copyOf(copy);
    }

    public static Flag on(String key) { return new Flag(key, true, Set.of(), Map.of(), 100); }

    public static Flag rollout(String key, int percent) { return new Flag(key, true, Set.of(), Map.of(), percent); }
}
