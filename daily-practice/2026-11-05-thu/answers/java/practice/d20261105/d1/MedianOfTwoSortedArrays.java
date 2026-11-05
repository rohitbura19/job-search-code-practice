package practice.d20261105.d1;

/**
 * Binary search a partition of the SHORTER array: take i elements from a and j = (m+n+1)/2 - i from b so the left halves hold half the elements.
 * Valid when aLeft <= bRight and bLeft <= aRight (missing sides are -inf / +inf). If aLeft > bRight, take fewer from a (hi = i-1); else more (lo = i+1).
 * Odd total: max(aLeft, bLeft). Even total: average of that and min(aRight, bRight), computed in long/double.
 * O(log min(m,n)) time, O(1) space.
 */
public final class MedianOfTwoSortedArrays {
    private MedianOfTwoSortedArrays() {}

    public static double findMedian(int[] a, int[] b) {
        if (a.length > b.length) return findMedian(b, a);
        int m = a.length, n = b.length, half = (m + n + 1) / 2;
        int lo = 0, hi = m;
        while (lo <= hi) {
            int i = lo + (hi - lo) / 2, j = half - i;
            long aL = i == 0 ? Long.MIN_VALUE : a[i - 1], aR = i == m ? Long.MAX_VALUE : a[i];
            long bL = j == 0 ? Long.MIN_VALUE : b[j - 1], bR = j == n ? Long.MAX_VALUE : b[j];
            if (aL > bR) hi = i - 1;
            else if (bL > aR) lo = i + 1;
            else {
                long left = Math.max(aL, bL);
                if (((m + n) & 1) == 1) return left;
                long right = Math.min(aR, bR);
                return (left + right) / 2.0;
            }
        }
        throw new IllegalArgumentException("inputs must be sorted and not both empty");
    }
}
