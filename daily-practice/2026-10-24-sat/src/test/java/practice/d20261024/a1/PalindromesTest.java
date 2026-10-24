package practice.d20261024.a1;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.Random;
import org.junit.jupiter.api.Test;

class PalindromesTest {
    @Test void isPalindromeExamples() {
        assertTrue(Palindromes.isPalindrome("A man, a plan, a canal: Panama"));
        assertFalse(Palindromes.isPalindrome("race a car"));
        assertTrue(Palindromes.isPalindrome(" "), "only separators");
        assertTrue(Palindromes.isPalindrome(""));
        assertTrue(Palindromes.isPalindrome("a"));
        assertTrue(Palindromes.isPalindrome("0P0"));
        assertFalse(Palindromes.isPalindrome("0P"));
        assertTrue(Palindromes.isPalindrome(".,"));
    }

    @Test void validPalindromeIIExamples() {
        assertTrue(Palindromes.validPalindromeII("aba"));
        assertTrue(Palindromes.validPalindromeII("abca"));
        assertFalse(Palindromes.validPalindromeII("abc"));
        assertTrue(Palindromes.validPalindromeII(""));
        assertTrue(Palindromes.validPalindromeII("a"));
        assertTrue(Palindromes.validPalindromeII("ab"));
        assertTrue(Palindromes.validPalindromeII("deeee"), "delete the d, not the last e");
        assertTrue(Palindromes.validPalindromeII("eeeed"));
        assertFalse(Palindromes.validPalindromeII("abcbxa" + "z"));
    }

    @Test void validPalindromeIIMatchesBruteForce() {
        Random rnd = new Random(7);
        for (int it = 0; it < 5000; it++) {
            int n = rnd.nextInt(9);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < n; i++) sb.append((char) ('a' + rnd.nextInt(3)));
            String s = sb.toString();
            boolean expected = pal(s);
            for (int i = 0; i < n && !expected; i++) expected = pal(s.substring(0, i) + s.substring(i + 1));
            assertEquals(expected, Palindromes.validPalindromeII(s), s);
        }
    }

    @Test void largeInputIsLinear() {
        String s = "a".repeat(2_000_000) + "b" + "a".repeat(2_000_000);
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {
            assertTrue(Palindromes.validPalindromeII(s));
            assertTrue(Palindromes.isPalindrome(s));
            assertTrue(Palindromes.validPalindromeII(s + "c"));
            assertFalse(Palindromes.validPalindromeII("c" + s + "dd"));
        });
    }

    private static boolean pal(String s) { return new StringBuilder(s).reverse().toString().equals(s); }
}
