package binarysearch;

// Split Array Largest Sum  [Hard | Binary search on the answer + greedy check]
// Split into k contiguous parts; minimise the largest part sum.
//
// [7,2,5,10,8], k=2 -> 18
// [1,2,3,4,5], k=2  -> 9
// [1,4,4], k=3      -> 4

public class SplitArrayLargestSum {

    static int splitArray(int[] nums, int k) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(splitArray(new int[]{7, 2, 5, 10, 8}, 2)); // 18
        System.out.println(splitArray(new int[]{1, 2, 3, 4, 5}, 2));  // 9
        System.out.println(splitArray(new int[]{1, 4, 4}, 3));        // 4
    }
}
