package practice.d20261030.d1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class RegexTest {
    @Test void examples() {
        assertFalse(Regex.isMatch("aa", "a"));
        assertTrue(Regex.isMatch("aa", "a*"));
        assertTrue(Regex.isMatch("ab", ".*"));
        assertTrue(Regex.isMatch("aab", "c*a*b"));
        assertFalse(Regex.isMatch("mississippi", "mis*is*p*."));
        assertTrue(Regex.isMatch("mississippi", "mis*is*ip*."));
        assertTrue(Regex.isMatch("", "a*b*"));
        assertTrue(Regex.isMatch("", ""));
        assertFalse(Regex.isMatch("a", ""));
        assertFalse(Regex.isMatch("", "a"));
        assertFalse(Regex.isMatch("", "."));
        assertTrue(Regex.isMatch("abc", "a.c"));
        assertFalse(Regex.isMatch("abcd", "a.c"));
        assertTrue(Regex.isMatch("a", "ab*"));
        assertTrue(Regex.isMatch("bbbba", ".*a*a"));
        assertFalse(Regex.isMatch("ab", ".*c"));
    }

    @Test void randomAgainstJavaUtilRegex() {
        Random r = new Random(13);
        String el = "ab.";
        for (int it = 0; it < 20000; it++) {
            StringBuilder p = new StringBuilder();
            for (int k = r.nextInt(6); k > 0; k--) {
                p.append(el.charAt(r.nextInt(3)));
                if (r.nextInt(3) == 0) p.append('*');
            }
            StringBuilder s = new StringBuilder();
            for (int k = r.nextInt(8); k > 0; k--) s.append((char) ('a' + r.nextInt(2)));
            assertEquals(Pattern.matches(p.toString(), s), Regex.isMatch(s.toString(), p.toString()), "s=" + s + " p=" + p);
        }
    }

    @Test @Timeout(2) void notExponential() {
        assertFalse(Regex.isMatch("a".repeat(30), "a*".repeat(15) + "b"));
        assertFalse(Regex.isMatch("a".repeat(2000), "a*".repeat(1000) + "b"));
        assertTrue(Regex.isMatch("a".repeat(2000), ".*".repeat(1000)));
    }

    @Test @Timeout(3) void large() {
        String s = "ab".repeat(1000);
        assertTrue(Regex.isMatch(s, "a.".repeat(1000)));
        assertTrue(Regex.isMatch(s, ".*b"));
    }
}
