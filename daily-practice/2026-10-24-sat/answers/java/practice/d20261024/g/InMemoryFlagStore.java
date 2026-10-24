package practice.d20261024.g;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/** Flag is an immutable record, so a ConcurrentHashMap gives atomic replace with no extra locking. */
public final class InMemoryFlagStore implements FlagStore {
    private final ConcurrentHashMap<String, Flag> flags = new ConcurrentHashMap<>();

    @Override public Optional<Flag> find(String key) { return Optional.ofNullable(flags.get(key)); }

    @Override public void put(Flag flag) { flags.put(flag.key(), flag); }
}
