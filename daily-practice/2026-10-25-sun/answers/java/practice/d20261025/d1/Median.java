package practice.d20261025.d1;

/**
 * Binary search a partition of the SHORTER array: take i elements from a and j = half - i from b so the left part has half = (n + m + 1) / 2
 * elements. It is right when maxLeft(a) <= minRight(b) and maxLeft(b) <= minRight(a). If a's left max is too big, move i left, else right.
 * Missing neighbours are +-infinity (use long sentinels). O(log min(n, m)) time, O(1) space.
 */
public final class Median {
    private Median() {}

    public static double findMedian(int[] a, int[] b) {
        if (a.length + b.length == 0) throw new IllegalArgumentException("both empty");
        if (a.length > b.length) return findMedian(b, a);
        int n = a.length, m = b.length, half = (n + m + 1) / 2;
        int lo = 0, hi = n;
        while (lo <= hi) {
            int i = (lo + hi) >>> 1, j = half - i;
            long aL = i == 0 ? Long.MIN_VALUE : a[i - 1], aR = i == n ? Long.MAX_VALUE : a[i];
            long bL = j == 0 ? Long.MIN_VALUE : b[j - 1], bR = j == m ? Long.MAX_VALUE : b[j];
            if (aL > bR) hi = i - 1;
            else if (bL > aR) lo = i + 1;
            else {
                long left = Math.max(aL, bL);
                if (((n + m) & 1) == 1) return left;
                long right = Math.min(aR, bR);
                return (left + right) / 2.0;
            }
        }
        throw new IllegalStateException("inputs are not sorted");
    }
}
