package practice.d20261030.a1;

/**
 * sqrt: binary search on the answer in a long, mid <= x / mid avoids overflow. O(log x) time, O(1) space.
 * pow: widen n to long before negating (so MIN_VALUE works), square the base, multiply when the bit is set. O(log |n|).
 * reverse: pop a digit, check before pushing: r > MAX/10 or (r == MAX/10 and d > 7) overflows (mirror for MIN, d < -8).
 */
public final class Numbers {
    private Numbers() {}

    public static int sqrt(int x) {
        long lo = 0, hi = 46340;          // 46341^2 > Integer.MAX_VALUE
        while (lo < hi) {
            long mid = (lo + hi + 1) >>> 1;
            if (mid * mid <= x) lo = mid; else hi = mid - 1;
        }
        return (int) lo;
    }

    public static double pow(double x, int n) {
        long e = n;
        if (e < 0) { x = 1 / x; e = -e; }
        double r = 1;
        while (e > 0) {
            if ((e & 1) == 1) r *= x;
            x *= x;
            e >>= 1;
        }
        return r;
    }

    public static int reverse(int x) {
        int r = 0;
        while (x != 0) {
            int d = x % 10;
            x /= 10;
            if (r > Integer.MAX_VALUE / 10 || (r == Integer.MAX_VALUE / 10 && d > 7)) return 0;
            if (r < Integer.MIN_VALUE / 10 || (r == Integer.MIN_VALUE / 10 && d < -8)) return 0;
            r = r * 10 + d;
        }
        return r;
    }
}
