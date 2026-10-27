package practice.d20261027.a1;

/**
 * Kadane: best sum ending at i is max(a[i], best ending at i-1 + a[i]); answer = max over i. O(n) time, O(1) space.
 * Product: a negative flips max and min, so track both the largest and smallest product ending at i; a zero resets them
 * (the max(x, ...) term restarts the run at x). O(n) time, O(1) space.
 */
public final class Subarrays {
    private Subarrays() {}

    public static int maxSubarray(int[] a) {
        int cur = a[0], best = a[0];
        for (int i = 1; i < a.length; i++) {
            cur = Math.max(a[i], cur + a[i]);
            best = Math.max(best, cur);
        }
        return best;
    }

    public static int maxProduct(int[] a) {
        int hi = a[0], lo = a[0], best = a[0];
        for (int i = 1; i < a.length; i++) {
            int x = a[i];
            int h = Math.max(x, Math.max(hi * x, lo * x));
            int l = Math.min(x, Math.min(hi * x, lo * x));
            hi = h;
            lo = l;
            best = Math.max(best, hi);
        }
        return best;
    }
}
