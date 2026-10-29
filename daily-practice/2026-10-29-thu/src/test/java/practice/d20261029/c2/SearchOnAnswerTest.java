package practice.d20261029.c2;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.Random;
import org.junit.jupiter.api.Test;

class SearchOnAnswerTest {
    @Test void kokoExamples() {
        assertEquals(4, SearchOnAnswer.minEatingSpeed(new int[]{3, 6, 7, 11}, 8));
        assertEquals(30, SearchOnAnswer.minEatingSpeed(new int[]{30, 11, 23, 4, 20}, 5));
        assertEquals(23, SearchOnAnswer.minEatingSpeed(new int[]{30, 11, 23, 4, 20}, 6));
        assertEquals(1, SearchOnAnswer.minEatingSpeed(new int[]{1}, 1));
        assertEquals(1, SearchOnAnswer.minEatingSpeed(new int[]{1, 1, 1}, 3));
    }

    @Test void kokoOverflow() {
        int[] piles = new int[100_000];
        java.util.Arrays.fill(piles, 1_000_000_000);
        assertEquals(1, SearchOnAnswer.minEatingSpeed(new int[]{1_000_000_000}, 1_000_000_000));
        assertEquals(1_000_000_000, SearchOnAnswer.minEatingSpeed(piles, 100_000));
        assertEquals(500_000_000, SearchOnAnswer.minEatingSpeed(piles, 200_000));
    }

    @Test void splitExamples() {
        assertEquals(18, SearchOnAnswer.splitArray(new int[]{7, 2, 5, 10, 8}, 2));
        assertEquals(9, SearchOnAnswer.splitArray(new int[]{1, 2, 3, 4, 5}, 2));
        assertEquals(4, SearchOnAnswer.splitArray(new int[]{1, 4, 4}, 3));
        assertEquals(5, SearchOnAnswer.splitArray(new int[]{5}, 1));
        assertEquals(0, SearchOnAnswer.splitArray(new int[]{0, 0, 0}, 2));
        assertEquals(15, SearchOnAnswer.splitArray(new int[]{1, 2, 3, 4, 5}, 1));
    }

    @Test void splitSumNearIntMax() {
        assertEquals(2_000_000_000, SearchOnAnswer.splitArray(new int[]{1_000_000_000, 1_000_000_000}, 1));
        assertEquals(1_000_000_000, SearchOnAnswer.splitArray(new int[]{1_000_000_000, 1_000_000_000}, 2));
    }

    @Test void randomAgainstBruteForce() {
        Random rnd = new Random(29);
        for (int round = 0; round < 1500; round++) {
            int n = 1 + rnd.nextInt(7);
            int[] a = new int[n];
            for (int i = 0; i < n; i++) a[i] = rnd.nextInt(12);
            int k = 1 + rnd.nextInt(n);
            assertEquals(splitDp(a, k), SearchOnAnswer.splitArray(a, k));
            int[] piles = new int[n];
            for (int i = 0; i < n; i++) piles[i] = 1 + rnd.nextInt(30);
            int h = n + rnd.nextInt(40);
            int s = 1;
            while (true) { long t = 0; for (int p : piles) t += (p + s - 1) / s; if (t <= h) break; s++; }
            assertEquals(s, SearchOnAnswer.minEatingSpeed(piles, h));
        }
    }

    static int splitDp(int[] a, int k) {
        int n = a.length;
        int[] pre = new int[n + 1];
        for (int i = 0; i < n; i++) pre[i + 1] = pre[i] + a[i];
        int[][] dp = new int[k + 1][n + 1];
        for (int[] row : dp) java.util.Arrays.fill(row, Integer.MAX_VALUE);
        dp[0][0] = 0;
        for (int j = 1; j <= k; j++)
            for (int i = j; i <= n; i++)
                for (int m = j - 1; m < i; m++)
                    if (dp[j - 1][m] != Integer.MAX_VALUE) dp[j][i] = Math.min(dp[j][i], Math.max(dp[j - 1][m], pre[i] - pre[m]));
        return dp[k][n];
    }

    @Test void largeInput() {
        int n = 500_000;
        int[] a = new int[n];
        Random rnd = new Random(3);
        for (int i = 0; i < n; i++) a[i] = rnd.nextInt(1000);
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> {
            SearchOnAnswer.splitArray(a, 1000);
            SearchOnAnswer.minEatingSpeed(a, n + 10);
        });
    }
}
