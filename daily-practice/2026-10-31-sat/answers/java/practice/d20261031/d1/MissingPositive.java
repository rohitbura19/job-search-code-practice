package practice.d20261031.d1;

/**
 * With n numbers the answer is in [1, n+1] (pigeonhole). Use the array itself as a presence table: put every value v in
 * [1, n] at index v-1 by swapping (cycle sort). Each swap places one value permanently, so the total is O(n) even though
 * there is a nested loop. The first index i with nums[i] != i+1 gives the answer i+1; if none, n+1.
 * The duplicate check nums[nums[i]-1] != nums[i] is what stops an infinite loop on {1, 1}.
 * Alternative: sign-marking (negate nums[v-1]); same bounds, but needs a clean-up pass for non-positives. Time O(n), space O(1).
 */
public final class MissingPositive {
    private MissingPositive() {}

    public static int firstMissingPositive(int[] nums) {
        int n = nums.length;
        for (int i = 0; i < n; i++) {
            while (nums[i] >= 1 && nums[i] <= n && nums[nums[i] - 1] != nums[i]) {
                int j = nums[i] - 1, t = nums[j];
                nums[j] = nums[i];
                nums[i] = t;
            }
        }
        for (int i = 0; i < n; i++) if (nums[i] != i + 1) return i + 1;
        return n + 1;
    }
}
