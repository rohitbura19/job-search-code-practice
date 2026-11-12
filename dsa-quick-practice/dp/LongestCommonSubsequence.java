package dp;

// Longest Common Subsequence  [Medium | 2-D DP, O(min) space]
//
// "abcde", "ace" -> 3
// "abc", "abc"   -> 3
// "abc", "def"   -> 0

public class LongestCommonSubsequence {

    static int longestCommonSubsequence(String a, String b) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(longestCommonSubsequence("abcde", "ace")); // 3
        System.out.println(longestCommonSubsequence("abc", "abc"));   // 3
        System.out.println(longestCommonSubsequence("abc", "def"));   // 0
    }
}
