package practice.d20261105.c1;

import java.util.HashMap;
import java.util.Map;

/**
 * Prefix sums: sum(i..j) = P[j+1] - P[i]. Sum == k  <=>  P[j+1] - k was seen as an earlier prefix: count them in a HashMap (seed P[0]=0 once).
 * Balanced binary: treat 0 as -1; equal counts <=> equal prefix values; keep the FIRST index of each prefix, longest = max(i - first).
 * Divisible by k: equal prefix remainders; floorMod keeps remainders in [0,k) for negatives; the count of pairs grows by the number seen before.
 * All O(n) time, O(n) space (O(k) for the last one).
 */
public final class PrefixSums {
    private PrefixSums() {}

    public static long subarraySumEqualsK(int[] a, long k) {
        Map<Long, Integer> seen = new HashMap<>();
        seen.put(0L, 1);
        long sum = 0, count = 0;
        for (int v : a) {
            sum += v;
            count += seen.getOrDefault(sum - k, 0);
            seen.merge(sum, 1, Integer::sum);
        }
        return count;
    }

    public static int longestBalancedBinary(int[] a) {
        Map<Integer, Integer> first = new HashMap<>();
        first.put(0, -1);
        int sum = 0, best = 0;
        for (int i = 0; i < a.length; i++) {
            sum += a[i] == 1 ? 1 : -1;
            Integer j = first.putIfAbsent(sum, i);
            if (j != null) best = Math.max(best, i - j);
        }
        return best;
    }

    public static long subarraysDivisibleByK(int[] a, int k) {
        int[] freq = new int[k];
        freq[0] = 1;
        long sum = 0, count = 0;
        for (int v : a) {
            sum += v;
            int r = (int) Math.floorMod(sum, (long) k);
            count += freq[r]++;
        }
        return count;
    }
}
