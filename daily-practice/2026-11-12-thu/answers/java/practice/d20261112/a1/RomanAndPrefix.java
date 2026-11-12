package practice.d20261112.a1;

import java.util.Map;

/** Roman numerals both ways (O(n) / O(1)), longest common prefix by vertical scan (O(total chars)). */
public class RomanAndPrefix {
    private static final int[] VALUES = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
    private static final String[] SYMBOLS = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
    private static final Map<Character, Integer> VAL = Map.of('I', 1, 'V', 5, 'X', 10, 'L', 50, 'C', 100, 'D', 500, 'M', 1000);

    /** A smaller symbol before a larger one is subtracted: scan right to left, subtract when smaller than the right neighbour. */
    public static int romanToInt(String s) {
        int total = 0, prev = 0;
        for (int i = s.length() - 1; i >= 0; i--) {
            int v = VAL.get(s.charAt(i));
            total += v < prev ? -v : v;
            prev = v;
        }
        return total;
    }

    public static String intToRoman(int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < VALUES.length; i++) {
            while (n >= VALUES[i]) { sb.append(SYMBOLS[i]); n -= VALUES[i]; }
        }
        return sb.toString();
    }

    public static String longestCommonPrefix(String[] strs) {
        if (strs.length == 0) return "";
        for (int i = 0; i < strs[0].length(); i++) {
            char c = strs[0].charAt(i);
            for (int j = 1; j < strs.length; j++) {
                if (i == strs[j].length() || strs[j].charAt(i) != c) return strs[0].substring(0, i);
            }
        }
        return strs[0];
    }
}
