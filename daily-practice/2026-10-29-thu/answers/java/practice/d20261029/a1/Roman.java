package practice.d20261029.a1;

/**
 * Idea: a symbol smaller than its right neighbour is subtracted (IV, CM), otherwise added: scan once.
 * toRoman: greedy over the 13 value/symbol pairs (900, 400, ... included). O(n) / O(1).
 */
public final class Roman {
    private Roman() {}

    private static final int[] VALUES = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
    private static final String[] SYMBOLS = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

    private static int value(char c) {
        return switch (c) {
            case 'I' -> 1; case 'V' -> 5; case 'X' -> 10; case 'L' -> 50;
            case 'C' -> 100; case 'D' -> 500; case 'M' -> 1000;
            default -> throw new IllegalArgumentException("bad symbol " + c);
        };
    }

    public static int toInt(String s) {
        int total = 0;
        for (int i = 0; i < s.length(); i++) {
            int v = value(s.charAt(i));
            if (i + 1 < s.length() && v < value(s.charAt(i + 1))) total -= v; else total += v;
        }
        return total;
    }

    public static String toRoman(int n) {
        if (n < 1 || n > 3999) throw new IllegalArgumentException("1..3999 only: " + n);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < VALUES.length; i++)
            while (n >= VALUES[i]) { sb.append(SYMBOLS[i]); n -= VALUES[i]; }
        return sb.toString();
    }
}
