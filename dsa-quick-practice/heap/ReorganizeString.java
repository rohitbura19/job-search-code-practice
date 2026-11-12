package heap;

// Reorganize String  [Medium | Max-heap by count, or fill even indices]
// Rearrange so no two adjacent chars are equal; "" if impossible.
//
// "aab"  -> "aba"
// "aaab" -> ""

public class ReorganizeString {

    static String reorganizeString(String s) {
        // TODO
        return "";
    }

    public static void main(String[] args) {
        System.out.println(reorganizeString("aab"));                    // aba
        System.out.println("[" + reorganizeString("aaab") + "]");       // []
    }
}
