package arrays;

import java.util.List;

// Find All Duplicates in an Array  [Medium | Index marking]
// Values in 1..n, each once or twice. Return the ones seen twice, ascending. O(1) extra space.
//
// [4,3,2,7,8,2,3,1] -> [2,3]

public class FindAllDuplicates {

    static List<Integer> findDuplicates(int[] nums) {
        // TODO
        return List.of();
    }

    public static void main(String[] args) {
        System.out.println(findDuplicates(new int[]{4, 3, 2, 7, 8, 2, 3, 1})); // [2, 3]
    }
}
