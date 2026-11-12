package hashing;

// Longest Consecutive Sequence  [Medium | HashSet, start only at run heads]
// Length of the longest run of consecutive integers (any order). O(n).
//
// [100,4,200,1,3,2]       -> 4
// [0,3,7,2,5,8,4,6,0,1]   -> 9

public class LongestConsecutiveSequence {

    static int longestConsecutive(int[] nums) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(longestConsecutive(new int[]{100, 4, 200, 1, 3, 2}));          // 4
        System.out.println(longestConsecutive(new int[]{0, 3, 7, 2, 5, 8, 4, 6, 0, 1}));  // 9
    }
}
