package dp;

// Burst Balloons  [Hard | Interval DP: pick the LAST balloon in a range]
// Bursting i earns nums[i-1] * nums[i] * nums[i+1] (missing neighbour = 1). Max total.
//
// [3,1,5,8] -> 167
// [1,5]     -> 10

public class BurstBalloons {

    static int maxCoins(int[] nums) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(maxCoins(new int[]{3, 1, 5, 8})); // 167
        System.out.println(maxCoins(new int[]{1, 5}));       // 10
    }
}
