package practice.d20261025.g;

/** Repeated divmod by 62 (encode) and Horner's rule with multiplyExact/addExact (decode). O(digits). 62^11 > 2^63, so a long needs at most 11 chars. */
public final class Base62 {
    static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    private Base62() {}

    public static String encode(long n) {
        if (n < 0) throw new IllegalArgumentException("negative: " + n);
        if (n == 0) return "0";
        StringBuilder sb = new StringBuilder();
        for (; n > 0; n /= 62) sb.append(ALPHABET.charAt((int) (n % 62)));
        return sb.reverse().toString();
    }

    public static long decode(String s) {
        if (s == null || s.isEmpty()) throw new IllegalArgumentException("empty");
        long v = 0;
        for (int i = 0; i < s.length(); i++) {
            int d = ALPHABET.indexOf(s.charAt(i));
            if (d < 0) throw new IllegalArgumentException("bad char '" + s.charAt(i) + "'");
            v = Math.addExact(Math.multiplyExact(v, 62L), d);
        }
        return v;
    }
}
