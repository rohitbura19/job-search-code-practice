package dp;

// Partition Equal Subset Sum  [Medium | 0/1 knapsack on sum/2]
// Can the array be split into two subsets with equal sum?
//
// [1,5,11,5] -> true
// [1,2,3,5]  -> false

public class PartitionEqualSubsetSum {

    static boolean canPartition(int[] nums) {
        // TODO
        return false;
    }

    public static void main(String[] args) {
        System.out.println(canPartition(new int[]{1, 5, 11, 5})); // true
        System.out.println(canPartition(new int[]{1, 2, 3, 5}));  // false
    }
}
