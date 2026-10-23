package practice.d20261023.a2;

/**
 * dp[i] = max(dp[i-1], dp[i-2] + a[i]); only two values are needed, so O(1) space. Circle: the first and last cannot both be
 * taken, so answer = max(best on [0, n-2], best on [1, n-1]). O(n) time.
 */
public final class HouseRobber {
    private HouseRobber() {}

    public static long rob(int[] nums) {
        return range(nums, 0, nums.length);
    }

    public static long robCircular(int[] nums) {
        int n = nums.length;
        if (n == 0) return 0;
        if (n == 1) return nums[0];
        return Math.max(range(nums, 0, n - 1), range(nums, 1, n));
    }

    private static long range(int[] a, int from, int to) {
        long take = 0, skip = 0;
        for (int i = from; i < to; i++) {
            long t = skip + a[i];
            skip = Math.max(skip, take);
            take = t;
        }
        return Math.max(take, skip);
    }
}
