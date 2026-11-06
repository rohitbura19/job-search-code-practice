package practice.d20261106.c1;

/** Versioned key-value store. For one key, set() is called with non-decreasing timestamps (equal timestamp: the later set wins). */
public class TimeMap {
    public void set(String key, String value, long timestamp) { throw new UnsupportedOperationException("TODO"); }

    /** Value with the greatest timestamp <= the given one; "" if the key is unknown or every version is later. get must be O(log k). */
    public String get(String key, long timestamp) { throw new UnsupportedOperationException("TODO"); }
}
