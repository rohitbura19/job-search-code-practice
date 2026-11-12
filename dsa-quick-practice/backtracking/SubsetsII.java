package backtracking;

import java.util.List;

// Subsets II  [Medium | Sort + skip duplicates at the same depth]
// All distinct subsets (input may have duplicates). Any order.
//
// [1,2,2] -> [[],[1],[1,2],[1,2,2],[2],[2,2]]

public class SubsetsII {

    static List<List<Integer>> subsetsWithDup(int[] nums) {
        // TODO
        return List.of();
    }

    public static void main(String[] args) {
        System.out.println(subsetsWithDup(new int[]{1, 2, 2})); // [[], [1], [1, 2], [1, 2, 2], [2], [2, 2]]  (any order)
    }
}
