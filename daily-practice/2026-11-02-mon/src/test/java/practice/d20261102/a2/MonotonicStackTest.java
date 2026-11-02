package practice.d20261102.a2;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.Random;
import org.junit.jupiter.api.Test;

class MonotonicStackTest {
    @Test void dailyTemperaturesExamples() {
        assertArrayEquals(new int[]{1, 1, 4, 2, 1, 1, 0, 0}, MonotonicStack.dailyTemperatures(new int[]{73, 74, 75, 71, 69, 72, 76, 73}));
        assertArrayEquals(new int[]{1, 1, 1, 0}, MonotonicStack.dailyTemperatures(new int[]{30, 40, 50, 60}));
        assertArrayEquals(new int[]{0, 0, 0}, MonotonicStack.dailyTemperatures(new int[]{30, 30, 30}));
        assertArrayEquals(new int[]{}, MonotonicStack.dailyTemperatures(new int[]{}));
        assertArrayEquals(new int[]{0}, MonotonicStack.dailyTemperatures(new int[]{5}));
    }

    @Test void dailyTemperaturesRandomAgainstBruteForce() {
        Random r = new Random(3);
        for (int t = 0; t < 300; t++) {
            int[] a = r.ints(r.nextInt(40), -5, 6).toArray();
            int[] exp = new int[a.length];
            for (int i = 0; i < a.length; i++)
                for (int j = i + 1; j < a.length; j++) if (a[j] > a[i]) { exp[i] = j - i; break; }
            assertArrayEquals(exp, MonotonicStack.dailyTemperatures(a));
        }
    }

    @Test void circularExamples() {
        assertArrayEquals(new int[]{2, -1, 2}, MonotonicStack.nextGreaterCircular(new int[]{1, 2, 1}));
        assertArrayEquals(new int[]{2, 3, 4, -1, 4}, MonotonicStack.nextGreaterCircular(new int[]{1, 2, 3, 4, 3}));
        assertArrayEquals(new int[]{-1}, MonotonicStack.nextGreaterCircular(new int[]{7}));
        assertArrayEquals(new int[]{-1, -1}, MonotonicStack.nextGreaterCircular(new int[]{2, 2}));
        assertArrayEquals(new int[]{5, Integer.MAX_VALUE, -1}, MonotonicStack.nextGreaterCircular(new int[]{Integer.MIN_VALUE, 5, Integer.MAX_VALUE}));
    }

    @Test void circularRandomAgainstBruteForce() {
        Random r = new Random(4);
        for (int t = 0; t < 300; t++) {
            int[] a = r.ints(1 + r.nextInt(30), -4, 5).toArray();
            int n = a.length;
            int[] exp = new int[n];
            for (int i = 0; i < n; i++) {
                exp[i] = -1;
                for (int d = 1; d < n; d++) if (a[(i + d) % n] > a[i]) { exp[i] = a[(i + d) % n]; break; }
            }
            assertArrayEquals(exp, MonotonicStack.nextGreaterCircular(a));
        }
    }

    @Test void removeDuplicatesExamples() {
        assertEquals("aa", MonotonicStack.removeDuplicates("deeedbbcccbdaa", 3));
        assertEquals("ps", MonotonicStack.removeDuplicates("pbbcggttciiippooaais", 2));
        assertEquals("abcd", MonotonicStack.removeDuplicates("abcd", 2));
        assertEquals("", MonotonicStack.removeDuplicates("abc", 1));
        assertEquals("", MonotonicStack.removeDuplicates("", 3));
        assertEquals("aa", MonotonicStack.removeDuplicates("abbba", 3)); // runs merge after a deletion
    }

    @Test void removeDuplicatesRandomAgainstBruteForce() {
        Random r = new Random(5);
        for (int t = 0; t < 500; t++) {
            StringBuilder sb = new StringBuilder();
            int len = r.nextInt(30);
            for (int i = 0; i < len; i++) sb.append((char) ('a' + r.nextInt(3)));
            int k = 1 + r.nextInt(4);
            assertEquals(brute(sb.toString(), k), MonotonicStack.removeDuplicates(sb.toString(), k), sb + " k=" + k);
        }
    }

    @Test void largeInputsWithinTimeout() {
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {
            int n = 1_000_000;
            int[] inc = new int[n];
            for (int i = 0; i < n; i++) inc[i] = i;
            assertEquals(1, MonotonicStack.dailyTemperatures(inc)[0]);
            int[] dec = new int[n];
            for (int i = 0; i < n; i++) dec[i] = n - i;
            assertEquals(0, MonotonicStack.dailyTemperatures(dec)[0]);
            assertEquals(-1, MonotonicStack.nextGreaterCircular(dec)[0]);
            assertEquals("", MonotonicStack.removeDuplicates("a".repeat(n), 2));
            assertEquals("a".repeat(n - 1), MonotonicStack.removeDuplicates("a".repeat(n - 1), n));
        });
    }

    private static String brute(String s, int k) {
        boolean changed = true;
        while (changed) {
            changed = false;
            int run = 1;
            for (int i = 1; i <= s.length(); i++) {
                if (i < s.length() && s.charAt(i) == s.charAt(i - 1)) { run++; if (run == k) { s = s.substring(0, i - k + 1) + s.substring(i + 1); changed = true; break; } }
                else run = 1;
            }
            if (k == 1 && !s.isEmpty()) { s = ""; changed = true; }
        }
        return s;
    }
}
