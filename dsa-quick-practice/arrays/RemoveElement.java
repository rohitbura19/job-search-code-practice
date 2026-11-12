package arrays;

import java.util.Arrays;

// Remove Element  [Easy | Read/write pointers]
// Remove every val in place; return k, the first k slots hold the rest (original order).
//
// [3,2,2,3], val=3          -> 2, [2,2]
// [0,1,2,2,3,0,4,2], val=2  -> 5, [0,1,3,0,4]

public class RemoveElement {

    static int removeElement(int[] nums, int val) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        int[] a = {0, 1, 2, 2, 3, 0, 4, 2};
        int k = removeElement(a, 2);
        System.out.println(k + " " + Arrays.toString(Arrays.copyOf(a, k))); // 5 [0, 1, 3, 0, 4]
    }
}
