package heap;

// Kth Largest Element in an Array  [Medium | Min-heap of size k, or Quickselect]
// Return the k-th largest element (duplicates count).
//
// [3,2,1,5,6,4], k=2        -> 5
// [3,2,3,1,2,4,5,5,6], k=4  -> 4

public class KthLargestElement {

    static int findKthLargest(int[] nums, int k) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(findKthLargest(new int[]{3, 2, 1, 5, 6, 4}, 2));          // 5
        System.out.println(findKthLargest(new int[]{3, 2, 3, 1, 2, 4, 5, 5, 6}, 4)); // 4
    }
}
