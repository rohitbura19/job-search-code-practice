package dp;

// Target Sum  [Medium | Count subsets with sum (total + target) / 2]
// Put + or - before each number; ways to reach target.
//
// [1,1,1,1,1], 3 -> 5
// [1], 1         -> 1

public class TargetSum {

    static int findTargetSumWays(int[] nums, int target) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(findTargetSumWays(new int[]{1, 1, 1, 1, 1}, 3)); // 5
        System.out.println(findTargetSumWays(new int[]{1}, 1));             // 1
    }
}
