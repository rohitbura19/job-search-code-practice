package strings;

// Longest Common Prefix  [Easy | Vertical scan]
// "" if none.
//
// [flower,flow,flight] -> "fl"
// [dog,racecar,car]    -> ""

public class LongestCommonPrefix {

    static String longestCommonPrefix(String[] strs) {
        // TODO
        return "";
    }

    public static void main(String[] args) {
        System.out.println(longestCommonPrefix(new String[]{"flower", "flow", "flight"}));         // fl
        System.out.println("[" + longestCommonPrefix(new String[]{"dog", "racecar", "car"}) + "]"); // []
    }
}
