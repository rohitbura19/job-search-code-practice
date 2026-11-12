package practice.d20261112.g;

import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Trie of normalised queries with a count on terminal nodes. suggest(): walk to the prefix node, then DFS its subtree keeping a bounded
 * min-heap of size k: O(L + S log k) where S is the subtree size. Reads share a read lock, writes (record/block) take the write lock.
 * Production variant: cache the top-k list in every node (O(L) reads, O(L * k) updates), see README review.
 */
public class AutocompleteService {
    private static final class Node {
        final TreeMap<Character, Node> kids = new TreeMap<>();
        long count;
        boolean blocked;
        boolean terminal;
    }

    private record Hit(String query, long count) {}

    private final int maxQueryLength;
    private final Node root = new Node();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private int distinct;

    public AutocompleteService(int maxQueryLength) {
        if (maxQueryLength < 1) throw new IllegalArgumentException("maxQueryLength");
        this.maxQueryLength = maxQueryLength;
    }

    static String normalise(String s) {
        return s == null ? "" : s.trim().replaceAll("\\s+", " ").toLowerCase(Locale.ROOT);
    }

    public boolean record(String query) {
        String q = normalise(query);
        if (q.isEmpty() || q.length() > maxQueryLength) return false;
        lock.writeLock().lock();
        try {
            Node n = root;
            for (int i = 0; i < q.length(); i++) n = n.kids.computeIfAbsent(q.charAt(i), c -> new Node());
            if (!n.terminal) { n.terminal = true; distinct++; }
            n.count++;
            return true;
        } finally { lock.writeLock().unlock(); }
    }

    public void block(String query) { setBlocked(query, true); }

    public void unblock(String query) { setBlocked(query, false); }

    private void setBlocked(String query, boolean value) {
        String q = normalise(query);
        lock.writeLock().lock();
        try {
            Node n = find(q);
            if (n != null && n.terminal) n.blocked = value;
        } finally { lock.writeLock().unlock(); }
    }

    public long count(String query) {
        lock.readLock().lock();
        try {
            Node n = find(normalise(query));
            return n == null ? 0 : n.count;
        } finally { lock.readLock().unlock(); }
    }

    public int distinctQueries() {
        lock.readLock().lock();
        try { return distinct; } finally { lock.readLock().unlock(); }
    }

    public List<String> suggest(String prefix, int k) {
        if (k <= 0) return List.of();
        String p = normalise(prefix);
        lock.readLock().lock();
        try {
            Node start = find(p);
            if (start == null) return List.of();
            // min-heap: the weakest hit (lowest count, then alphabetically last) sits on top and is evicted first
            PriorityQueue<Hit> heap = new PriorityQueue<>(Comparator.comparingLong(Hit::count).thenComparing(Hit::query, Comparator.reverseOrder()));
            collect(start, new StringBuilder(p), heap, k);
            List<Hit> hits = new ArrayList<>(heap);
            hits.sort(Comparator.comparingLong(Hit::count).reversed().thenComparing(Hit::query));
            return hits.stream().map(Hit::query).toList();
        } finally { lock.readLock().unlock(); }
    }

    private void collect(Node n, StringBuilder path, PriorityQueue<Hit> heap, int k) {
        if (n.terminal && !n.blocked) {
            heap.add(new Hit(path.toString(), n.count));
            if (heap.size() > k) heap.poll();
        }
        for (Map.Entry<Character, Node> e : n.kids.entrySet()) {
            path.append(e.getKey());
            collect(e.getValue(), path, heap, k);
            path.setLength(path.length() - 1);
        }
    }

    private Node find(String q) {
        Node n = root;
        for (int i = 0; i < q.length() && n != null; i++) n = n.kids.get(q.charAt(i));
        return n;
    }
}
