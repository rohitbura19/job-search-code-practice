package practice.d20261022.c2;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.*;
import org.junit.jupiter.api.Test;

class CoinChangeTest {
    @Test void examples() {
        assertEquals(3, CoinChange.minCoins(new int[]{1, 2, 5}, 11));
        assertEquals(-1, CoinChange.minCoins(new int[]{2}, 3));
        assertEquals(0, CoinChange.minCoins(new int[]{1}, 0));
        assertEquals(2, CoinChange.minCoins(new int[]{1, 3, 4}, 6));          // greedy would give 3
        assertEquals(4, CoinChange.countWays(new int[]{1, 2, 5}, 5));
        assertEquals(0, CoinChange.countWays(new int[]{2}, 3));
        assertEquals(1, CoinChange.countWays(new int[]{10}, 10));
        assertEquals(1, CoinChange.countWays(new int[]{7}, 0));
    }

    @Test void edgeCases() {
        assertEquals(0, CoinChange.minCoins(new int[0], 0));
        assertEquals(-1, CoinChange.minCoins(new int[0], 5));
        assertEquals(1, CoinChange.countWays(new int[0], 0));
        assertEquals(0, CoinChange.countWays(new int[0], 5));
        assertEquals(-1, CoinChange.minCoins(new int[]{5, 10}, 3));             // every coin larger than amount
        assertEquals(2, CoinChange.minCoins(new int[]{Integer.MAX_VALUE, 1}, 2));
        assertEquals(1, CoinChange.minCoins(new int[]{2, 2, 5, 5}, 5));          // duplicate denominations
    }

    static int bruteMin(int[] coins, int amt) {
        if (amt == 0) return 0;
        int best = -1;
        for (int c : coins) {
            if (c <= amt) {
                int sub = bruteMin(coins, amt - c);
                if (sub >= 0 && (best < 0 || sub + 1 < best)) best = sub + 1;
            }
        }
        return best;
    }

    static long bruteWays(int[] coins, int i, int amt) {
        if (amt == 0) return 1;
        if (i == coins.length || amt < 0) return 0;
        return bruteWays(coins, i, amt - coins[i]) + bruteWays(coins, i + 1, amt);
    }

    @Test void randomAgainstBrute() {
        Random r = new Random(6);
        for (int t = 0; t < 300; t++) {
            int[] coins = r.ints(1 + r.nextInt(3), 1, 8).distinct().toArray();
            int amt = r.nextInt(25);
            assertEquals(bruteMin(coins, amt), CoinChange.minCoins(coins, amt), Arrays.toString(coins) + " " + amt);
            assertEquals(bruteWays(coins, 0, amt), CoinChange.countWays(coins, amt), Arrays.toString(coins) + " " + amt);
        }
    }

    @Test void largeAmount() {
        int[] coins = {1, 5, 10, 25, 50, 100, 200};
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {
            assertEquals(500_000 / 200, CoinChange.minCoins(coins, 500_000));
            assertTrue(CoinChange.countWays(coins, 100_000) > 0);
        });
    }
}
