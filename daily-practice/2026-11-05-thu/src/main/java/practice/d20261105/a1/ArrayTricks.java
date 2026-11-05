package practice.d20261105.a1;

public final class ArrayTricks {
    private ArrayTricks() {}

    /** out[i] = product of all a[j], j != i. No division. int arithmetic (wraps on overflow). O(n) time, O(1) extra space besides the result. */
    public static int[] productExceptSelf(int[] a) { throw new UnsupportedOperationException("TODO"); }

    /** Rotate right by k places IN PLACE (k >= 0, may exceed a.length; empty array allowed). O(n) time, O(1) space. */
    public static void rotate(int[] a, int k) { throw new UnsupportedOperationException("TODO"); }

    /** Leftmost index i with sum(a[0..i-1]) == sum(a[i+1..]); -1 if none. Sums can exceed int. */
    public static int pivotIndex(int[] a) { throw new UnsupportedOperationException("TODO"); }
}
