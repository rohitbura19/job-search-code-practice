package practice.d20261109.c2;

/** All O(n) time, O(1) space. */
public final class KadaneFamily {
    private KadaneFamily() {}

    /** Best sum of a non-empty contiguous subarray: cur = max(x, cur + x). */
    public static long maxSubarraySum(int[] a) {
        long best = a[0], cur = a[0];
        for (int i = 1; i < a.length; i++) {
            cur = Math.max(a[i], cur + a[i]);
            best = Math.max(best, cur);
        }
        return best;
    }

    /** Track both max and min product ending here (a negative flips them). */
    public static long maxProductSubarray(int[] a) {
        long max = a[0], min = a[0], best = a[0];
        for (int i = 1; i < a.length; i++) {
            long x = a[i], hi = Math.max(x, Math.max(max * x, min * x)), lo = Math.min(x, Math.min(max * x, min * x));
            max = hi; min = lo;
            best = Math.max(best, max);
        }
        return best;
    }

    /** Circular: max(best normal, total - min normal), except when every element is negative (the wrap would be empty). */
    public static long maxCircularSubarraySum(int[] a) {
        long total = 0, curMax = 0, curMin = 0, best = a[0], worst = a[0];
        for (int x : a) {
            total += x;
            curMax = Math.max(x, curMax + x); best = Math.max(best, curMax);
            curMin = Math.min(x, curMin + x); worst = Math.min(worst, curMin);
        }
        return best < 0 ? best : Math.max(best, total - worst);
    }
}
