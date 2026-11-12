package strings;

// Find the Index of the First Occurrence  [Easy | Scan (bonus: KMP)]
// Index of needle in hay, -1 if absent, 0 for empty needle. No indexOf/contains.
//
// "sadbutsad", "sad"   -> 0
// "leetcode", "leeto"  -> -1

public class StrStr {

    static int strStr(String hay, String needle) {
        // TODO
        return -1;
    }

    public static void main(String[] args) {
        System.out.println(strStr("sadbutsad", "sad"));  // 0
        System.out.println(strStr("leetcode", "leeto")); // -1
    }
}
