package practice.d20261109.g;

/** Alphabet "0-9A-Za-z" in that order (value 0 -> '0', 10 -> 'A', 36 -> 'a', 61 -> 'z'). */
public final class Base62 {
    static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private Base62() {}

    /** encode(0) = "0"; no leading zeros otherwise. Negative -> IllegalArgumentException. Must handle Long.MAX_VALUE. */
    public static String encode(long id) { throw new UnsupportedOperationException("TODO"); }

    /** Inverse of encode. Empty string or a character outside the alphabet -> IllegalArgumentException. */
    public static long decode(String code) { throw new UnsupportedOperationException("TODO"); }
}
