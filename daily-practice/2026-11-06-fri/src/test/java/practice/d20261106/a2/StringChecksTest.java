package practice.d20261106.a2;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.Random;
import org.junit.jupiter.api.Test;

class StringChecksTest {
    @Test void subsequence() {
        assertTrue(StringChecks.isSubsequence("abc", "ahbgdc"));
        assertFalse(StringChecks.isSubsequence("axc", "ahbgdc"));
        assertTrue(StringChecks.isSubsequence("", ""));
        assertTrue(StringChecks.isSubsequence("", "abc"));
        assertFalse(StringChecks.isSubsequence("a", ""));
        assertFalse(StringChecks.isSubsequence("aa", "a"));
        assertTrue(StringChecks.isSubsequence("aa", "abca"));
    }

    @Test void palindromeExamples() {
        assertTrue(StringChecks.validPalindromeII("aba"));
        assertTrue(StringChecks.validPalindromeII("abca"));
        assertFalse(StringChecks.validPalindromeII("abc"));
        assertTrue(StringChecks.validPalindromeII(""));
        assertTrue(StringChecks.validPalindromeII("a"));
        assertTrue(StringChecks.validPalindromeII("ab"));
        assertTrue(StringChecks.validPalindromeII("deeee"));
        assertTrue(StringChecks.validPalindromeII("cuppucu"));
        assertFalse(StringChecks.validPalindromeII("abcdba" + "x"));
    }

    @Test void palindromeRandomVsBrute() {
        Random r = new Random(3);
        for (int t = 0; t < 2000; t++) {
            StringBuilder sb = new StringBuilder();
            for (int i = r.nextInt(9); i > 0; i--) sb.append((char) ('a' + r.nextInt(3)));
            String s = sb.toString();
            boolean exp = pal(s);
            for (int i = 0; i < s.length() && !exp; i++) exp = pal(s.substring(0, i) + s.substring(i + 1));
            assertEquals(exp, StringChecks.validPalindromeII(s), s);
        }
    }

    @Test void palindromeLarge() {
        String s = "a".repeat(1_000_000) + "b" + "a".repeat(1_000_000);
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> assertTrue(StringChecks.validPalindromeII(s + "")));
    }

    @Test void countAndSay() {
        assertEquals("1", StringChecks.countAndSay(1));
        assertEquals("11", StringChecks.countAndSay(2));
        assertEquals("21", StringChecks.countAndSay(3));
        assertEquals("1211", StringChecks.countAndSay(4));
        assertEquals("111221", StringChecks.countAndSay(5));
        assertEquals("312211", StringChecks.countAndSay(6));
        assertEquals(4462, StringChecks.countAndSay(30).length());
    }

    private static boolean pal(String s) { return new StringBuilder(s).reverse().toString().equals(s); }
}
