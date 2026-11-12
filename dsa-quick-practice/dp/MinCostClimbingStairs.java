package dp;

// Min Cost Climbing Stairs  [Easy | 1-D DP, O(1) space]
// Pay cost[i] to leave step i, move 1 or 2. Start at 0 or 1, reach the top (index n).
//
// [10,15,20]                       -> 15
// [1,100,1,1,1,100,1,1,100,1]      -> 6

public class MinCostClimbingStairs {

    static int minCostClimbingStairs(int[] cost) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(minCostClimbingStairs(new int[]{10, 15, 20}));                         // 15
        System.out.println(minCostClimbingStairs(new int[]{1, 100, 1, 1, 1, 100, 1, 1, 100, 1})); // 6
    }
}
