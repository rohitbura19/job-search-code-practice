package practice.d20261024.g;

import java.util.Optional;

public final class InMemoryFlagStore implements FlagStore {
    // TODO: pick a thread-safe structure; Flag is immutable, so replacing a whole definition must be atomic.

    @Override public Optional<Flag> find(String key) { throw new UnsupportedOperationException("TODO"); }

    @Override public void put(Flag flag) { throw new UnsupportedOperationException("TODO"); }
}
