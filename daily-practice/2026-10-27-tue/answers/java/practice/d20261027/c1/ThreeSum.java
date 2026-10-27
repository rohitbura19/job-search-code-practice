package practice.d20261027.c1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Sort, fix the smallest element i, then two pointers on the rest. Skip equal values at i and, after a hit, at both
 * pointers, so no duplicate triplet is produced. Sorting makes the output lexicographic for free.
 * O(n^2) time, O(1) extra (besides the output and the sort). Closest: same scan, keep the best |sum - target|.
 */
public final class ThreeSum {
    private ThreeSum() {}

    public static List<List<Integer>> threeSum(int[] nums) {
        int[] a = nums.clone();
        Arrays.sort(a);
        List<List<Integer>> out = new ArrayList<>();
        for (int i = 0; i + 2 < a.length && a[i] <= 0; i++) {
            if (i > 0 && a[i] == a[i - 1]) continue;
            int l = i + 1, r = a.length - 1;
            while (l < r) {
                int s = a[i] + a[l] + a[r];
                if (s < 0) l++;
                else if (s > 0) r--;
                else {
                    out.add(List.of(a[i], a[l], a[r]));
                    while (l < r && a[l] == a[l + 1]) l++;
                    while (l < r && a[r] == a[r - 1]) r--;
                    l++;
                    r--;
                }
            }
        }
        return out;
    }

    public static int threeSumClosest(int[] nums, int target) {
        int[] a = nums.clone();
        Arrays.sort(a);
        long best = (long) a[0] + a[1] + a[2];
        for (int i = 0; i + 2 < a.length; i++) {
            int l = i + 1, r = a.length - 1;
            while (l < r) {
                long s = (long) a[i] + a[l] + a[r];
                if (Math.abs(s - target) < Math.abs(best - target)) best = s;
                if (s == target) return (int) s;
                if (s < target) l++; else r--;
            }
        }
        return (int) best;
    }
}
