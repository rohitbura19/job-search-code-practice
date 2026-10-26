package dp;

// Edit Distance  [Hard | 2-D DP]
// Min inserts, deletes and replaces to turn a into b.
//
// "horse" -> "ros"           = 3
// "intention" -> "execution" = 5

public class EditDistance {

    static int minDistance(String a, String b) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(minDistance("horse", "ros"));           // 3
        System.out.println(minDistance("intention", "execution")); // 5
    }
}
