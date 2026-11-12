package dp;

// Best Time to Buy and Sell Stock with Cooldown  [Medium | States: hold, sold, rest]
// Unlimited transactions; after a sell, skip one day before buying.
//
// [1,2,3,0,2] -> 3
// [1]         -> 0

public class StockWithCooldown {

    static int maxProfit(int[] prices) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(maxProfit(new int[]{1, 2, 3, 0, 2})); // 3
        System.out.println(maxProfit(new int[]{1}));             // 0
    }
}
