package practice.d20261104.a2;

/**
 * One helper does the work: first index whose value is >= t (or > t). searchInsert = lower bound; firstLast = [lower(t), upper(t) - 1] if present;
 * findMin compares the middle with the right end: if mid > right the minimum is strictly right of mid, else it is mid or left of it.
 * Avoid t + 1 for the upper bound: it overflows when t == Integer.MAX_VALUE. Midpoint is lo + (hi - lo) / 2.
 */
public final class BinarySearch {
    private BinarySearch() {}

    private static int bound(int[] a, int t, boolean strictlyGreater) {
        int lo = 0, hi = a.length;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (a[mid] < t || (strictlyGreater && a[mid] == t)) lo = mid + 1; else hi = mid;
        }
        return lo;
    }

    public static int searchInsert(int[] a, int target) { return bound(a, target, false); }

    public static int[] firstLast(int[] a, int target) {
        int lo = bound(a, target, false);
        if (lo == a.length || a[lo] != target) return new int[]{-1, -1};
        return new int[]{lo, bound(a, target, true) - 1};
    }

    public static int findMin(int[] a) {
        int lo = 0, hi = a.length - 1;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (a[mid] > a[hi]) lo = mid + 1; else hi = mid;
        }
        return a[lo];
    }
}
