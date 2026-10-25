package practice.d20261025.c1;

import java.util.HashMap;
import java.util.Map;

/**
 * productExceptSelf: out[i] first holds the product of everything left of i, then a right-to-left pass multiplies in the product of
 * everything right of i. O(n) time, O(1) extra. Zeros need no special case with this formulation.
 * subarraySum: with prefix sums P, a subarray (i, j] has sum k iff P[j] - P[i] = k, so count earlier prefixes equal to P[j] - k.
 * One pass with a HashMap<prefix, count> seeded with {0: 1}: O(n) time, O(n) space. Sliding window does not work with negatives.
 */
public final class Prefix {
    private Prefix() {}

    public static int[] productExceptSelf(int[] nums) {
        int n = nums.length;
        int[] out = new int[n];
        int left = 1;
        for (int i = 0; i < n; i++) { out[i] = left; left *= nums[i]; }
        int right = 1;
        for (int i = n - 1; i >= 0; i--) { out[i] *= right; right *= nums[i]; }
        return out;
    }

    public static int subarraySum(int[] nums, long k) {
        Map<Long, Integer> seen = new HashMap<>();
        seen.put(0L, 1);
        long sum = 0;
        int count = 0;
        for (int x : nums) {
            sum += x;
            count += seen.getOrDefault(sum - k, 0);
            seen.merge(sum, 1, Integer::sum);
        }
        return count;
    }
}
