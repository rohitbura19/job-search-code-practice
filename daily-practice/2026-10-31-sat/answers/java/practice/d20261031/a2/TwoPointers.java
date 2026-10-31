package practice.d20261031.a2;

/**
 * twoSumSorted: lo/hi pointers; sum too small -> lo++, too big -> hi--. Each step discards one index that cannot be in any
 * answer with the remaining range. O(n), O(1). Sum in long (two ints can overflow).
 * maxArea: start with the widest pair; the shorter wall limits the area, and moving the taller wall inward can only shrink
 * width without raising the limit, so always move the shorter one. O(n), O(1).
 */
public final class TwoPointers {
    private TwoPointers() {}

    public static int[] twoSumSorted(int[] sorted, long target) {
        int lo = 0, hi = sorted.length - 1;
        while (lo < hi) {
            long s = (long) sorted[lo] + sorted[hi];
            if (s == target) return new int[] {lo + 1, hi + 1};
            if (s < target) lo++; else hi--;
        }
        return new int[0];
    }

    public static long maxArea(int[] h) {
        int lo = 0, hi = h.length - 1;
        long best = 0;
        while (lo < hi) {
            best = Math.max(best, (long) Math.min(h[lo], h[hi]) * (hi - lo));
            if (h[lo] < h[hi]) lo++; else hi--;
        }
        return best;
    }
}
