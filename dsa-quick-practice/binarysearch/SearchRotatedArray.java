package binarysearch;

// Search in Rotated Sorted Array  [Medium | Binary search]
// Sorted distinct array rotated at an unknown pivot. Index of target or -1. O(log n).
//
// [4,5,6,7,0,1,2], 0 -> 4
// [4,5,6,7,0,1,2], 3 -> -1

public class SearchRotatedArray {

    static int search(int[] nums, int target) {
        // TODO
        return -1;
    }

    public static void main(String[] args) {
        int[] a = {4, 5, 6, 7, 0, 1, 2};
        System.out.println(search(a, 0)); // 4
        System.out.println(search(a, 3)); // -1
    }
}
