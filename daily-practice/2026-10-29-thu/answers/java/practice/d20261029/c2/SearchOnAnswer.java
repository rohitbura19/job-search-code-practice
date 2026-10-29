package practice.d20261029.c2;

/**
 * "Binary search on the answer": the feasibility predicate is monotone in the candidate value, so find the smallest feasible one.
 * minEatingSpeed: lo = 1, hi = max pile; hours(speed) = sum ceil(p / speed) in long. O(n log max).
 * splitArray: lo = max element, hi = total sum; greedy pack parts while sum <= cap and count the parts. O(n log sum).
 * shipDays-style predicates share the same shape: only `feasible` changes.
 */
public final class SearchOnAnswer {
    private SearchOnAnswer() {}

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

    public static int splitArray(int[] nums, int k) {
        long lo = 0, hi = 0;
        for (int x : nums) { lo = Math.max(lo, x); hi += x; }
        while (lo < hi) {
            long mid = lo + (hi - lo) / 2;
            int parts = 1;
            long cur = 0;
            for (int x : nums) {
                if (cur + x > mid) { parts++; cur = 0; }
                cur += x;
            }
            if (parts <= k) hi = mid; else lo = mid + 1;
        }
        return (int) lo;
    }
}
