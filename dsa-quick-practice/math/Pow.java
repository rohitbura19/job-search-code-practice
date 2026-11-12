package math;

// Pow(x, n)  [Medium | Fast exponentiation]
// O(log |n|). n may be negative, even Integer.MIN_VALUE.
//
// 2.0, 10   -> 1024.0
// 2.0, -2   -> 0.25
// -1.0, Integer.MIN_VALUE -> 1.0

public class Pow {

    static double myPow(double x, int n) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(myPow(2.0, 10));                 // 1024.0
        System.out.println(myPow(2.0, -2));                 // 0.25
        System.out.println(myPow(-1.0, Integer.MIN_VALUE)); // 1.0
    }
}
