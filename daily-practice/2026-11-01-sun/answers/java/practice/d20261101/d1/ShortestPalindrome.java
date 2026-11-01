package practice.d20261101.d1;

/**
 * Idea: the answer is reverse(suffix) + s, where prefix = the LONGEST palindromic prefix of s; the suffix is what is left over.
 * Find that prefix with KMP: build t = s + sep + reverse(s) and compute the prefix function; the last value is the length of the
 * longest prefix of s that equals a suffix of reverse(s), i.e. a palindromic prefix. The separator (a char outside the alphabet)
 * stops a match from running across the boundary. O(n) time, O(n) space.
 */
public final class ShortestPalindrome {
    private ShortestPalindrome() {}

    public static String shortestPalindrome(String s) {
        int n = s.length();
        if (n < 2) return s;
        String rev = new StringBuilder(s).reverse().toString();
        char[] t = (s + '\u0000' + rev).toCharArray();
        int[] pi = new int[t.length];
        for (int i = 1; i < t.length; i++) {
            int k = pi[i - 1];
            while (k > 0 && t[i] != t[k]) k = pi[k - 1];
            if (t[i] == t[k]) k++;
            pi[i] = k;
        }
        return rev.substring(0, n - pi[t.length - 1]) + s;
    }
}
