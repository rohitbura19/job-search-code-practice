package arrays;

import java.util.Arrays;

// Sort Colors  [Medium | Dutch national flag, 3-way partition]
// Array of only 0, 1, 2. Sort in place in ONE pass, no library sort.
//
// [2,0,2,1,1,0] -> [0,0,1,1,2,2]

public class SortColors {

    static void sortColors(int[] nums) {
        // TODO
    }

    public static void main(String[] args) {
        int[] a = {2, 0, 2, 1, 1, 0};
        sortColors(a);
        System.out.println(Arrays.toString(a)); // [0, 0, 1, 1, 2, 2]
    }
}
