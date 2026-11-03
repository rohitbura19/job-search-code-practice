package practice.d20261103.b1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * merge/compute replace the get-null-check-put dance with one hash lookup and no race window on a ConcurrentHashMap.
 * merge(k, v, f): absent -> put v; present -> f(old, v); a null result REMOVES the entry. compute is the general form.
 * removeIf on an ArrayList is one O(n) pass; removing in a for-each loop throws ConcurrentModificationException.
 */
public final class MapApi {
    private MapApi() {}

    public static Map<String, Integer> wordCounts(List<String> words) {
        Map<String, Integer> m = new HashMap<>();
        for (String w : words) m.merge(w, 1, Integer::sum);
        return m;
    }

    public static SortedMap<Character, List<String>> byInitial(List<String> words) {
        SortedMap<Character, List<String>> m = new TreeMap<>();
        for (String w : words) m.computeIfAbsent(Character.toLowerCase(w.charAt(0)), k -> new ArrayList<>()).add(w);
        return m;
    }

    public static int removeShort(List<String> words, int minLen) {
        int before = words.size();
        words.removeIf(w -> w.length() < minLen);
        return before - words.size();
    }

    public static Map<String, Integer> mergeAll(List<Map<String, Integer>> maps) {
        Map<String, Integer> out = new HashMap<>();
        for (Map<String, Integer> m : maps)
            m.forEach((k, v) -> out.merge(k, v, (a, b) -> a + b == 0 ? null : a + b));
        return out;
    }

    public static Map<Integer, List<String>> invert(Map<String, Integer> counts) {
        Map<Integer, List<String>> out = new TreeMap<>();
        counts.forEach((k, v) -> out.computeIfAbsent(v, x -> new ArrayList<>()).add(k));
        out.values().forEach(java.util.Collections::sort);
        return out;
    }

    public static List<String> topK(Map<String, Integer> counts, int k) {
        return counts.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed().thenComparing(Map.Entry.comparingByKey()))
                .limit(k).map(Map.Entry::getKey).collect(Collectors.toList());
    }
}
