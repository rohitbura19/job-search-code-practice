package practice.d20261106.d1;

/**
 * Pigeonhole / bucket idea. With n numbers in [min, max] the max gap is at least ceil((max-min)/(n-1)) =: size, so the two
 * numbers forming it cannot lie in the same bucket of width `size`. Hence only each bucket's min and max matter: the answer is
 * max over consecutive NON-EMPTY buckets of (next.min - prev.max). O(n) time, O(n) space, no sorting.
 * All arithmetic is in long (max - min can exceed int).
 */
public class MaximumGap {
    public static int maximumGap(int[] nums) {
        int n = nums.length;
        if (n < 2) return 0;
        long min = Long.MAX_VALUE, max = Long.MIN_VALUE;
        for (int v : nums) {
            min = Math.min(min, v);
            max = Math.max(max, v);
        }
        if (min == max) return 0;
        long size = Math.max(1, (max - min) / (n - 1));
        int buckets = (int) ((max - min) / size) + 1;
        long[] lo = new long[buckets], hi = new long[buckets];
        java.util.Arrays.fill(lo, Long.MAX_VALUE);
        java.util.Arrays.fill(hi, Long.MIN_VALUE);
        for (int v : nums) {
            int b = (int) ((v - min) / size);
            lo[b] = Math.min(lo[b], v);
            hi[b] = Math.max(hi[b], v);
        }
        long best = 0, prevMax = min;
        for (int b = 0; b < buckets; b++) {
            if (lo[b] == Long.MAX_VALUE) continue;
            best = Math.max(best, lo[b] - prevMax);
            prevMax = hi[b];
        }
        return (int) Math.min(best, Integer.MAX_VALUE);
    }
}
