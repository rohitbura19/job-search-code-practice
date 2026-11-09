package practice.d20261109.c1;

/**
 * Pattern: the answer lies in [lo, hi] and feasible(x) is monotonic (false...false true...true).
 * Binary search the smallest feasible x: O(n log(range)) time, O(1) space.
 */
public final class BinarySearchOnAnswer {
    private BinarySearchOnAnswer() {}

    /** Smallest speed k such that all piles are eaten within h hours (one pile per hour, ceil(p / k) hours each). h >= piles.length. */
    public static int minEatingSpeed(int[] piles, int h) {
        int lo = 1, hi = 1;
        for (int p : piles) hi = Math.max(hi, p);
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            long hours = 0;
            for (int p : piles) hours += (p + (long) mid - 1) / mid;
            if (hours <= h) hi = mid; else lo = mid + 1;
        }
        return lo;
    }

    /** Minimum ship capacity to carry all packages, in order, within the given days. */
    public static int shipWithinDays(int[] weights, int days) {
        long lo = 0, hi = 0;
        for (int w : weights) { lo = Math.max(lo, w); hi += w; }
        return (int) smallestLargestSum(weights, days, lo, hi);
    }

    /** Split into at most k contiguous parts minimising the largest part sum. Sums can exceed int. */
    public static long splitArray(int[] nums, int k) {
        long lo = 0, hi = 0;
        for (int x : nums) { lo = Math.max(lo, x); hi += x; }
        return smallestLargestSum(nums, k, lo, hi);
    }

    private static long smallestLargestSum(int[] a, int parts, long lo, long hi) {
        while (lo < hi) {
            long mid = lo + (hi - lo) / 2;
            int used = 1;
            long cur = 0;
            for (int x : a) {
                if (cur + x > mid) { used++; cur = 0; }
                cur += x;
            }
            if (used <= parts) hi = mid; else lo = mid + 1;
        }
        return lo;
    }
}
