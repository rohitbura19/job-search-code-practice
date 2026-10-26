package arrays;

// Trapping Rain Water  [Hard | Two pointers]
// Bars of width 1. How much water is trapped after rain? Aim for O(1) extra space.
//
// [0,1,0,2,1,0,1,3,2,1,2,1] -> 6
// [4,2,0,3,2,5]             -> 9

public class TrappingRainWater {

    static int trap(int[] height) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(trap(new int[]{0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1})); // 6
        System.out.println(trap(new int[]{4, 2, 0, 3, 2, 5}));                   // 9
    }
}
