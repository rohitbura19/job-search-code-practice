package practice.d20261031.a1;

import java.util.HashMap;
import java.util.Map;

/**
 * containsNearbyDuplicate: map value -> last index seen; a hit within k wins. O(n) time, O(min(n, k)) if you evict, O(n) as written.
 * maxProfitOnce: track the lowest price so far and the best (price - low). O(n), O(1).
 * maxProfitMany: every positive day-to-day rise is collectable (unlimited transactions), so sum the positive deltas. O(n), O(1); long avoids overflow.
 */
public final class ArrayBasics {
    private ArrayBasics() {}

    public static boolean containsNearbyDuplicate(int[] nums, int k) {
        Map<Integer, Integer> last = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            Integer j = last.put(nums[i], i);
            if (j != null && i - j <= k) return true;
        }
        return false;
    }

    public static int maxProfitOnce(int[] prices) {
        int low = Integer.MAX_VALUE, best = 0;
        for (int p : prices) {
            low = Math.min(low, p);
            best = Math.max(best, p - low);
        }
        return best;
    }

    public static long maxProfitMany(int[] prices) {
        long sum = 0;
        for (int i = 1; i < prices.length; i++) if (prices[i] > prices[i - 1]) sum += (long) prices[i] - prices[i - 1];
        return sum;
    }
}
