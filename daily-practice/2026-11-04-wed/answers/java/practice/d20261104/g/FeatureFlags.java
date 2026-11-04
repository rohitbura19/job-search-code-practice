package practice.d20261104.g;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Copy-on-write snapshot: the flags live in an immutable Map held in an AtomicReference. Readers do one volatile read and never lock;
 * writers copy-and-swap (updateAndGet retries on contention, fine because writes are rare). replaceAll builds the new map completely before the swap,
 * which is what makes the update atomic for readers. bucket() mixes String.hashCode (murmur3 finalizer) so that sequential ids spread evenly.
 */
public final class FeatureFlags {
    private final AtomicReference<Map<String, Flag>> snapshot = new AtomicReference<>(Map.of());

    public FeatureFlags() {}

    public void put(Flag flag) {
        snapshot.updateAndGet(m -> {
            Map<String, Flag> c = new HashMap<>(m);
            c.put(flag.key(), flag);
            return Map.copyOf(c);
        });
    }

    public void replaceAll(Collection<Flag> flags) {
        Map<String, Flag> c = new HashMap<>();
        for (Flag f : flags) c.put(f.key(), f);
        snapshot.set(Map.copyOf(c));
    }

    public boolean remove(String key) {
        boolean[] removed = {false};
        snapshot.updateAndGet(m -> {
            removed[0] = m.containsKey(key);
            if (!removed[0]) return m;
            Map<String, Flag> c = new HashMap<>(m);
            c.remove(key);
            return Map.copyOf(c);
        });
        return removed[0];
    }

    public Optional<Flag> get(String key) { return Optional.ofNullable(snapshot.get().get(key)); }

    public int size() { return snapshot.get().size(); }

    public boolean isEnabled(String key, Context ctx) { return eval(snapshot.get().get(key), ctx); }

    public Map<String, Boolean> evaluateAll(Context ctx) {
        Map<String, Boolean> out = new HashMap<>();
        snapshot.get().forEach((k, f) -> out.put(k, eval(f, ctx)));
        return out;
    }

    private static boolean eval(Flag f, Context ctx) {
        if (f == null || !f.enabled()) return false;
        String user = ctx.userId();
        if (user != null && f.allowUsers().contains(user)) return true;
        for (Map.Entry<String, java.util.Set<String>> rule : f.attrRules().entrySet()) {
            String v = ctx.attrs().get(rule.getKey());
            if (v == null || !rule.getValue().contains(v)) return false;
        }
        if (f.percent() >= 100) return true;
        if (f.percent() <= 0 || user == null) return false;
        return bucket(f.key(), user) < f.percent() * 100;
    }

    public static int bucket(String key, String userId) {
        int h = (key + ":" + userId).hashCode();
        h ^= h >>> 16; h *= 0x85ebca6b;
        h ^= h >>> 13; h *= 0xc2b2ae35;
        h ^= h >>> 16;
        return Math.floorMod(h, 10_000);
    }
}
