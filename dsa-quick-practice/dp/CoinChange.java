package dp;

// Coin Change  [Medium | 1-D DP]
// Fewest coins (unlimited supply) to make amount, or -1.
//
// [1,2,5], 11 -> 3
// [2], 3      -> -1
// [1,3,4], 6  -> 2   (greedy gives 3)

public class CoinChange {

    static int coinChange(int[] coins, int amount) {
        // TODO
        return -1;
    }

    public static void main(String[] args) {
        System.out.println(coinChange(new int[]{1, 2, 5}, 11)); // 3
        System.out.println(coinChange(new int[]{2}, 3));        // -1
        System.out.println(coinChange(new int[]{1, 3, 4}, 6));  // 2
    }
}
