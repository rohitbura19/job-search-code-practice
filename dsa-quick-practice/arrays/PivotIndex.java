package arrays;

// Find Pivot Index  [Easy | Prefix sum]
// Leftmost i where sum left of i == sum right of i; -1 if none.
//
// [1,7,3,6,5,6] -> 3
// [1,2,3]       -> -1
// [2,1,-1]      -> 0

public class PivotIndex {

    static int pivotIndex(int[] nums) {
        // TODO
        return -1;
    }

    public static void main(String[] args) {
        System.out.println(pivotIndex(new int[]{1, 7, 3, 6, 5, 6})); // 3
        System.out.println(pivotIndex(new int[]{1, 2, 3}));          // -1
        System.out.println(pivotIndex(new int[]{2, 1, -1}));         // 0
    }
}
