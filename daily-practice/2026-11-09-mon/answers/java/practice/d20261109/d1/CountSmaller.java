package practice.d20261109.d1;

/**
 * Merge sort on an index array. While merging two sorted halves, every time an element of the LEFT half is emitted,
 * the number of RIGHT elements already emitted are exactly the smaller elements to its right (equal values must NOT count:
 * on ties emit the left one first). O(n log n) time, O(n) space; iterative recursion depth is only log n.
 * (A Fenwick tree over compressed values is the alternative.)
 */
public final class CountSmaller {
    private CountSmaller() {}

    public static int[] countSmallerAfter(int[] a) {
        int n = a.length;
        int[] res = new int[n], idx = new int[n], tmp = new int[n];
        for (int i = 0; i < n; i++) idx[i] = i;
        sort(a, idx, tmp, res, 0, n);
        return res;
    }

    private static void sort(int[] a, int[] idx, int[] tmp, int[] res, int lo, int hi) {
        if (hi - lo < 2) return;
        int mid = (lo + hi) >>> 1;
        sort(a, idx, tmp, res, lo, mid);
        sort(a, idx, tmp, res, mid, hi);
        int i = lo, j = mid, k = lo;
        while (i < mid || j < hi) {
            if (j == hi || (i < mid && a[idx[i]] <= a[idx[j]])) {
                res[idx[i]] += j - mid;
                tmp[k++] = idx[i++];
            } else {
                tmp[k++] = idx[j++];
            }
        }
        System.arraycopy(tmp, lo, idx, lo, hi - lo);
    }
}
