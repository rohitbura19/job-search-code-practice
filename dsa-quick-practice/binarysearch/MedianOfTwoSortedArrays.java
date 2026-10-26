package binarysearch;

// Median of Two Sorted Arrays  [Hard | Binary search on partition]
// Median of the union of two sorted arrays in O(log(min(n, m))).
//
// [1,3] & [2]   -> 2.0
// [1,2] & [3,4] -> 2.5

public class MedianOfTwoSortedArrays {

    static double findMedianSortedArrays(int[] a, int[] b) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(findMedianSortedArrays(new int[]{1, 3}, new int[]{2}));    // 2.0
        System.out.println(findMedianSortedArrays(new int[]{1, 2}, new int[]{3, 4})); // 2.5
    }
}
