package practice.d20261028.c1;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Trie over characters (children in a map, space is a normal char). Each node keeps the number of hits when a sentence ends there.
 * suggest: walk to the prefix node, DFS the subtree, keep the best 3 in a size-3 min-heap: O(subtree size * log 3).
 * Follow-up (see README review): cache top-3 per node, updated on record(), for O(len) suggest.
 */
public class Autocomplete {
    private static final class Node {
        final Map<Character, Node> next = new HashMap<>();
        int count;
    }

    private record Hit(String sentence, int count) {}

    private static final Comparator<Hit> WORSE_FIRST =
            Comparator.comparingInt(Hit::count).thenComparing(Hit::sentence, Comparator.reverseOrder());

    private final Node root = new Node();

    public void record(String sentence, int times) {
        Node n = root;
        for (int i = 0; i < sentence.length(); i++) n = n.next.computeIfAbsent(sentence.charAt(i), c -> new Node());
        n.count += times;
    }

    public List<String> suggest(String prefix) {
        Node n = root;
        for (int i = 0; i < prefix.length(); i++) {
            n = n.next.get(prefix.charAt(i));
            if (n == null) return List.of();
        }
        PriorityQueue<Hit> heap = new PriorityQueue<>(WORSE_FIRST);
        StringBuilder sb = new StringBuilder(prefix);
        collect(n, sb, heap);
        List<String> out = new ArrayList<>();
        while (!heap.isEmpty()) out.add(heap.poll().sentence());
        java.util.Collections.reverse(out);
        return out;
    }

    private void collect(Node n, StringBuilder sb, PriorityQueue<Hit> heap) {
        if (n.count > 0) {
            heap.add(new Hit(sb.toString(), n.count));
            if (heap.size() > 3) heap.poll();
        }
        for (Map.Entry<Character, Node> e : n.next.entrySet()) {
            sb.append(e.getKey());
            collect(e.getValue(), sb, heap);
            sb.setLength(sb.length() - 1);
        }
    }
}
