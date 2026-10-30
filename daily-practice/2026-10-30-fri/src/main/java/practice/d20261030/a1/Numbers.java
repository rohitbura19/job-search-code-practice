package practice.d20261030.a1;

public final class Numbers {
    private Numbers() {}

    /**
     * floor(sqrt(x)) for 0 <= x <= Integer.MAX_VALUE. No Math.sqrt, no floating point. Beware: mid * mid overflows int.
     * sqrt(8) = 2, sqrt(16) = 4, sqrt(Integer.MAX_VALUE) = 46340. O(log x).
     */
    public static int sqrt(int x) { throw new UnsupportedOperationException("TODO"); }

    /**
     * x^n by repeated squaring, O(log |n|). n may be negative and may be Integer.MIN_VALUE (-n overflows int!).
     * pow(2, 10) = 1024, pow(2, -2) = 0.25, pow(x, 0) = 1, pow(-1, Integer.MIN_VALUE) = 1.
     */
    public static double pow(double x, int n) { throw new UnsupportedOperationException("TODO"); }

    /**
     * Reverse the decimal digits, keeping the sign; return 0 if the result does not fit in an int.
     * Use only int/long arithmetic, no String. reverse(123) = 321, reverse(-120) = -21, reverse(1534236469) = 0.
     */
    public static int reverse(int x) { throw new UnsupportedOperationException("TODO"); }
}
