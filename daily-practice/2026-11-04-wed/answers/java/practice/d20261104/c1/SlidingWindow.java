package practice.d20261104.c1;

import java.util.Arrays;

/**
 * Window [l, r] with an invariant that is repaired by moving l; every index enters and leaves once: O(n).
 * longestUnique: remember the last index of each char; on a repeat inside the window jump l to last + 1.
 * longestRepeatingReplacement: window is valid while (size - count of the most frequent letter) <= k. maxFreq never needs to shrink: the answer only grows when maxFreq grows.
 * shortestSubarrayAtLeast: all numbers positive, so the sum is monotone in the window; shrink while the sum still reaches the target. Use long for the sum.
 */
public final class SlidingWindow {
    private SlidingWindow() {}

    public static int longestUnique(String s) {
        int[] last = new int[65536];
        Arrays.fill(last, -1);
        int best = 0, l = 0;
        for (int r = 0; r < s.length(); r++) {
            char c = s.charAt(r);
            if (last[c] >= l) l = last[c] + 1;
            last[c] = r;
            best = Math.max(best, r - l + 1);
        }
        return best;
    }

    public static int longestRepeatingReplacement(String s, int k) {
        int[] cnt = new int[26];
        int l = 0, maxFreq = 0, best = 0;
        for (int r = 0; r < s.length(); r++) {
            maxFreq = Math.max(maxFreq, ++cnt[s.charAt(r) - 'A']);
            while (r - l + 1 - maxFreq > k) cnt[s.charAt(l++) - 'A']--;
            best = Math.max(best, r - l + 1);
        }
        return best;
    }

    public static int shortestSubarrayAtLeast(long target, int[] a) {
        long sum = 0;
        int l = 0, best = Integer.MAX_VALUE;
        for (int r = 0; r < a.length; r++) {
            sum += a[r];
            while (sum >= target && l <= r) {
                best = Math.min(best, r - l + 1);
                sum -= a[l++];
            }
        }
        return best == Integer.MAX_VALUE ? 0 : best;
    }
}
