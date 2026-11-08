package practice.d20261108.a1;

/**
 * sortColors: invariant a[0..lo)=0, a[lo..mid)=1, a(hi..n)=2, a[mid..hi] unknown. A swapped-in 2 from the right is
 * unexamined, so mid does not advance in that case. moveZeroes: write pointer of the non-zero prefix.
 * removeDuplicatesAtMostTwice: x is kept iff it differs from the value two slots behind the write pointer.
 * All O(n) time, O(1) space.
 */
public final class ArrayPartition {
    private ArrayPartition() {}

    public static void sortColors(int[] a) {
        int lo = 0, mid = 0, hi = a.length - 1;
        while (mid <= hi) {
            if (a[mid] == 0) swap(a, lo++, mid++);
            else if (a[mid] == 1) mid++;
            else swap(a, mid, hi--);
        }
    }

    public static int moveZeroes(int[] a) {
        int w = 0;
        for (int x : a) if (x != 0) a[w++] = x;
        for (int i = w; i < a.length; i++) a[i] = 0;
        return w;
    }

    public static int removeDuplicatesAtMostTwice(int[] a) {
        int w = 0;
        for (int x : a) if (w < 2 || x != a[w - 2]) a[w++] = x;
        return w;
    }

    private static void swap(int[] a, int i, int j) { int t = a[i]; a[i] = a[j]; a[j] = t; }
}
