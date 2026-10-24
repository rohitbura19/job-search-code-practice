package practice.d20261024.d1;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.Random;
import org.junit.jupiter.api.Test;

class MinWindowTest {
    @Test void examples() {
        assertEquals("BANC", MinWindow.minWindow("ADOBECODEBANC", "ABC"));
        assertEquals("a", MinWindow.minWindow("a", "a"));
        assertEquals("", MinWindow.minWindow("a", "aa"), "multiplicity matters");
        assertEquals("aa", MinWindow.minWindow("aa", "aa"));
        assertEquals("", MinWindow.minWindow("", "a"));
        assertEquals("", MinWindow.minWindow("abc", "d"));
        assertEquals("", MinWindow.minWindow("abc", ""), "empty t has no window by this contract");
        assertEquals("ab", MinWindow.minWindow("abba", "ab"), "ab at 0 and ba at 2 tie: leftmost");
    }

    @Test void leftmostShortestWins() {
        assertEquals("ab", MinWindow.minWindow("abxab", "ab"));
        assertEquals("ab", MinWindow.minWindow("xabyab", "ab"));
    }

    @Test void matchesBruteForce() {
        Random rnd = new Random(9);
        for (int it = 0; it < 4000; it++) {
            String s = rand(rnd, rnd.nextInt(12)), t = rand(rnd, 1 + rnd.nextInt(4));
            assertEquals(brute(s, t), MinWindow.minWindow(s, t), s + "/" + t);
        }
    }

    @Test void largeInput() {
        String s = "ab".repeat(2_500_000) + "c";
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> assertEquals("abc", MinWindow.minWindow(s, "abc")));
    }

    static String rand(Random rnd, int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) sb.append((char) ('a' + rnd.nextInt(3)));
        return sb.toString();
    }

    static String brute(String s, String t) {
        String best = "";
        for (int i = 0; i < s.length(); i++) {
            for (int j = i + t.length(); j <= s.length(); j++) {
                String w = s.substring(i, j);
                if ((best.isEmpty() || w.length() < best.length()) && covers(w, t)) best = w;
            }
        }
        return best;
    }

    static boolean covers(String w, String t) {
        int[] c = new int[128];
        for (char ch : w.toCharArray()) c[ch]++;
        for (char ch : t.toCharArray()) if (--c[ch] < 0) return false;
        return true;
    }
}
