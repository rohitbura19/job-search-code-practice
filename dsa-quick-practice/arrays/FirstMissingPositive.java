package arrays;

// First Missing Positive  [Hard | Cyclic placement]
// Smallest positive integer not in nums. O(n) time, O(1) extra space.
//
// [1,2,0]        -> 3
// [3,4,-1,1]     -> 2
// [7,8,9,11,12]  -> 1

public class FirstMissingPositive {

    static int firstMissingPositive(int[] nums) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(firstMissingPositive(new int[]{1, 2, 0}));         // 3
        System.out.println(firstMissingPositive(new int[]{3, 4, -1, 1}));     // 2
        System.out.println(firstMissingPositive(new int[]{7, 8, 9, 11, 12})); // 1
    }
}
