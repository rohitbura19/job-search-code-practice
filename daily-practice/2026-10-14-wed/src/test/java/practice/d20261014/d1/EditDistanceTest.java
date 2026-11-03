package practice.d20261014.d1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class EditDistanceTest {

    @Test @DisplayName("sheet examples")
    void examples() {
        assertEquals(3, EditDistance.editDistance("horse", "ros"));
        assertEquals(5, EditDistance.editDistance("intention", "execution"));
    }

    @Test @DisplayName("empty strings, equal strings, single characters")
    void edges() {
        assertEquals(0, EditDistance.editDistance("", ""));
        assertEquals(3, EditDistance.editDistance("", "abc"));
        assertEquals(3, EditDistance.editDistance("abc", ""));
        assertEquals(0, EditDistance.editDistance("same", "same"));
        assertEquals(1, EditDistance.editDistance("a", "b"));
        assertEquals(1, EditDistance.editDistance("a", "ab"));
        assertEquals(2, EditDistance.editDistance("ab", "ba"));
    }

    @Test @DisplayName("symmetry, and either string can be the longer one")
    void symmetry() {
        assertEquals(EditDistance.editDistance("kitten", "sitting"), EditDistance.editDistance("sitting", "kitten"));
        assertEquals(3, EditDistance.editDistance("kitten", "sitting"));
        assertEquals(3, EditDistance.editDistance("sunday", "saturday"));
    }

    static int brute(String a, String b, int i, int j, Map<Long, Integer> memo) {
        if (i == a.length()) return b.length() - j;
        if (j == b.length()) return a.length() - i;
        long key = (long) i * 100 + j;
        Integer c = memo.get(key);
        if (c != null) return c;
        int r = a.charAt(i) == b.charAt(j) ? brute(a, b, i + 1, j + 1, memo)
                : 1 + Math.min(brute(a, b, i + 1, j + 1, memo), Math.min(brute(a, b, i + 1, j, memo), brute(a, b, i, j + 1, memo)));
        memo.put(key, r);
        return r;
    }

    @Test @DisplayName("random short strings against a memoised recursion")
    void random() {
        Random rnd = new Random(14);
        for (int t = 0; t < 1000; t++) {
            StringBuilder x = new StringBuilder(), y = new StringBuilder();
            for (int i = 0, n = rnd.nextInt(9); i < n; i++) x.append((char) ('a' + rnd.nextInt(3)));
            for (int i = 0, n = rnd.nextInt(9); i < n; i++) y.append((char) ('a' + rnd.nextInt(3)));
            assertEquals(brute(x.toString(), y.toString(), 0, 0, new HashMap<>()),
                    EditDistance.editDistance(x.toString(), y.toString()), x + " -> " + y);
        }
    }

    @Test @DisplayName("5000 x 5000 characters: O(n*m) time and no 25M-cell table")
    void large() {
        Random rnd = new Random(3);
        StringBuilder x = new StringBuilder(), y = new StringBuilder();
        for (int i = 0; i < 5000; i++) { x.append((char) ('a' + rnd.nextInt(4))); y.append((char) ('a' + rnd.nextInt(4))); }
        assertTimeoutPreemptively(Duration.ofSeconds(5), () -> {
            int d = EditDistance.editDistance(x.toString(), y.toString());
            assertTrue(d > 0 && d <= 5000);
        });
        assertTimeoutPreemptively(Duration.ofSeconds(5), () ->
                assertEquals(0, EditDistance.editDistance("a".repeat(8000), "a".repeat(8000))));
    }
}
