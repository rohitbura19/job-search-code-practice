package backtracking;

import java.util.List;

// Combination Sum  [Medium | Backtracking]
// Distinct positive candidates, each reusable. All unique combinations summing to target.
//
// [2,3,6,7], 7 -> [[2,2,3],[7]]
// [2,3,5], 8   -> [[2,2,2,2],[2,3,3],[3,5]]

public class CombinationSum {

    static List<List<Integer>> combinationSum(int[] candidates, int target) {
        // TODO
        return List.of();
    }

    public static void main(String[] args) {
        System.out.println(combinationSum(new int[]{2, 3, 6, 7}, 7)); // [[2, 2, 3], [7]]
        System.out.println(combinationSum(new int[]{2, 3, 5}, 8));    // [[2, 2, 2, 2], [2, 3, 3], [3, 5]]
    }
}
