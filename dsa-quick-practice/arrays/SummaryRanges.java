package arrays;

import java.util.List;

// Summary Ranges  [Easy | One pass]
// Sorted, no duplicates. Summarise consecutive runs.
//
// [0,1,2,4,5,7]   -> [0->2, 4->5, 7]
// [0,2,3,4,6,8,9] -> [0, 2->4, 6, 8->9]

public class SummaryRanges {

    static List<String> summaryRanges(int[] nums) {
        // TODO
        return List.of();
    }

    public static void main(String[] args) {
        System.out.println(summaryRanges(new int[]{0, 1, 2, 4, 5, 7}));    // [0->2, 4->5, 7]
        System.out.println(summaryRanges(new int[]{0, 2, 3, 4, 6, 8, 9})); // [0, 2->4, 6, 8->9]
    }
}
