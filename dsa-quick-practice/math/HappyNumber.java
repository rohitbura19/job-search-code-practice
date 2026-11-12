package math;

// Happy Number  [Easy | Floyd cycle on digit-square sums]
// Repeatedly replace n by the sum of the squares of its digits; true if it reaches 1. O(1) space.
//
// 19 -> true
// 2  -> false

public class HappyNumber {

    static boolean isHappy(int n) {
        // TODO
        return false;
    }

    public static void main(String[] args) {
        System.out.println(isHappy(19)); // true
        System.out.println(isHappy(2));  // false
    }
}
