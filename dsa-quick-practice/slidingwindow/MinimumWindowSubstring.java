package slidingwindow;

// Minimum Window Substring  [Hard | Sliding window + counts]
// Shortest substring of s containing every char of t (with repeats). "" if none.
//
// s="ADOBECODEBANC", t="ABC" -> "BANC"
// s="a", t="aa"              -> ""

public class MinimumWindowSubstring {

    static String minWindow(String s, String t) {
        // TODO
        return "";
    }

    public static void main(String[] args) {
        System.out.println(minWindow("ADOBECODEBANC", "ABC")); // BANC
        System.out.println("[" + minWindow("a", "aa") + "]");  // []
    }
}
