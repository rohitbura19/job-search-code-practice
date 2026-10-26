package arrays;

// Majority Element  [Easy | Boyer-Moore voting]
// Return the value that appears more than n/2 times (always exists). O(1) extra space.
//
// [3,2,3]         -> 3
// [2,2,1,1,1,2,2] -> 2

public class MajorityElement {

    static int majorityElement(int[] nums) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(majorityElement(new int[]{3, 2, 3}));             // 3
        System.out.println(majorityElement(new int[]{2, 2, 1, 1, 1, 2, 2})); // 2
    }
}
