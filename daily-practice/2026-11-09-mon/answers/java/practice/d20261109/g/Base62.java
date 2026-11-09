package practice.d20261109.g;

public final class Base62 {
    static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private Base62() {}

    public static String encode(long id) {
        if (id < 0) throw new IllegalArgumentException("negative id");
        if (id == 0) return "0";
        StringBuilder sb = new StringBuilder();
        for (; id > 0; id /= 62) sb.append(ALPHABET.charAt((int) (id % 62)));
        return sb.reverse().toString();
    }

    public static long decode(String code) {
        if (code == null || code.isEmpty()) throw new IllegalArgumentException("empty code");
        long n = 0;
        for (char c : code.toCharArray()) {
            int d = ALPHABET.indexOf(c);
            if (d < 0) throw new IllegalArgumentException("bad character: " + c);
            n = Math.addExact(Math.multiplyExact(n, 62L), d);
        }
        return n;
    }
}
