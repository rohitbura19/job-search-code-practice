package practice.d20261023.c1;

import java.util.*;

/**
 * Count with a HashMap, then keep a min-heap of size k whose top is the WORST of the kept entries (lowest count, and among equal
 * counts the lexicographically largest word). Each of the d distinct words costs O(log k). O(n + d log k) time, O(d) space.
 */
public final class TopKWords {
    private TopKWords() {}

    public static List<String> topKFrequent(String[] words, int k) {
        Map<String, Integer> count = new HashMap<>();
        for (String w : words) count.merge(w, 1, Integer::sum);
        Comparator<Map.Entry<String, Integer>> worstFirst = Map.Entry.<String, Integer>comparingByValue()
                .thenComparing(Map.Entry.comparingByKey(Comparator.reverseOrder()));
        PriorityQueue<Map.Entry<String, Integer>> heap = new PriorityQueue<>(k + 1, worstFirst);
        for (Map.Entry<String, Integer> e : count.entrySet()) {
            heap.offer(e);
            if (heap.size() > k) heap.poll();
        }
        String[] out = new String[heap.size()];
        for (int i = out.length - 1; i >= 0; i--) out[i] = heap.poll().getKey();
        return Arrays.asList(out);
    }
}
