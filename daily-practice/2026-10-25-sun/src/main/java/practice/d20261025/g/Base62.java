package practice.d20261025.g;

public final class Base62 {
    static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    private Base62() {}

    /** n >= 0 (else IllegalArgumentException); 0 -> "0"; no leading zeros otherwise; most significant digit first. */
    public static String encode(long n) { throw new UnsupportedOperationException("TODO"); }

    /** Inverse of encode. Empty or a char outside the alphabet -> IllegalArgumentException; a value above Long.MAX_VALUE -> ArithmeticException. */
    public static long decode(String s) { throw new UnsupportedOperationException("TODO"); }
}
