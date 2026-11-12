package arrays;

// Maximum Gap  [Hard | Buckets / pigeonhole]
// Largest difference between successive elements once sorted. O(n), no comparison sort.
//
// [3,6,9,1] -> 3
// [10]      -> 0

public class MaximumGap {

    static int maximumGap(int[] nums) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(maximumGap(new int[]{3, 6, 9, 1})); // 3
        System.out.println(maximumGap(new int[]{10}));         // 0
    }
}
