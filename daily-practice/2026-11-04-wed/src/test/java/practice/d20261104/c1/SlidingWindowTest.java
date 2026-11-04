package practice.d20261104.c1;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.*;
import org.junit.jupiter.api.Test;

class SlidingWindowTest {
    @Test void longestUniqueExamples() {
        assertEquals(3, SlidingWindow.longestUnique("abcabcbb"));
        assertEquals(1, SlidingWindow.longestUnique("bbbbb"));
        assertEquals(3, SlidingWindow.longestUnique("pwwkew"));
        assertEquals(0, SlidingWindow.longestUnique(""));
        assertEquals(1, SlidingWindow.longestUnique("x"));
        assertEquals(2, SlidingWindow.longestUnique("abba"));   // the stale index of 'a' must not move the left edge back
        assertEquals(5, SlidingWindow.longestUnique("tmmzuxt"));
        assertEquals(3, SlidingWindow.longestUnique("a b a b"));
    }

    @Test void replacementExamples() {
        assertEquals(4, SlidingWindow.longestRepeatingReplacement("ABAB", 2));
        assertEquals(4, SlidingWindow.longestRepeatingReplacement("AABABBA", 1));
        assertEquals(0, SlidingWindow.longestRepeatingReplacement("", 3));
        assertEquals(1, SlidingWindow.longestRepeatingReplacement("AB", 0));
        assertEquals(2, SlidingWindow.longestRepeatingReplacement("AB", 1));
        assertEquals(5, SlidingWindow.longestRepeatingReplacement("AAAAA", 0));
        assertEquals(3, SlidingWindow.longestRepeatingReplacement("ABC", 100));
    }

    @Test void shortestSubarrayExamples() {
        assertEquals(2, SlidingWindow.shortestSubarrayAtLeast(7, new int[]{2, 3, 1, 2, 4, 3}));
        assertEquals(1, SlidingWindow.shortestSubarrayAtLeast(4, new int[]{1, 4, 4}));
        assertEquals(0, SlidingWindow.shortestSubarrayAtLeast(11, new int[]{1, 1, 1, 1, 1, 1, 1, 1}));
        assertEquals(0, SlidingWindow.shortestSubarrayAtLeast(5, new int[]{}));
        assertEquals(3, SlidingWindow.shortestSubarrayAtLeast(6, new int[]{2, 2, 2}));
    }

    @Test void sumsBeyondIntRange() {
        int[] a = {Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE};
        assertEquals(2, SlidingWindow.shortestSubarrayAtLeast(Integer.MAX_VALUE + 1L, a));
        assertEquals(3, SlidingWindow.shortestSubarrayAtLeast(2L * Integer.MAX_VALUE + 1, a));
        assertEquals(0, SlidingWindow.shortestSubarrayAtLeast(3L * Integer.MAX_VALUE + 1, a));
    }

    @Test void randomVsBruteForce() {
        Random rnd = new Random(11);
        for (int t = 0; t < 400; t++) {
            int n = rnd.nextInt(14);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < n; i++) sb.append((char) ('A' + rnd.nextInt(3)));
            String s = sb.toString();
            assertEquals(bruteUnique(s), SlidingWindow.longestUnique(s), s);
            int k = rnd.nextInt(4);
            assertEquals(bruteReplace(s, k), SlidingWindow.longestRepeatingReplacement(s, k), s + " k=" + k);
            int[] a = new int[n];
            for (int i = 0; i < n; i++) a[i] = 1 + rnd.nextInt(6);
            long target = 1 + rnd.nextInt(25);
            assertEquals(bruteShortest(target, a), SlidingWindow.shortestSubarrayAtLeast(target, a), Arrays.toString(a) + " " + target);
        }
    }

    @Test void largeInputsAreLinear() {
        StringBuilder sb = new StringBuilder();
        Random rnd = new Random(5);
        for (int i = 0; i < 1_000_000; i++) sb.append((char) ('A' + rnd.nextInt(26)));
        String s = sb.toString();
        int[] a = new int[1_000_000];
        Arrays.fill(a, 1);
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> {
            SlidingWindow.longestUnique(s);
            assertTrue(SlidingWindow.longestRepeatingReplacement(s, 1000) >= 1000);
            assertEquals(1_000_000, SlidingWindow.shortestSubarrayAtLeast(1_000_000, a));
            assertEquals(0, SlidingWindow.shortestSubarrayAtLeast(1_000_001, a));
        });
    }

    static int bruteUnique(String s) {
        int best = 0;
        for (int i = 0; i < s.length(); i++)
            for (int j = i; j < s.length(); j++) {
                Set<Character> seen = new HashSet<>();
                boolean ok = true;
                for (int x = i; x <= j && ok; x++) ok = seen.add(s.charAt(x));
                if (ok) best = Math.max(best, j - i + 1);
            }
        return best;
    }

    static int bruteReplace(String s, int k) {
        int best = 0;
        for (int i = 0; i < s.length(); i++)
            for (int j = i; j < s.length(); j++) {
                int[] c = new int[26];
                int mx = 0;
                for (int x = i; x <= j; x++) mx = Math.max(mx, ++c[s.charAt(x) - 'A']);
                if (j - i + 1 - mx <= k) best = Math.max(best, j - i + 1);
            }
        return best;
    }

    static int bruteShortest(long target, int[] a) {
        int best = 0;
        for (int i = 0; i < a.length; i++) {
            long sum = 0;
            for (int j = i; j < a.length; j++) {
                sum += a[j];
                if (sum >= target) { if (best == 0 || j - i + 1 < best) best = j - i + 1; break; }
            }
        }
        return best;
    }
}
