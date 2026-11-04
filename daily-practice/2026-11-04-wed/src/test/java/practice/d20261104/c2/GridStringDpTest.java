package practice.d20261104.c2;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.*;
import org.junit.jupiter.api.Test;

class GridStringDpTest {
    @Test void wordBreakExamples() {
        assertTrue(GridStringDp.wordBreak("leetcode", List.of("leet", "code")));
        assertTrue(GridStringDp.wordBreak("applepenapple", List.of("apple", "pen")));
        assertFalse(GridStringDp.wordBreak("catsandog", List.of("cats", "dog", "sand", "and", "cat")));
        assertFalse(GridStringDp.wordBreak("a", List.of()));
        assertTrue(GridStringDp.wordBreak("aaaa", List.of("a")));
        assertFalse(GridStringDp.wordBreak("aaaab", List.of("a", "aa", "aaa")));
    }

    @Test void wordBreakAdversarialInputIsFast() {
        String s = "a".repeat(10_000) + "b";
        List<String> dict = new ArrayList<>();
        for (int i = 1; i <= 20; i++) dict.add("a".repeat(i));
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> assertFalse(GridStringDp.wordBreak(s, dict)));
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> assertTrue(GridStringDp.wordBreak("a".repeat(10_000), dict)));
    }

    @Test void minPathSumExamples() {
        assertEquals(7, GridStringDp.minPathSum(new int[][]{{1, 3, 1}, {1, 5, 1}, {4, 2, 1}}));
        assertEquals(12, GridStringDp.minPathSum(new int[][]{{1, 2, 3}, {4, 5, 6}}));
        assertEquals(5, GridStringDp.minPathSum(new int[][]{{5}}));
        assertEquals(6, GridStringDp.minPathSum(new int[][]{{1, 2, 3}}));
        assertEquals(6, GridStringDp.minPathSum(new int[][]{{1}, {2}, {3}}));
        assertEquals(0, GridStringDp.minPathSum(new int[][]{{0, 0}, {0, 0}}));
    }

    @Test void palindromicSubstringsExamples() {
        assertEquals(3, GridStringDp.countPalindromicSubstrings("abc"));
        assertEquals(6, GridStringDp.countPalindromicSubstrings("aaa"));
        assertEquals(1, GridStringDp.countPalindromicSubstrings("z"));
        assertEquals(0, GridStringDp.countPalindromicSubstrings(""));
        assertEquals(8, GridStringDp.countPalindromicSubstrings("abaab"));   // 5 singles + aba, aa, baab
    }

    @Test void randomVsBruteForce() {
        Random rnd = new Random(21);
        for (int t = 0; t < 300; t++) {
            int n = rnd.nextInt(14);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < n; i++) sb.append((char) ('a' + rnd.nextInt(2)));
            String s = sb.toString();
            assertEquals(brutePal(s), GridStringDp.countPalindromicSubstrings(s), s);
            if (n > 0) {
                List<String> dict = new ArrayList<>();
                for (int i = 0; i < 3; i++) dict.add("a".repeat(1 + rnd.nextInt(3)) + (rnd.nextBoolean() ? "b" : ""));
                assertEquals(bruteBreak(s, dict, 0), GridStringDp.wordBreak(s, dict), s + " " + dict);
            }
            int r = 1 + rnd.nextInt(4), c = 1 + rnd.nextInt(4);
            int[][] g = new int[r][c];
            for (int[] row : g) for (int j = 0; j < c; j++) row[j] = rnd.nextInt(10);
            assertEquals(brutePath(g, 0, 0), GridStringDp.minPathSum(g), Arrays.deepToString(g));
        }
    }

    @Test void largeInputsAreQuadraticAtWorst() {
        int[][] g = new int[1000][1000];
        for (int[] row : g) Arrays.fill(row, 1);
        String s = "a".repeat(10_000);
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> {
            assertEquals(1999, GridStringDp.minPathSum(g));
            assertEquals(10_000 * 10_001 / 2, GridStringDp.countPalindromicSubstrings(s));
        });
    }

    static int brutePal(String s) {
        int c = 0;
        for (int i = 0; i < s.length(); i++)
            for (int j = i; j < s.length(); j++) if (new StringBuilder(s.substring(i, j + 1)).reverse().toString().equals(s.substring(i, j + 1))) c++;
        return c;
    }

    static boolean bruteBreak(String s, List<String> dict, int from) {
        if (from == s.length()) return true;
        for (String w : dict) if (s.startsWith(w, from) && bruteBreak(s, dict, from + w.length())) return true;
        return false;
    }

    static int brutePath(int[][] g, int i, int j) {
        if (i == g.length - 1 && j == g[0].length - 1) return g[i][j];
        int best = Integer.MAX_VALUE;
        if (i + 1 < g.length) best = Math.min(best, brutePath(g, i + 1, j));
        if (j + 1 < g[0].length) best = Math.min(best, brutePath(g, i, j + 1));
        return g[i][j] + best;
    }
}
