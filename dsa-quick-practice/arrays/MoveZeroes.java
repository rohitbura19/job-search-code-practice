package arrays;

import java.util.Arrays;

// Move Zeroes  [Easy | Two pointers]
// Move all 0s to the end in place, keeping the order of the other values.
//
// [0,1,0,3,12] -> [1,3,12,0,0]

public class MoveZeroes {

    static void moveZeroes(int[] nums) {
        // swith it with the next positive number

        int pos = 0;
        int zero = 0;
        while(pos < nums.length){
            while(pos< nums.length && nums[pos]==0){
                pos++;
            }
            if(pos==nums.length){
                return;
            }

            while(zero< nums.length && nums[zero]!=0){
                zero++;
            }
            if(zero==nums.length){
                return;
            }

            // non-zero is before the first zero: it is already in place, search after the zero
            if(pos < zero){
                pos = zero;
                continue;
            }

            // move the next non-zero into the first zero slot
            nums[zero] = nums[pos];
            nums[pos] = 0;
            zero++;
            pos++;
        }
    }

    public static void main(String[] args) {
        int[] a = {0, 1, 0, 3, 12};
        moveZeroes(a);
        System.out.println(Arrays.toString(a)); // [1, 3, 12, 0, 0]
    }
}
