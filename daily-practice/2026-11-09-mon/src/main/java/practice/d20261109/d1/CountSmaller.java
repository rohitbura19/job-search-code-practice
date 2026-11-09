package practice.d20261109.d1;

public final class CountSmaller {
    private CountSmaller() {}

    /**
     * result[i] = number of j > i with a[j] < a[i] (strictly smaller). n up to 200_000, values any int (duplicates, negatives, extremes).
     * Target O(n log n): merge sort over indices, or a Fenwick tree over compressed values. The O(n^2) loop times out.
     */
    public static int[] countSmallerAfter(int[] a) { throw new UnsupportedOperationException("TODO"); }
}
