package math;

import java.util.Arrays;

// Counting Bits  [Easy | DP on i >> 1]
// out[i] = number of 1 bits in i, for 0..n. O(n), no bitCount.
//
// 2 -> [0,1,1]
// 5 -> [0,1,1,2,1,2]

public class CountingBits {

    static int[] countBits(int n) {
        // TODO
        return new int[0];
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(countBits(2))); // [0, 1, 1]
        System.out.println(Arrays.toString(countBits(5))); // [0, 1, 1, 2, 1, 2]
    }
}
