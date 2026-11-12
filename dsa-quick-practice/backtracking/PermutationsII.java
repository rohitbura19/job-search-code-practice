package backtracking;

import java.util.List;

// Permutations II  [Medium | Sort + skip used duplicates]
// All distinct permutations (input may have duplicates).
//
// [1,1,2] -> [[1,1,2],[1,2,1],[2,1,1]]

public class PermutationsII {

    static List<List<Integer>> permuteUnique(int[] nums) {
        // TODO
        return List.of();
    }

    public static void main(String[] args) {
        System.out.println(permuteUnique(new int[]{1, 1, 2})); // [[1, 1, 2], [1, 2, 1], [2, 1, 1]]
    }
}
