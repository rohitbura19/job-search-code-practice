package practice.d20261013.b1;

import java.util.LinkedHashMap;

/**
 * LRU cache in a few lines on top of LinkedHashMap (access order). Capacity cap: inserting entry cap+1 evicts the least recently used.
 * Both get() and put() of an existing key count as a use. Not thread-safe (say why).
 */
public class Lru<K, V> extends LinkedHashMap<K, V> {
    public Lru(int cap) {
        super(16, 0.75f, true);
        throw new UnsupportedOperationException("TODO");
    }
}
