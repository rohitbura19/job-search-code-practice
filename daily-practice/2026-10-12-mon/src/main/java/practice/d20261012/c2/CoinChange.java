package practice.d20261012.c2;

import java.util.List;

/** C2 - see questions/C2-coin-change.md. Test: CoinChangeTest. */
public final class CoinChange {
    private CoinChange() {}

    /** Fewest coins to make amount (unlimited supply of each), or -1. amount 0 -> 0. */
    public static int coinChange(int[] coins, int amount) {
        throw new UnsupportedOperationException("TODO");
    }

    /** Coin Change II: number of COMBINATIONS (order does not matter) that make amount. amount 0 -> 1. Which loop goes outside? */
    public static long countWays(int[] coins, int amount) {
        throw new UnsupportedOperationException("TODO");
    }

    /** An actual optimal selection of coins (any order), or null if impossible; amount 0 -> empty list. Keep choice[a] and walk back. */
    public static List<Integer> coinsUsed(int[] coins, int amount) {
        throw new UnsupportedOperationException("TODO");
    }
}
