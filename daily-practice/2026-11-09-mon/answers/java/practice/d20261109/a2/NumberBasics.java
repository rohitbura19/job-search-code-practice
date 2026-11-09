package practice.d20261109.a2;

public final class NumberBasics {
    private NumberBasics() {}

    /** Euclid, O(log min). gcd(0, 0) = 0. Inputs are non-negative. */
    public static int gcd(int a, int b) {
        while (b != 0) { int t = a % b; a = b; b = t; }
        return a;
    }

    /** a / gcd * b in long (the int product can overflow). lcm with 0 is 0. */
    public static long lcm(int a, int b) {
        if (a == 0 || b == 0) return 0;
        return (long) (a / gcd(a, b)) * b;
    }

    /** Happy number: repeated sum of squared digits reaches 1. Floyd cycle detection, O(1) space. */
    public static boolean isHappy(int n) {
        int slow = n, fast = next(n);
        while (fast != 1 && slow != fast) { slow = next(slow); fast = next(next(fast)); }
        return fast == 1;
    }

    private static int next(int n) {
        int s = 0;
        while (n > 0) { int d = n % 10; s += d * d; n /= 10; }
        return s;
    }

    /** 1 -> "A", 26 -> "Z", 27 -> "AA". Bijective base 26: subtract 1 before each digit. */
    public static String excelTitle(int n) {
        StringBuilder sb = new StringBuilder();
        while (n > 0) { n--; sb.append((char) ('A' + n % 26)); n /= 26; }
        return sb.reverse().toString();
    }

    /** Inverse of excelTitle. Titles up to "FXSHRXW" (Integer.MAX_VALUE) fit in int. */
    public static int excelNumber(String title) {
        int n = 0;
        for (char c : title.toCharArray()) n = n * 26 + (c - 'A' + 1);
        return n;
    }

    /** Number of primes strictly less than n. Sieve of Eratosthenes. */
    public static int countPrimes(int n) {
        if (n < 3) return 0;
        boolean[] composite = new boolean[n];
        int count = 0;
        for (int i = 2; i < n; i++) {
            if (composite[i]) continue;
            count++;
            for (long j = (long) i * i; j < n; j += i) composite[(int) j] = true;
        }
        return count;
    }
}
