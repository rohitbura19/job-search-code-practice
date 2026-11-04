package practice.d20261104.g;

import java.util.Map;
import java.util.Set;

/**
 * One feature flag. Evaluation order lives in FeatureFlags; this record is the immutable definition.
 * key: non-blank. enabled: kill switch. allowUsers: always on for these users. attrRules: attribute -> allowed values (ALL attributes must match).
 * percent: 0..100 rollout share.
 */
public record Flag(String key, boolean enabled, Set<String> allowUsers, Map<String, Set<String>> attrRules, int percent) {
    // TODO compact constructor: key null -> NullPointerException, blank -> IllegalArgumentException; percent outside 0..100 -> IllegalArgumentException;
    //  null allowUsers / attrRules become empty; store DEFENSIVE IMMUTABLE COPIES (also of the inner sets) so later changes by the caller cannot leak in.

    /** Given: fully on for everybody. */
    public static Flag on(String key) { return new Flag(key, true, Set.of(), Map.of(), 100); }

    /** Given: on for a percentage of users. */
    public static Flag rollout(String key, int percent) { return new Flag(key, true, Set.of(), Map.of(), percent); }
}
