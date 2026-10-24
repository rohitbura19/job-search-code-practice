package practice.d20261024.a1;

/**
 * Two pointers from both ends. (a) skip non-alphanumerics, compare case-insensitively. (b) on the first mismatch we may drop
 * either side, so check "palindrome without s[l]" or "without s[r]" with a plain scan: the deletion is used at most once, so the
 * total work is O(n). Compare chars with Character.toLowerCase on the code unit (the inputs are ASCII). O(n) time, O(1) space.
 */
public final class Palindromes {
    private Palindromes() {}

    public static boolean isPalindrome(String s) {
        int l = 0, r = s.length() - 1;
        while (l < r) {
            if (!Character.isLetterOrDigit(s.charAt(l))) { l++; continue; }
            if (!Character.isLetterOrDigit(s.charAt(r))) { r--; continue; }
            if (Character.toLowerCase(s.charAt(l)) != Character.toLowerCase(s.charAt(r))) return false;
            l++;
            r--;
        }
        return true;
    }

    public static boolean validPalindromeII(String s) {
        int l = 0, r = s.length() - 1;
        while (l < r) {
            if (s.charAt(l) != s.charAt(r)) return plain(s, l + 1, r) || plain(s, l, r - 1);
            l++;
            r--;
        }
        return true;
    }

    private static boolean plain(String s, int l, int r) {
        while (l < r) if (s.charAt(l++) != s.charAt(r--)) return false;
        return true;
    }
}
