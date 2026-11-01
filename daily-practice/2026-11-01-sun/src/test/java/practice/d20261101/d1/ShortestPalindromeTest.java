package practice.d20261101.d1;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.Random;
import org.junit.jupiter.api.Test;

class ShortestPalindromeTest {
    private static boolean pal(String s, int len) {
        for (int i = 0, j = len - 1; i < j; i++, j--) if (s.charAt(i) != s.charAt(j)) return false;
        return true;
    }

    private static String brute(String s) {
        for (int k = s.length(); k >= 0; k--) if (pal(s, k)) return new StringBuilder(s.substring(k)).reverse() + s;
        throw new AssertionError();
    }

    @Test void examples() {
        assertEquals("aaacecaaa", ShortestPalindrome.shortestPalindrome("aacecaaa"));
        assertEquals("dcbabcd", ShortestPalindrome.shortestPalindrome("abcd"));
    }

    @Test void edges() {
        assertEquals("", ShortestPalindrome.shortestPalindrome(""));
        assertEquals("a", ShortestPalindrome.shortestPalindrome("a"));
        assertEquals("aaaa", ShortestPalindrome.shortestPalindrome("aaaa"));
        assertEquals("abcba", ShortestPalindrome.shortestPalindrome("abcba"));
        assertEquals("baab", ShortestPalindrome.shortestPalindrome("aab"));
        assertEquals("aba", ShortestPalindrome.shortestPalindrome("ba"));
    }

    @Test void randomAgainstBruteForce() {
        Random r = new Random(13);
        for (int t = 0; t < 3000; t++) {
            int n = r.nextInt(13);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < n; i++) sb.append((char) ('a' + r.nextInt(2)));
            String s = sb.toString();
            String got = ShortestPalindrome.shortestPalindrome(s);
            assertEquals(brute(s), got, s);
            assertTrue(pal(got, got.length()));
        }
    }

    @Test void largeInputs() {
        String a = "a".repeat(250_000) + "b" + "a".repeat(250_000);
        String worst = "b" + "a".repeat(499_999);
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {
            assertEquals(a, ShortestPalindrome.shortestPalindrome(a));
            String r = ShortestPalindrome.shortestPalindrome(worst);
            assertEquals(2 * worst.length() - 1, r.length());
            assertEquals('a', r.charAt(0));
            assertEquals('b', r.charAt(r.length() / 2));
        });
    }
}
