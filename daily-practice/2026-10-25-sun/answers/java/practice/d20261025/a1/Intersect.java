package practice.d20261025.a1;

import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;

/**
 * Hash counts: count the shorter array, walk the longer one and consume counts -> O(n + m) time, O(min(n, m)) space; sort the result
 * for the contract. Sorted inputs: two pointers, advance the smaller side, emit on equality -> O(n + m) time, O(1) space, already ascending.
 * If one array is on disk: count the small one in memory and stream the big one.
 */
public final class Intersect {
    private Intersect() {}

    public static int[] intersect(int[] a, int[] b) {
        int[] small = a.length <= b.length ? a : b, big = small == a ? b : a;
        Map<Integer, Integer> cnt = new HashMap<>();
        for (int x : small) cnt.merge(x, 1, Integer::sum);
        int[] out = new int[small.length];
        int k = 0;
        for (int x : big) {
            Integer c = cnt.get(x);
            if (c != null && c > 0) { out[k++] = x; cnt.put(x, c - 1); }
        }
        out = Arrays.copyOf(out, k);
        Arrays.sort(out);
        return out;
    }

    public static int[] intersectSorted(int[] a, int[] b) {
        int[] out = new int[Math.min(a.length, b.length)];
        int i = 0, j = 0, k = 0;
        while (i < a.length && j < b.length) {
            if (a[i] < b[j]) i++;
            else if (a[i] > b[j]) j++;
            else { out[k++] = a[i]; i++; j++; }
        }
        return Arrays.copyOf(out, k);
    }
}
