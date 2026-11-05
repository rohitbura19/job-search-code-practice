package practice.d20261105.a1;

/**
 * productExceptSelf: prefix products left to right into out, then a running suffix product right to left. O(n) / O(1) extra.
 * rotate: reverse all, reverse first k%n, reverse the rest. O(n) / O(1).
 * pivotIndex: total in long; left running sum; pivot when left == total - left - a[i]. O(n) / O(1).
 */
public final class ArrayTricks {
    private ArrayTricks() {}

    public static int[] productExceptSelf(int[] a) {
        int n = a.length;
        int[] out = new int[n];
        int prefix = 1;
        for (int i = 0; i < n; i++) { out[i] = prefix; prefix *= a[i]; }
        int suffix = 1;
        for (int i = n - 1; i >= 0; i--) { out[i] *= suffix; suffix *= a[i]; }
        return out;
    }

    public static void rotate(int[] a, int k) {
        int n = a.length;
        if (n == 0) return;
        k %= n;
        reverse(a, 0, n - 1);
        reverse(a, 0, k - 1);
        reverse(a, k, n - 1);
    }

    private static void reverse(int[] a, int i, int j) {
        while (i < j) { int t = a[i]; a[i++] = a[j]; a[j--] = t; }
    }

    public static int pivotIndex(int[] a) {
        long total = 0;
        for (int v : a) total += v;
        long left = 0;
        for (int i = 0; i < a.length; i++) {
            if (left == total - left - a[i]) return i;
            left += a[i];
        }
        return -1;
    }
}
