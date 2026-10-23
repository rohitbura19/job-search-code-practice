package practice.d20261023.c2;

import java.util.Arrays;

/**
 * tails[len-1] = the smallest possible tail of an increasing subsequence of that length; tails stays sorted, so each element
 * replaces the first tail >= it (binary search), or extends the array. Strictness: lower bound (>=), not upper bound.
 * Envelopes: sort by width ascending and, for equal widths, height DESCENDING (so two equal widths can never chain), then LIS on
 * heights. O(n log n) time, O(n) space.
 */
public final class Increasing {
    private Increasing() {}

    public static int lengthOfLIS(int[] nums) {
        int[] tails = new int[nums.length];
        int size = 0;
        for (int x : nums) size = place(tails, size, x);
        return size;
    }

    private static int place(int[] tails, int size, int x) {
        int lo = 0, hi = size;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (tails[mid] < x) lo = mid + 1; else hi = mid;
        }
        tails[lo] = x;
        return lo == size ? size + 1 : size;
    }

    public static int maxEnvelopes(int[][] envelopes) {
        int[][] e = envelopes.clone();
        Arrays.sort(e, (a, b) -> a[0] != b[0] ? Integer.compare(a[0], b[0]) : Integer.compare(b[1], a[1]));
        int[] tails = new int[e.length];
        int size = 0;
        for (int[] env : e) size = place(tails, size, env[1]);
        return size;
    }
}
