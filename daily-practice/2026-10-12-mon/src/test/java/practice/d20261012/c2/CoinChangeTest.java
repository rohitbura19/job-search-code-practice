package practice.d20261012.c2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class CoinChangeTest {

    @Test @DisplayName("examples from the question")
    void examples() {
        assertEquals(3, CoinChange.coinChange(new int[]{1, 2, 5}, 11));
        assertEquals(-1, CoinChange.coinChange(new int[]{2}, 3));
    }

    @Test @DisplayName("amount 0, empty coins, coin larger than amount")
    void edges() {
        assertEquals(0, CoinChange.coinChange(new int[]{1, 2}, 0));
        assertEquals(-1, CoinChange.coinChange(new int[]{}, 5));
        assertEquals(-1, CoinChange.coinChange(new int[]{7}, 5));
        assertEquals(1, CoinChange.coinChange(new int[]{7}, 7));
        assertEquals(0, CoinChange.coinChange(new int[]{}, 0));
    }

    @Test @DisplayName("greedy fails: [1,3,4] for 6 needs 2 coins (3+3), not 3 (4+1+1)")
    void greedyTrap() {
        assertEquals(2, CoinChange.coinChange(new int[]{1, 3, 4}, 6));
        assertEquals(20, CoinChange.coinChange(new int[]{186, 419, 83, 408}, 6249));
    }

    @Test @DisplayName("amount 1,000,000 with 7 coin values finishes fast")
    void bigIsFast() {
        int[] coins = {1, 2, 5, 10, 20, 50, 100};
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> assertEquals(10_000, CoinChange.coinChange(coins, 1_000_000)));
    }

    @Test @DisplayName("countWays: combinations, not permutations")
    void ways() {
        assertEquals(4, CoinChange.countWays(new int[]{1, 2, 5}, 5));
        assertEquals(0, CoinChange.countWays(new int[]{2}, 3));
        assertEquals(1, CoinChange.countWays(new int[]{10}, 10));
        assertEquals(2, CoinChange.countWays(new int[]{1, 2}, 3));       // {1,1,1} and {1,2}; 3 would mean permutations
        assertEquals(1, CoinChange.countWays(new int[]{1, 2, 5}, 0));
        assertEquals(1, CoinChange.countWays(new int[]{}, 0));
        assertEquals(0, CoinChange.countWays(new int[]{}, 3));
        assertEquals(73682, CoinChange.countWays(new int[]{1, 2, 5, 10, 20, 50, 100, 200}, 200));
    }

    @Test @DisplayName("countWays matches a brute-force recursion")
    void waysRandom() {
        Random rnd = new Random(4);
        for (int t = 0; t < 500; t++) {
            int n = rnd.nextInt(4) + 1; int[] coins = new int[n];
            for (int i = 0; i < n; i++) coins[i] = rnd.nextInt(6) + 1;
            int amount = rnd.nextInt(25);
            assertEquals(brute(coins, 0, amount), CoinChange.countWays(coins, amount), Arrays.toString(coins) + " " + amount);
        }
    }

    /** Brute force: coins may repeat, and a coin index never goes backwards, so each combination is counted once. */
    static long brute(int[] coins, int from, int left) {
        if (left == 0) return 1;
        long total = 0;
        for (int i = from; i < coins.length; i++) {
            if (coins[i] > left) continue;
            total += brute(coins, i, left - coins[i]);
        }
        return total;
    }

    @Test @DisplayName("coinChange matches a breadth-first search over amounts")
    void minRandom() {
        Random rnd = new Random(8);
        for (int t = 0; t < 500; t++) {
            int n = rnd.nextInt(4) + 1; int[] coins = new int[n];
            for (int i = 0; i < n; i++) coins[i] = rnd.nextInt(9) + 1;
            int amount = rnd.nextInt(40);
            assertEquals(bfs(coins, amount), CoinChange.coinChange(coins, amount), Arrays.toString(coins) + " " + amount);
        }
    }

    static int bfs(int[] coins, int amount) {
        int[] dist = new int[amount + 1]; Arrays.fill(dist, -1); dist[0] = 0;
        int[] q = new int[amount + 1]; int h = 0, tl = 0; q[tl++] = 0;
        while (h < tl) {
            int cur = q[h++];
            for (int c : coins) { int nx = cur + c; if (nx <= amount && dist[nx] < 0) { dist[nx] = dist[cur] + 1; q[tl++] = nx; } }
        }
        return dist[amount];
    }

    @Test @DisplayName("coinsUsed returns coins that sum to the amount using the minimum count")
    void reconstruct() {
        int[][][] cases = {{{1, 2, 5}, {11}}, {{1, 3, 4}, {6}}, {{2}, {3}}, {{5}, {0}}, {{186, 419, 83, 408}, {6249}}};
        for (int[][] cs : cases) {
            int[] coins = cs[0]; int amount = cs[1][0];
            List<Integer> used = CoinChange.coinsUsed(coins, amount);
            int best = CoinChange.coinChange(coins, amount);
            if (best == -1) { assertNull(used); continue; }
            assertNotNull(used);
            assertEquals(best, used.size(), "count for " + Arrays.toString(coins) + " " + amount);
            int sum = 0;
            for (int c : used) { sum += c; assertTrue(Arrays.stream(coins).anyMatch(x -> x == c), "coin " + c + " not allowed"); }
            assertEquals(amount, sum);
        }
    }
}
