package slidingwindow;

// Minimum Size Subarray Sum  [Medium | Shrinking window]
// All positive. Shortest subarray with sum >= target; 0 if none.
//
// 7, [2,3,1,2,4,3]       -> 2
// 4, [1,4,4]             -> 1
// 11, [1,1,1,1,1,1,1,1]  -> 0

public class MinSizeSubarraySum {

    static int minSubArrayLen(int target, int[] nums) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(minSubArrayLen(7, new int[]{2, 3, 1, 2, 4, 3}));        // 2
        System.out.println(minSubArrayLen(4, new int[]{1, 4, 4}));                 // 1
        System.out.println(minSubArrayLen(11, new int[]{1, 1, 1, 1, 1, 1, 1, 1})); // 0
    }
}
