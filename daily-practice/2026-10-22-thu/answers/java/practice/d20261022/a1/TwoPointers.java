package practice.d20261022.a1;

import java.util.Arrays;

/**
 * Both are "slow writer, fast reader". O(n) time, O(1) space.
 * moveZeroes: write every non-zero forward, then zero-fill the tail (fewer writes than swapping).
 * removeDuplicates: allow x when fewer than 2 kept, or when x differs from the value 2 slots behind the writer.
 * Comparing with nums[w-2] (not nums[i-2]) matters because the prefix has already been overwritten.
 * For "at most K" copies use nums[w-K].
 */
public final class TwoPointers {
    private TwoPointers() {}

    public static void moveZeroes(int[] nums) {
        int w = 0;
        for (int x : nums) {
            if (x != 0) nums[w++] = x;
        }
        Arrays.fill(nums, w, nums.length, 0);
    }

    public static int removeDuplicates(int[] nums) {
        int w = 0;
        for (int x : nums) {
            if (w < 2 || x != nums[w - 2]) nums[w++] = x;
        }
        return w;
    }
}
