package dp;

// Decode Ways  [Medium | 1-D DP]
// A=1 ... Z=26. Count the ways to decode a digit string.
//
// "12"  -> 2   (AB, L)
// "226" -> 3   (BBF, BZ, VF)
// "06"  -> 0

public class DecodeWays {

    static int numDecodings(String s) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(numDecodings("12"));  // 2
        System.out.println(numDecodings("226")); // 3
        System.out.println(numDecodings("06"));  // 0
    }
}
