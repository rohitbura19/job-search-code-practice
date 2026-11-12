package arrays;

// Find the Duplicate Number  [Medium | Floyd cycle detection]
// n+1 values in [1, n], one value repeats. Don't modify the array, O(1) space.
//
// [1,3,4,2,2] -> 2
// [3,1,3,4,2] -> 3

public class FindDuplicateNumber {

    static int findDuplicate(int[] nums) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(findDuplicate(new int[]{1, 3, 4, 2, 2})); // 2
        System.out.println(findDuplicate(new int[]{3, 1, 3, 4, 2})); // 3
    }
}
