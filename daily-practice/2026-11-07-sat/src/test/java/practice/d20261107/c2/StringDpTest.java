package practice.d20261107.c2;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.Random;
import org.junit.jupiter.api.Test;

class StringDpTest {
    @Test void palindromeExamples() {
        assertEquals("bab", StringDp.longestPalindrome("babad"));
        assertEquals("bb", StringDp.longestPalindrome("cbbd"));
        assertEquals("a", StringDp.longestPalindrome("a"));
        assertEquals("", StringDp.longestPalindrome(""));
        assertEquals("a", StringDp.longestPalindrome("abc"));
        assertEquals("aaaa", StringDp.longestPalindrome("aaaa"));
        assertEquals("racecar", StringDp.longestPalindrome("xxracecaryz"));
    }

    @Test void palindromeVsBrute() {
        Random r = new Random(6);
        for (int t = 0; t < 3000; t++) {
            StringBuilder sb = new StringBuilder();
            int n = r.nextInt(14);
            for (int i = 0; i < n; i++) sb.append((char) ('a' + r.nextInt(2 + t % 3)));
            assertEquals(brute(sb.toString()), StringDp.longestPalindrome(sb.toString()), sb.toString());
        }
    }

    @Test void palindromeLarge() {
        String s = "a".repeat(5000);
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> assertEquals(s, StringDp.longestPalindrome(s)));
        StringBuilder sb = new StringBuilder();
        Random r = new Random(7);
        for (int i = 0; i < 5000; i++) sb.append((char) ('a' + r.nextInt(26)));
        String rnd = sb.toString();
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> StringDp.longestPalindrome(rnd));
    }

    @Test void targetSumExamples() {
        assertEquals(5, StringDp.targetSum(new int[]{1, 1, 1, 1, 1}, 3));
        assertEquals(1, StringDp.targetSum(new int[]{1}, 1));
        assertEquals(0, StringDp.targetSum(new int[]{1}, 2));
        assertEquals(2, StringDp.targetSum(new int[]{1}, -1) + 1);
        assertEquals(256, StringDp.targetSum(new int[]{0, 0, 0, 0, 0, 0, 0, 0, 1}, 1));
        assertEquals(0, StringDp.targetSum(new int[]{1, 2}, 4));
        assertEquals(1L << 40, StringDp.targetSum(new int[40], 0));
        assertEquals(0, StringDp.targetSum(new int[]{2, 2}, 1)); // parity
    }

    @Test void targetSumVsBrute() {
        Random r = new Random(8);
        for (int t = 0; t < 2000; t++) {
            int[] a = r.ints(r.nextInt(11), 0, 6).toArray();
            int target = r.nextInt(31) - 15;
            assertEquals(brute(a, 0, target), StringDp.targetSum(a, target));
        }
    }

    @Test void targetSumLarge() {
        int[] a = new Random(9).ints(60, 0, 80).toArray();
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> StringDp.targetSum(a, 7));
    }

    private static String brute(String s) {
        String best = "";
        for (int i = 0; i < s.length(); i++)
            for (int j = i + 1; j <= s.length(); j++) {
                String c = s.substring(i, j);
                if (c.length() > best.length() && new StringBuilder(c).reverse().toString().equals(c)) best = c;
            }
        return best;
    }

    private static long brute(int[] a, int i, int rem) {
        if (i == a.length) return rem == 0 ? 1 : 0;
        return brute(a, i + 1, rem - a[i]) + brute(a, i + 1, rem + a[i]);
    }
}
