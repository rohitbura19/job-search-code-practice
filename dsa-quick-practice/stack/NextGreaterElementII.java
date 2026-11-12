package stack;

import java.util.Arrays;

// Next Greater Element II  [Medium | Monotonic stack, loop twice]
// Circular array: first strictly greater element to the right (wrapping), -1 if none.
//
// [1,2,1]     -> [2,-1,2]
// [1,2,3,4,3] -> [2,3,4,-1,4]

public class NextGreaterElementII {

    static int[] nextGreaterElements(int[] nums) {
        // TODO
        return new int[0];
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(nextGreaterElements(new int[]{1, 2, 1})));       // [2, -1, 2]
        System.out.println(Arrays.toString(nextGreaterElements(new int[]{1, 2, 3, 4, 3}))); // [2, 3, 4, -1, 4]
    }
}
