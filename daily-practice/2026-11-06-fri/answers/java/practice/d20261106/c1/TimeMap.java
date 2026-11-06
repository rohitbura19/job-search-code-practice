package practice.d20261106.c1;

import java.util.*;

/**
 * key -> parallel lists of timestamps (non-decreasing per key, as promised) and values. get = binary search for the
 * greatest timestamp <= t. set O(1), get O(log k).
 */
public class TimeMap {
    private final Map<String, List<Long>> times = new HashMap<>();
    private final Map<String, List<String>> values = new HashMap<>();

    public void set(String key, String value, long timestamp) {
        times.computeIfAbsent(key, k -> new ArrayList<>()).add(timestamp);
        values.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
    }

    public String get(String key, long timestamp) {
        List<Long> ts = times.get(key);
        if (ts == null) return "";
        int lo = 0, hi = ts.size();
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (ts.get(mid) <= timestamp) lo = mid + 1; else hi = mid;
        }
        return lo == 0 ? "" : values.get(key).get(lo - 1);
    }
}
