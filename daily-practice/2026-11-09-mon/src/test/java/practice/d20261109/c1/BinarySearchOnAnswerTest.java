package practice.d20261109.c1;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.Random;
import org.junit.jupiter.api.Test;

class BinarySearchOnAnswerTest {
    private static int bruteSpeed(int[] piles, int h) {
        for (int k = 1; ; k++) {
            long hours = 0;
            for (int p : piles) hours += (p + k - 1) / k;
            if (hours <= h) return k;
        }
    }

    private static long bruteSplit(int[] a, int k) {
        int n = a.length;
        long[][] dp = new long[k + 1][n + 1];
        long[] pre = new long[n + 1];
        for (int i = 0; i < n; i++) pre[i + 1] = pre[i] + a[i];
        for (long[] row : dp) java.util.Arrays.fill(row, Long.MAX_VALUE);
        dp[0][0] = 0;
        for (int p = 1; p <= k; p++)
            for (int i = 0; i <= n; i++)
                for (int j = 0; j <= i; j++)
                    if (dp[p - 1][j] != Long.MAX_VALUE) dp[p][i] = Math.min(dp[p][i], Math.max(dp[p - 1][j], pre[i] - pre[j]));
        long best = Long.MAX_VALUE;
        for (int p = 1; p <= k; p++) best = Math.min(best, dp[p][n]);
        return best;
    }

    @Test void bananaExamples() {
        assertEquals(4, BinarySearchOnAnswer.minEatingSpeed(new int[] {3, 6, 7, 11}, 8));
        assertEquals(30, BinarySearchOnAnswer.minEatingSpeed(new int[] {30, 11, 23, 4, 20}, 5));
        assertEquals(23, BinarySearchOnAnswer.minEatingSpeed(new int[] {30, 11, 23, 4, 20}, 6));
        assertEquals(1, BinarySearchOnAnswer.minEatingSpeed(new int[] {1}, 1));
        assertEquals(Integer.MAX_VALUE, BinarySearchOnAnswer.minEatingSpeed(new int[] {Integer.MAX_VALUE}, 1));
        assertEquals(1, BinarySearchOnAnswer.minEatingSpeed(new int[] {Integer.MAX_VALUE}, Integer.MAX_VALUE));
    }

    @Test void bananaRandomVsBrute() {
        Random r = new Random(11);
        for (int t = 0; t < 400; t++) {
            int n = 1 + r.nextInt(6);
            int[] p = new int[n];
            for (int i = 0; i < n; i++) p[i] = 1 + r.nextInt(30);
            int h = n + r.nextInt(20);
            assertEquals(bruteSpeed(p, h), BinarySearchOnAnswer.minEatingSpeed(p, h));
        }
    }

    @Test void shipExamples() {
        assertEquals(15, BinarySearchOnAnswer.shipWithinDays(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, 5));
        assertEquals(6, BinarySearchOnAnswer.shipWithinDays(new int[] {3, 2, 2, 4, 1, 4}, 3));
        assertEquals(3, BinarySearchOnAnswer.shipWithinDays(new int[] {1, 2, 3, 1, 1}, 4));
        assertEquals(7, BinarySearchOnAnswer.shipWithinDays(new int[] {7}, 5));
        assertEquals(10, BinarySearchOnAnswer.shipWithinDays(new int[] {1, 2, 3, 4}, 1));
    }

    @Test void splitExamples() {
        assertEquals(18, BinarySearchOnAnswer.splitArray(new int[] {7, 2, 5, 10, 8}, 2));
        assertEquals(9, BinarySearchOnAnswer.splitArray(new int[] {1, 2, 3, 4, 5}, 2));
        assertEquals(0, BinarySearchOnAnswer.splitArray(new int[] {0, 0, 0}, 2));
        assertEquals(4_294_967_294L, BinarySearchOnAnswer.splitArray(new int[] {Integer.MAX_VALUE, Integer.MAX_VALUE}, 1));
        assertEquals(Integer.MAX_VALUE, BinarySearchOnAnswer.splitArray(new int[] {Integer.MAX_VALUE, Integer.MAX_VALUE}, 2));
    }

    @Test void splitAndShipRandomVsBrute() {
        Random r = new Random(5);
        for (int t = 0; t < 400; t++) {
            int n = 1 + r.nextInt(8);
            int[] a = new int[n];
            for (int i = 0; i < n; i++) a[i] = r.nextInt(20);
            int k = 1 + r.nextInt(n);
            assertEquals(bruteSplit(a, k), BinarySearchOnAnswer.splitArray(a, k), java.util.Arrays.toString(a) + " k=" + k);
            for (int i = 0; i < n; i++) a[i] += 1;
            assertEquals(bruteSplit(a, k), BinarySearchOnAnswer.shipWithinDays(a, k));
        }
    }

    @Test void largeInputs() {
        int n = 200_000;
        int[] a = new int[n];
        Random r = new Random(1);
        for (int i = 0; i < n; i++) a[i] = 1 + r.nextInt(1_000_000);
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> {
            assertTrue(BinarySearchOnAnswer.minEatingSpeed(a, n + 1000) >= 1);
            assertTrue(BinarySearchOnAnswer.splitArray(a, 7) > 0);
            int[] w = new int[n];
            for (int i = 0; i < n; i++) w[i] = 1 + a[i] % 10_000;
            assertTrue(BinarySearchOnAnswer.shipWithinDays(w, 30) > 0);
        });
    }
}
