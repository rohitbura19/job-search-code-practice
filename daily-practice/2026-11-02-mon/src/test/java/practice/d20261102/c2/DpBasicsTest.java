package practice.d20261102.c2;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.*;
import org.junit.jupiter.api.Test;

class DpBasicsTest {
    @Test void minExamples() {
        assertEquals(3, DpBasics.coinChangeMin(new int[]{1, 2, 5}, 11));
        assertEquals(-1, DpBasics.coinChangeMin(new int[]{2}, 3));
        assertEquals(0, DpBasics.coinChangeMin(new int[]{1}, 0));
        assertEquals(0, DpBasics.coinChangeMin(new int[]{}, 0));
        assertEquals(-1, DpBasics.coinChangeMin(new int[]{}, 5));
        assertEquals(2, DpBasics.coinChangeMin(new int[]{1, 3, 4}, 6)); // greedy gives 3
    }

    @Test void minRandomAgainstBfs() {
        Random r = new Random(10);
        for (int t = 0; t < 300; t++) {
            int[] coins = r.ints(1 + r.nextInt(4), 1, 9).distinct().toArray();
            int amount = r.nextInt(40);
            assertEquals(bfs(coins, amount), DpBasics.coinChangeMin(coins, amount), Arrays.toString(coins) + " " + amount);
        }
    }

    @Test void waysExamples() {
        assertEquals(4, DpBasics.coinWays(new int[]{1, 2, 5}, 5));   // 5, 2+2+1, 2+1+1+1, 1*5
        assertEquals(0, DpBasics.coinWays(new int[]{2}, 3));
        assertEquals(1, DpBasics.coinWays(new int[]{10}, 10));
        assertEquals(1, DpBasics.coinWays(new int[]{}, 0));
        assertEquals(0, DpBasics.coinWays(new int[]{}, 3));
        assertEquals(73_682, DpBasics.coinWays(new int[]{1, 2, 5, 10, 20, 50, 100, 200}, 200));
    }

    @Test void waysRandomAgainstRecursion() {
        Random r = new Random(11);
        for (int t = 0; t < 200; t++) {
            int[] coins = r.ints(1 + r.nextInt(4), 1, 8).distinct().toArray();
            int amount = r.nextInt(30);
            assertEquals(rec(coins, 0, amount), DpBasics.coinWays(coins, amount), Arrays.toString(coins) + " " + amount);
        }
    }

    @Test void partitionExamples() {
        assertTrue(DpBasics.canPartition(new int[]{1, 5, 11, 5}));
        assertFalse(DpBasics.canPartition(new int[]{1, 2, 3, 5}));
        assertTrue(DpBasics.canPartition(new int[]{}));
        assertFalse(DpBasics.canPartition(new int[]{7}));
        assertTrue(DpBasics.canPartition(new int[]{3, 3}));
        assertFalse(DpBasics.canPartition(new int[]{1, 1, 1})); // odd sum
        assertFalse(DpBasics.canPartition(new int[]{100, 1, 1, 1}));
    }

    @Test void partitionRandomAgainstSubsets() {
        Random r = new Random(12);
        for (int t = 0; t < 300; t++) {
            int[] a = r.ints(r.nextInt(12), 1, 10).toArray();
            int sum = Arrays.stream(a).sum();
            boolean exp = false;
            for (int mask = 0; mask < (1 << a.length) && !exp; mask++) {
                int s = 0;
                for (int i = 0; i < a.length; i++) if ((mask >> i & 1) == 1) s += a[i];
                exp = s * 2 == sum;
            }
            assertEquals(exp, DpBasics.canPartition(a), Arrays.toString(a));
        }
    }

    @Test void largeInputsWithinTimeout() {
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {
            assertEquals(1000, DpBasics.coinChangeMin(new int[]{500}, 500_000));
            assertEquals(1000, DpBasics.coinChangeMin(new int[]{1, 7, 13, 99, 500}, 500_000));
            int[] nums = new int[200];
            Arrays.fill(nums, 100);
            assertTrue(DpBasics.canPartition(nums));
            nums[0] = 99;
            assertFalse(DpBasics.canPartition(nums));
        });
    }

    private static int bfs(int[] coins, int amount) {
        int[] dist = new int[amount + 1];
        Arrays.fill(dist, -1);
        dist[0] = 0;
        ArrayDeque<Integer> q = new ArrayDeque<>(List.of(0));
        while (!q.isEmpty()) {
            int cur = q.poll();
            for (int c : coins) {
                int nx = cur + c;
                if (nx <= amount && dist[nx] < 0) { dist[nx] = dist[cur] + 1; q.add(nx); }
            }
        }
        return dist[amount];
    }

    private static long rec(int[] coins, int i, int left) {
        if (left == 0) return 1;
        if (i == coins.length || left < 0) return 0;
        return rec(coins, i, left - coins[i]) + rec(coins, i + 1, left);
    }
}
