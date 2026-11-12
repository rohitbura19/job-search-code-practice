package arrays;

// Single Number II  [Medium | Bit counting]
// Every value appears three times except one. Return it. O(n) time, O(1) space, no map.
//
// [2,2,3,2]         -> 3
// [0,1,0,1,0,1,99]  -> 99

public class SingleNumberII {

    static int singleNumber(int[] nums) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(singleNumber(new int[]{2, 2, 3, 2}));             // 3
        System.out.println(singleNumber(new int[]{0, 1, 0, 1, 0, 1, 99}));   // 99
    }
}
