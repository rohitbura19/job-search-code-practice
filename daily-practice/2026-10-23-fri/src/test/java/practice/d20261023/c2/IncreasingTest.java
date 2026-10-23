package practice.d20261023.c2;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.*;
import org.junit.jupiter.api.Test;

class IncreasingTest {
    static int bruteLis(int[] a) {
        int[] dp = new int[a.length];
        int best = 0;
        for (int i = 0; i < a.length; i++) {
            dp[i] = 1;
            for (int j = 0; j < i; j++) if (a[j] < a[i]) dp[i] = Math.max(dp[i], dp[j] + 1);
            best = Math.max(best, dp[i]);
        }
        return best;
    }

    static int bruteEnvelopes(int[][] e) {
        int[][] s = e.clone();
        Arrays.sort(s, Comparator.<int[]>comparingInt(x -> x[0]).thenComparingInt(x -> x[1]));
        int[] dp = new int[s.length];
        int best = 0;
        for (int i = 0; i < s.length; i++) {
            dp[i] = 1;
            for (int j = 0; j < i; j++) if (s[j][0] < s[i][0] && s[j][1] < s[i][1]) dp[i] = Math.max(dp[i], dp[j] + 1);
            best = Math.max(best, dp[i]);
        }
        return best;
    }

    @Test void lisExamples() {
        assertEquals(4, Increasing.lengthOfLIS(new int[]{10, 9, 2, 5, 3, 7, 101, 18}));
        assertEquals(4, Increasing.lengthOfLIS(new int[]{0, 1, 0, 3, 2, 3}));
        assertEquals(1, Increasing.lengthOfLIS(new int[]{7, 7, 7, 7}));
        assertEquals(0, Increasing.lengthOfLIS(new int[]{}));
        assertEquals(1, Increasing.lengthOfLIS(new int[]{42}));
        assertEquals(2, Increasing.lengthOfLIS(new int[]{Integer.MIN_VALUE, Integer.MAX_VALUE}));
        assertEquals(1, Increasing.lengthOfLIS(new int[]{5, 4, 3, 2, 1}));
    }

    @Test void envelopeExamples() {
        assertEquals(3, Increasing.maxEnvelopes(new int[][]{{5, 4}, {6, 4}, {6, 7}, {2, 3}}));
        assertEquals(1, Increasing.maxEnvelopes(new int[][]{{1, 1}, {1, 1}, {1, 1}}));
        assertEquals(4, Increasing.maxEnvelopes(new int[][]{{4, 5}, {4, 6}, {6, 7}, {2, 3}, {1, 1}}));
        assertEquals(0, Increasing.maxEnvelopes(new int[][]{}));
    }

    @Test void envelopesDoNotChainOnEqualWidth() {
        assertEquals(1, Increasing.maxEnvelopes(new int[][]{{3, 1}, {3, 2}, {3, 3}}));
    }

    @Test void callerArrayIsNotReordered() {
        int[][] e = {{5, 4}, {6, 4}, {6, 7}, {2, 3}};
        Increasing.maxEnvelopes(e);
        assertArrayEquals(new int[]{5, 4}, e[0]);
        assertArrayEquals(new int[]{2, 3}, e[3]);
    }

    @Test void randomAgainstQuadratic() {
        Random rnd = new Random(9);
        for (int t = 0; t < 1000; t++) {
            int[] a = new int[rnd.nextInt(25)];
            for (int i = 0; i < a.length; i++) a[i] = rnd.nextInt(12) - 4;
            assertEquals(bruteLis(a), Increasing.lengthOfLIS(a), Arrays.toString(a));
            int[][] e = new int[rnd.nextInt(20)][];
            for (int i = 0; i < e.length; i++) e[i] = new int[]{rnd.nextInt(6), rnd.nextInt(6)};
            assertEquals(bruteEnvelopes(e), Increasing.maxEnvelopes(e), Arrays.deepToString(e));
        }
    }

    @Test void largeInput() {
        int n = 200_000;
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = i;
        int[][] e = new int[100_000][];
        for (int i = 0; i < e.length; i++) e[i] = new int[]{i, i};
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {
            assertEquals(n, Increasing.lengthOfLIS(a));
            assertEquals(100_000, Increasing.maxEnvelopes(e));
        });
    }
}
