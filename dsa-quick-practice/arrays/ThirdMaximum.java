package arrays;

// Third Maximum Number  [Easy | Track top 3]
// Third DISTINCT maximum; if fewer than 3 distinct values, the maximum. O(1) space.
//
// [3,2,1]   -> 1
// [1,2]     -> 2
// [2,2,3,1] -> 1

public class ThirdMaximum {

    static int thirdMax(int[] nums) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(thirdMax(new int[]{3, 2, 1}));    // 1
        System.out.println(thirdMax(new int[]{1, 2}));       // 2
        System.out.println(thirdMax(new int[]{2, 2, 3, 1})); // 1
    }
}
