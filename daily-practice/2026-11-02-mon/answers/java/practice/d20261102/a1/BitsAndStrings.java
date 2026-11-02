package practice.d20261102.a1;

/**
 * excelTitle: bijective base 26 - subtract 1 before each digit so 'A'..'Z' map to 0..25. O(log n).
 * excelNumber: Horner, r = r * 26 + digit (1..26). O(len).
 * addBinary: add from the right with a carry, then strip leading zeros. O(max len).
 * countBits: popcount(i) = popcount(i >> 1) + (i & 1). O(n) time and output.
 */
public final class BitsAndStrings {
    private BitsAndStrings() {}

    public static String excelTitle(int n) {
        StringBuilder sb = new StringBuilder();
        while (n > 0) {
            n--;
            sb.append((char) ('A' + n % 26));
            n /= 26;
        }
        return sb.reverse().toString();
    }

    public static int excelNumber(String title) {
        int r = 0;
        for (int i = 0; i < title.length(); i++) r = r * 26 + (title.charAt(i) - 'A' + 1);
        return r;
    }

    public static String addBinary(String a, String b) {
        StringBuilder sb = new StringBuilder(Math.max(a.length(), b.length()) + 1);
        int i = a.length() - 1, j = b.length() - 1, carry = 0;
        while (i >= 0 || j >= 0 || carry > 0) {
            int s = carry;
            if (i >= 0) s += a.charAt(i--) - '0';
            if (j >= 0) s += b.charAt(j--) - '0';
            sb.append((char) ('0' + (s & 1)));
            carry = s >> 1;
        }
        while (sb.length() > 1 && sb.charAt(sb.length() - 1) == '0') sb.setLength(sb.length() - 1);
        if (sb.length() == 0) return "0";
        return sb.reverse().toString();
    }

    public static int[] countBits(int n) {
        int[] out = new int[n + 1];
        for (int i = 1; i <= n; i++) out[i] = out[i >> 1] + (i & 1);
        return out;
    }
}
