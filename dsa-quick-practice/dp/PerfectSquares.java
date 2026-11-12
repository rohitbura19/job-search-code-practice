package dp;

// Perfect Squares  [Medium | dp[n] = 1 + min dp[n - k*k]]
// Least number of perfect squares that sum to n.
//
// 12 -> 3   (4+4+4)
// 13 -> 2   (4+9)

public class PerfectSquares {

    static int numSquares(int n) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(numSquares(12)); // 3
        System.out.println(numSquares(13)); // 2
    }
}
