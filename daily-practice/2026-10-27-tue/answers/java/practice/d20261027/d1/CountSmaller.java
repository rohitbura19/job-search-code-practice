package practice.d20261027.d1;

import java.util.Arrays;

/**
 * Fenwick tree (binary indexed tree) over the ranks of the values (coordinate compression handles huge or negative values
 * without overflow). Scan right to left: answer[i] = number of already inserted values with a smaller rank, then insert nums[i].
 * O(n log n) time, O(n) space. Inversions = sum of the counts (use long). A merge sort that counts while merging is the
 * alternative with the same bound and no tree.
 */
public final class CountSmaller {
    private CountSmaller() {}

    public static int[] countSmaller(int[] nums) {
        int n = nums.length;
        int[] sorted = nums.clone();
        Arrays.sort(sorted);
        int m = 0;
        for (int i = 0; i < n; i++) if (i == 0 || sorted[i] != sorted[i - 1]) sorted[m++] = sorted[i];
        int[] tree = new int[m + 1];
        int[] res = new int[n];
        for (int i = n - 1; i >= 0; i--) {
            int rank = Arrays.binarySearch(sorted, 0, m, nums[i]) + 1;
            int c = 0;
            for (int k = rank - 1; k > 0; k -= k & -k) c += tree[k];
            res[i] = c;
            for (int k = rank; k <= m; k += k & -k) tree[k]++;
        }
        return res;
    }

    public static long inversions(int[] nums) {
        long total = 0;
        for (int c : countSmaller(nums)) total += c;
        return total;
    }
}
