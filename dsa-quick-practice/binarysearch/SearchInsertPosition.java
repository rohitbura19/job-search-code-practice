package binarysearch;

// Search Insert Position  [Easy | Binary search, lower bound]
// Sorted distinct array: index of target, or where it would be inserted.
//
// [1,3,5,6], 5 -> 2
// [1,3,5,6], 2 -> 1
// [1,3,5,6], 7 -> 4

public class SearchInsertPosition {

    static int searchInsert(int[] nums, int target) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        int[] a = {1, 3, 5, 6};
        System.out.println(searchInsert(a, 5)); // 2
        System.out.println(searchInsert(a, 2)); // 1
        System.out.println(searchInsert(a, 7)); // 4
    }
}
