package practice.d20261110.a2;

/**
 * compress: read/write pointers, write the char then the decimal digits of a run longer than 1; O(n) time, O(1) space.
 * isValidIPv4: exactly 4 dot-separated parts, each 1-3 digits, no leading zero unless the part is "0", value <= 255. Split with limit -1 so trailing dots yield empty parts.
 * addStrings: schoolbook addition from the right with a carry, StringBuilder reversed at the end; no BigInteger, O(max(n, m)).
 */
public class StringBasics {
    public static int compress(char[] chars) {
        int w = 0, i = 0, n = chars.length;
        while (i < n) {
            char c = chars[i];
            int j = i;
            while (j < n && chars[j] == c) j++;
            chars[w++] = c;
            int run = j - i;
            if (run > 1) for (char d : Integer.toString(run).toCharArray()) chars[w++] = d;
            i = j;
        }
        return w;
    }

    public static boolean isValidIPv4(String s) {
        String[] parts = s.split("\\.", -1);
        if (parts.length != 4) return false;
        for (String p : parts) {
            if (p.isEmpty() || p.length() > 3) return false;
            if (p.length() > 1 && p.charAt(0) == '0') return false;
            int v = 0;
            for (int k = 0; k < p.length(); k++) {
                char c = p.charAt(k);
                if (c < '0' || c > '9') return false;
                v = v * 10 + (c - '0');
            }
            if (v > 255) return false;
        }
        return true;
    }

    public static String addStrings(String a, String b) {
        StringBuilder sb = new StringBuilder();
        int i = a.length() - 1, j = b.length() - 1, carry = 0;
        while (i >= 0 || j >= 0 || carry > 0) {
            int sum = carry;
            if (i >= 0) sum += a.charAt(i--) - '0';
            if (j >= 0) sum += b.charAt(j--) - '0';
            sb.append((char) ('0' + sum % 10));
            carry = sum / 10;
        }
        return sb.reverse().toString();
    }
}
