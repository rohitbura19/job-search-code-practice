package stack;

// Remove All Adjacent Duplicates in String II  [Medium | Stack of (char, count)]
// Repeatedly delete k equal adjacent chars. O(n).
//
// "abcd", 2              -> "abcd"
// "deeedbbcccbdaa", 3    -> "aa"
// "pbbcggttciiippooaais", 2 -> "ps"

public class RemoveDuplicatesK {

    static String removeDuplicates(String s, int k) {
        // TODO
        return "";
    }

    public static void main(String[] args) {
        System.out.println(removeDuplicates("abcd", 2));                 // abcd
        System.out.println(removeDuplicates("deeedbbcccbdaa", 3));       // aa
        System.out.println(removeDuplicates("pbbcggttciiippooaais", 2)); // ps
    }
}
