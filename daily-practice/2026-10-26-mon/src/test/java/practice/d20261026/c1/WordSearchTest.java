package practice.d20261026.c1;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.Arrays;
import java.util.Random;
import org.junit.jupiter.api.Test;

class WordSearchTest {
    static char[][] grid(String... rows) {
        char[][] g = new char[rows.length][];
        for (int i = 0; i < rows.length; i++) g[i] = rows[i].toCharArray();
        return g;
    }

    static final String[] CLASSIC = {"ABCE", "SFCS", "ADEE"};

    @Test void classicExamples() {
        assertTrue(WordSearch.exist(grid(CLASSIC), "ABCCED"));
        assertTrue(WordSearch.exist(grid(CLASSIC), "SEE"));
        assertFalse(WordSearch.exist(grid(CLASSIC), "ABCB"), "a cell cannot be reused");
    }

    @Test void edges() {
        assertTrue(WordSearch.exist(grid("a"), "a"));
        assertFalse(WordSearch.exist(grid("a"), "aa"));
        assertFalse(WordSearch.exist(grid("a"), "b"));
        assertFalse(WordSearch.exist(grid("ab", "cd"), "abcd"), "b and c are diagonal, so a-b-c-d is not a path");
        assertTrue(WordSearch.exist(grid("ab", "cd"), "abdc"));
        assertTrue(WordSearch.exist(grid("abcde"), "edcba"), "paths may go left");
    }

    @Test void boardIsRestoredAfterTheSearch() {
        char[][] g = grid(CLASSIC);
        WordSearch.exist(g, "ABCCED");
        WordSearch.countPaths(g, "SEE");
        assertArrayEquals(grid(CLASSIC)[1], g[1]);
        assertEquals("ABCE", new String(g[0]));
    }

    @Test void countPaths() {
        assertEquals(8, WordSearch.countPaths(grid("aa", "aa"), "aaa"));
        assertEquals(0, WordSearch.countPaths(grid("aa", "aa"), "aaaaa"));
        assertEquals(2, WordSearch.countPaths(grid("aba"), "a") );
        assertEquals(2, WordSearch.countPaths(grid("aba"), "ab"), "a-b from the left and from the right");
        assertEquals(1, WordSearch.countPaths(grid(CLASSIC), "ABCCED"));
    }

    /** Independent brute force: enumerate every simple path. */
    static long brute(char[][] g, String w) {
        long n = 0;
        for (int r = 0; r < g.length; r++) for (int c = 0; c < g[0].length; c++) n += walk(g, w, r, c, 0, new boolean[g.length][g[0].length]);
        return n;
    }
    static long walk(char[][] g, String w, int r, int c, int k, boolean[][] seen) {
        if (r < 0 || c < 0 || r >= g.length || c >= g[0].length || seen[r][c] || g[r][c] != w.charAt(k)) return 0;
        if (k == w.length() - 1) return 1;
        seen[r][c] = true;
        long n = walk(g, w, r + 1, c, k + 1, seen) + walk(g, w, r - 1, c, k + 1, seen) + walk(g, w, r, c + 1, k + 1, seen) + walk(g, w, r, c - 1, k + 1, seen);
        seen[r][c] = false;
        return n;
    }

    @Test void randomAgainstBruteForce() {
        Random rnd = new Random(8);
        for (int t = 0; t < 1500; t++) {
            int rows = 1 + rnd.nextInt(3), cols = 1 + rnd.nextInt(4);
            char[][] g = new char[rows][cols];
            for (char[] row : g) for (int i = 0; i < cols; i++) row[i] = (char) ('a' + rnd.nextInt(2));
            char[] w = new char[1 + rnd.nextInt(6)];
            for (int i = 0; i < w.length; i++) w[i] = (char) ('a' + rnd.nextInt(2));
            String word = new String(w);
            long expected = brute(g, word);
            assertEquals(expected, WordSearch.countPaths(g, word), Arrays.deepToString(g) + " " + word);
            assertEquals(expected > 0, WordSearch.exist(g, word), Arrays.deepToString(g) + " " + word);
        }
    }

    @Test void missingLetterMustBeRejectedFast() {
        char[][] g = new char[6][6];
        for (char[] row : g) Arrays.fill(row, 'a');
        String word = "a".repeat(35) + "b";
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {
            assertFalse(WordSearch.exist(g, word));
            assertEquals(0, WordSearch.countPaths(g, word));
        });
    }

    @Test void longWordOnABigBoard() {
        char[][] g = new char[200][200];
        for (char[] row : g) Arrays.fill(row, 'a');
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> assertTrue(WordSearch.exist(g, "a".repeat(40))));
    }
}
