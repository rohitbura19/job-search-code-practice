package arrays;

// Best Time to Buy and Sell Stock  [Easy | One pass, track min]
// Buy one day, sell on a later day. Max profit, or 0.
//
// [7,1,5,3,6,4] -> 5
// [7,6,4,3,1]   -> 0

public class BestTimeToBuySellStock {

    static int maxProfit(int[] prices) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(maxProfit(new int[]{7, 1, 5, 3, 6, 4})); // 5
        System.out.println(maxProfit(new int[]{7, 6, 4, 3, 1}));    // 0
    }
}
