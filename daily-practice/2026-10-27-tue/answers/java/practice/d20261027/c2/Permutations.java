package practice.d20261027.c2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * permuteUnique: sort, then backtrack choosing one unused element per position. To avoid duplicates, skip a[i] if it equals
 * a[i-1] and a[i-1] is not used (the equal twin on the left must be placed first, so each multiset arrangement appears once).
 * Sorting makes the output lexicographic. O(n * P) time for P results, O(n) recursion.
 * nextPermutation: find the rightmost ascent i (a[i] < a[i+1]); swap a[i] with the rightmost element greater than it; reverse
 * the suffix (it was descending, now ascending = smallest). No ascent -> reverse everything. O(n) time, O(1) space.
 */
public final class Permutations {
    private Permutations() {}

    public static List<List<Integer>> permuteUnique(int[] nums) {
        int[] a = nums.clone();
        Arrays.sort(a);
        List<List<Integer>> out = new ArrayList<>();
        build(a, new boolean[a.length], new ArrayList<>(), out);
        return out;
    }

    private static void build(int[] a, boolean[] used, List<Integer> cur, List<List<Integer>> out) {
        if (cur.size() == a.length) {
            out.add(new ArrayList<>(cur));
            return;
        }
        for (int i = 0; i < a.length; i++) {
            if (used[i] || (i > 0 && a[i] == a[i - 1] && !used[i - 1])) continue;
            used[i] = true;
            cur.add(a[i]);
            build(a, used, cur, out);
            cur.remove(cur.size() - 1);
            used[i] = false;
        }
    }

    public static void nextPermutation(int[] a) {
        int i = a.length - 2;
        while (i >= 0 && a[i] >= a[i + 1]) i--;
        if (i >= 0) {
            int j = a.length - 1;
            while (a[j] <= a[i]) j--;
            swap(a, i, j);
        }
        for (int l = i + 1, r = a.length - 1; l < r; l++, r--) swap(a, l, r);
    }

    private static void swap(int[] a, int i, int j) {
        int t = a[i];
        a[i] = a[j];
        a[j] = t;
    }
}
