package practice.d20261026.d1;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.Random;
import org.junit.jupiter.api.Test;

class EditDistanceTest {
    @Test void examples() {
        assertEquals(3, EditDistance.minDistance("horse", "ros"));
        assertEquals(5, EditDistance.minDistance("intention", "execution"));
        assertEquals(3, EditDistance.minDistance("kitten", "sitting"));
    }

    @Test void edges() {
        assertEquals(0, EditDistance.minDistance("", ""));
        assertEquals(4, EditDistance.minDistance("", "abcd"));
        assertEquals(4, EditDistance.minDistance("abcd", ""));
        assertEquals(0, EditDistance.minDistance("same", "same"));
        assertEquals(1, EditDistance.minDistance("a", "b"));
        assertEquals(3, EditDistance.minDistance("aaa", "bbb"));
        assertEquals(2, EditDistance.minDistance("ab", "ba"), "a transposition costs 2 here");
    }

    /** Exponential recursion: the definition itself. */
    static int brute(String a, String b) {
        if (a.isEmpty()) return b.length();
        if (b.isEmpty()) return a.length();
        if (a.charAt(0) == b.charAt(0)) return brute(a.substring(1), b.substring(1));
        return 1 + Math.min(brute(a.substring(1), b.substring(1)), Math.min(brute(a.substring(1), b), brute(a, b.substring(1))));
    }

    static String rnd(Random r, int maxLen) {
        char[] c = new char[r.nextInt(maxLen + 1)];
        for (int i = 0; i < c.length; i++) c[i] = (char) ('a' + r.nextInt(3));
        return new String(c);
    }

    @Test void randomAgainstBruteForce() {
        Random r = new Random(13);
        for (int t = 0; t < 1500; t++) {
            String a = rnd(r, 7), b = rnd(r, 7);
            assertEquals(brute(a, b), EditDistance.minDistance(a, b), a + "/" + b);
            assertEquals(EditDistance.minDistance(a, b), EditDistance.minDistance(b, a), "symmetric");
        }
    }

    @Test void boundedMatchesFullForEveryK() {
        Random r = new Random(14);
        for (int t = 0; t < 4000; t++) {
            String a = rnd(r, 9), b = rnd(r, 9);
            int d = EditDistance.minDistance(a, b);
            for (int k = 0; k <= 10; k++) assertEquals(d <= k ? d : -1, EditDistance.distanceAtMost(a, b, k), a + "/" + b + " k=" + k);
        }
    }

    @Test void fullTableSizeIsFine() {
        Random r = new Random(1);
        String a = "a".repeat(5000), b = "b" + "a".repeat(4998) + "c";
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> assertEquals(2, EditDistance.minDistance(a, b)));
    }

    @Test void boundedOnHugeStringsMustUseABand() {
        StringBuilder sb = new StringBuilder();
        Random r = new Random(2);
        for (int i = 0; i < 200_000; i++) sb.append((char) ('a' + r.nextInt(26)));
        String a = sb.toString();
        String b = a.substring(0, 1000) + "#" + a.substring(1001, 150_000) + a.substring(150_001);      // one substitution, one more later
        String c = a.substring(0, 500) + a.substring(501);                                              // one deletion
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> {
            assertEquals(2, EditDistance.distanceAtMost(a, b, 5));
            assertEquals(1, EditDistance.distanceAtMost(a, c, 5));
            assertEquals(-1, EditDistance.distanceAtMost(a, a.substring(0, 199_000), 10));
            assertEquals(0, EditDistance.distanceAtMost(a, a, 0));
        });
    }
}
