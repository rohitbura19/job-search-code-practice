package practice.d20261024.g;

import java.util.Optional;

/** Source of flag definitions. Implementations must be safe for concurrent readers and writers. */
public interface FlagStore {
    Optional<Flag> find(String key);

    /** Insert or replace; readers see either the old or the new definition, never a mix. */
    void put(Flag flag);
}
