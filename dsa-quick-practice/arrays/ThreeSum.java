package arrays;

import java.util.List;

// 3Sum  [Medium | Sort + two pointers]
// All distinct triplets with a + b + c == 0.
//
// [-1,0,1,2,-1,-4] -> [[-1,-1,2],[-1,0,1]]
// [0,0,0]          -> [[0,0,0]]

public class ThreeSum {

    static List<List<Integer>> threeSum(int[] nums) {
        // TODO
        return List.of();
    }

    public static void main(String[] args) {
        System.out.println(threeSum(new int[]{-1, 0, 1, 2, -1, -4})); // [[-1, -1, 2], [-1, 0, 1]]
        System.out.println(threeSum(new int[]{0, 0, 0}));             // [[0, 0, 0]]
    }
}
