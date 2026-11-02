package practice.d20261102.a1;

public final class BitsAndStrings {
    private BitsAndStrings() {}

    /** 1 -> "A", 26 -> "Z", 27 -> "AA", 701 -> "ZY", Integer.MAX_VALUE -> "FXSHRXW". n >= 1. */
    public static String excelTitle(int n) { throw new UnsupportedOperationException("TODO"); }

    /** Inverse of excelTitle. Input is valid upper-case letters, 1..7 chars, result fits in int. */
    public static int excelNumber(String title) { throw new UnsupportedOperationException("TODO"); }

    /** Sum of two binary strings (non-empty, only '0'/'1', may have leading zeros). No leading zeros in the result except "0". Lengths up to 10^5. */
    public static String addBinary(String a, String b) { throw new UnsupportedOperationException("TODO"); }

    /** out[i] = number of 1 bits in i, for i in 0..n (length n + 1). n >= 0. O(n) total, no Integer.bitCount. */
    public static int[] countBits(int n) { throw new UnsupportedOperationException("TODO"); }
}
