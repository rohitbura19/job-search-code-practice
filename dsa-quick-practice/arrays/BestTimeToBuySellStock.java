package arrays;

// Best Time to Buy and Sell Stock  [Easy | One pass, track min]
// Buy one day, sell on a later day. Max profit, or 0.
//
// [7,1,5,3,6,4] -> 5
// [7,6,4,3,1]   -> 0

public class BestTimeToBuySellStock {

    static int maxProfit(int[] prices) {

        int result = 0;

        int maxRight = prices[prices.length-1];
        for(int i=prices.length-2;i>=0;i--){
            if(prices[i] > maxRight){
                maxRight = prices[i];
            }else{
                result = Math.max(result, maxRight - prices[i]);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(maxProfit(new int[]{7, 1, 5, 3, 6, 4})); // 5
        System.out.println(maxProfit(new int[]{7, 6, 4, 3, 1}));    // 0
    }
}
