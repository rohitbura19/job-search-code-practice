package practice.d20261110.a1;

import java.util.*;

/**
 * sortedSquares: two pointers fill the result from the back with the larger absolute value, O(n) time/space.
 * intersect: multiset intersection with a count map over the smaller array, O(n + m); sort-based two pointers is O(n log n) with O(1) extra.
 * rotateRight: three reversals, O(n) time, O(1) space; k is reduced mod n.
 */
public class ArrayBasics {
    public static int[] sortedSquares(int[] a) {
        int n = a.length;
        int[] r = new int[n];
        int l = 0, h = n - 1;
        for (int i = n - 1; i >= 0; i--) {
            long L = (long) a[l] * a[l], H = (long) a[h] * a[h];
            if (L > H) { r[i] = (int) L; l++; } else { r[i] = (int) H; h--; }
        }
        return r;
    }

    public static int[] intersect(int[] a, int[] b) {
        if (a.length > b.length) return intersect(b, a);
        Map<Integer, Integer> cnt = new HashMap<>();
        for (int x : a) cnt.merge(x, 1, Integer::sum);
        int[] out = new int[a.length];
        int k = 0;
        for (int x : b) {
            Integer c = cnt.get(x);
            if (c != null && c > 0) { out[k++] = x; cnt.put(x, c - 1); }
        }
        return Arrays.copyOf(out, k);
    }

    public static void rotateRight(int[] a, int k) {
        int n = a.length;
        if (n == 0) return;
        k %= n;
        if (k < 0) k += n;
        reverse(a, 0, n - 1);
        reverse(a, 0, k - 1);
        reverse(a, k, n - 1);
    }

    private static void reverse(int[] a, int i, int j) {
        while (i < j) { int t = a[i]; a[i++] = a[j]; a[j--] = t; }
    }
}
