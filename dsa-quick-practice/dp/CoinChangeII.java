package dp;

// Coin Change II  [Medium | 1-D DP, loop order matters]
// Number of combinations (order doesn't matter) that make amount.
//
// [1,2,5], 5 -> 4
// [2], 3     -> 0

public class CoinChangeII {

    static int change(int amount, int[] coins) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(change(5, new int[]{1, 2, 5})); // 4
        System.out.println(change(3, new int[]{2}));       // 0
    }
}
