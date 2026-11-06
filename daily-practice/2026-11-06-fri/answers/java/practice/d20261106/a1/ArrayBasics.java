package practice.d20261106.a1;

import java.util.HashMap;
import java.util.Map;

/**
 * maxProfit: one pass, track the lowest price so far and the best (price - low). O(n) time, O(1) space.
 * containsNearbyDuplicate: map value -> last index; a hit within k is the answer. O(n) / O(min(n, distinct)).
 * mergeSorted: fill a from the BACK so unread elements of a are never overwritten. O(m+n) / O(1).
 */
public class ArrayBasics {
    public static int maxProfit(int[] prices) {
        int low = Integer.MAX_VALUE, best = 0;
        for (int p : prices) {
            low = Math.min(low, p);
            best = Math.max(best, p - low);
        }
        return best;
    }

    public static boolean containsNearbyDuplicate(int[] nums, int k) {
        Map<Integer, Integer> last = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            Integer j = last.put(nums[i], i);
            if (j != null && (long) i - j <= k) return true;
        }
        return false;
    }

    public static void mergeSorted(int[] a, int m, int[] b, int n) {
        int i = m - 1, j = n - 1, w = m + n - 1;
        while (j >= 0) a[w--] = (i >= 0 && a[i] > b[j]) ? a[i--] : b[j--];
    }
}
