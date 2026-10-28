package practice.d20261028.a2;

/**
 * Read pointer scans, write pointer marks the next slot to fill (the "keep" prefix).
 * moveZeroes: write only non-zeros, then zero-fill the tail (writes = n, no swap needed).
 * removeDuplicatesAtMostTwice: a[r] may be kept iff it differs from a[w-2] (the value two slots back in the KEPT prefix).
 * All O(n) time, O(1) space.
 */
public final class Compact {
    private Compact() {}

    public static void moveZeroes(int[] a) {
        int w = 0;
        for (int x : a) if (x != 0) a[w++] = x;
        while (w < a.length) a[w++] = 0;
    }

    public static int removeElement(int[] a, int val) {
        int w = 0;
        for (int x : a) if (x != val) a[w++] = x;
        return w;
    }

    public static int removeDuplicatesAtMostTwice(int[] a) {
        int w = 0;
        for (int x : a) if (w < 2 || x != a[w - 2]) a[w++] = x;
        return w;
    }
}
