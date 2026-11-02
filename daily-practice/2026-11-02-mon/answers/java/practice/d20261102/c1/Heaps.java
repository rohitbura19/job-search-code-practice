package practice.d20261102.c1;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * KthLargest: min-heap of the k largest seen; the root is the answer. add: O(log k).
 * kClosest: max-heap of size k on (dist2, x, y); evict the worst when full. O(n log k), then sort k.
 * mergeSorted: min-heap of (array, position) cursors; O(N log k).
 */
public final class Heaps {
    private Heaps() {}

    public static final class KthLargest {
        private final int k;
        private final PriorityQueue<Integer> heap = new PriorityQueue<>();

        public KthLargest(int k, int[] nums) {
            this.k = k;
            for (int v : nums) offer(v);
        }

        private void offer(int v) {
            if (heap.size() < k) heap.add(v);
            else if (v > heap.peek()) { heap.poll(); heap.add(v); }
        }

        public int add(int val) {
            offer(val);
            return heap.peek();
        }
    }

    private static long d2(int[] p) { return (long) p[0] * p[0] + (long) p[1] * p[1]; }

    private static final Comparator<int[]> NEAR_FIRST =
            Comparator.<int[]>comparingLong(Heaps::d2).thenComparingInt(p -> p[0]).thenComparingInt(p -> p[1]);

    public static int[][] kClosest(int[][] points, int k) {
        PriorityQueue<int[]> worstOnTop = new PriorityQueue<>(NEAR_FIRST.reversed());
        for (int[] p : points) {
            if (worstOnTop.size() < k) worstOnTop.add(p);
            else if (NEAR_FIRST.compare(p, worstOnTop.peek()) < 0) { worstOnTop.poll(); worstOnTop.add(p); }
        }
        int[][] out = worstOnTop.toArray(new int[0][]);
        Arrays.sort(out, NEAR_FIRST);
        return out;
    }

    public static int[] mergeSorted(int[][] arrays) {
        int total = 0;
        for (int[] a : arrays) total += a.length;
        int[] out = new int[total];
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(c -> arrays[c[0]][c[1]]));
        for (int i = 0; i < arrays.length; i++) if (arrays[i].length > 0) pq.add(new int[]{i, 0});
        int n = 0;
        while (!pq.isEmpty()) {
            int[] c = pq.poll();
            out[n++] = arrays[c[0]][c[1]];
            if (++c[1] < arrays[c[0]].length) pq.add(c);
        }
        return out;
    }
}
