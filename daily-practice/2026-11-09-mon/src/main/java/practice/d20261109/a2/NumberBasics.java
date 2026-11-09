package practice.d20261109.a2;

public final class NumberBasics {
    private NumberBasics() {}

    /** Euclid, O(log min). gcd(0, 0) = 0. Inputs are non-negative. */
    public static int gcd(int a, int b) { throw new UnsupportedOperationException("TODO"); }

    /** Least common multiple as long (the int product can overflow). lcm with 0 is 0. */
    public static long lcm(int a, int b) { throw new UnsupportedOperationException("TODO"); }

    /** Happy number: repeatedly replace n by the sum of the squares of its digits; true if it reaches 1. O(1) space (no HashSet). */
    public static boolean isHappy(int n) { throw new UnsupportedOperationException("TODO"); }

    /** Excel column title: 1 -> "A", 26 -> "Z", 27 -> "AA", 702 -> "ZZ", 703 -> "AAA". */
    public static String excelTitle(int n) { throw new UnsupportedOperationException("TODO"); }

    /** Inverse of excelTitle for upper-case titles that fit in an int. */
    public static int excelNumber(String title) { throw new UnsupportedOperationException("TODO"); }

    /** Number of primes strictly less than n (n up to 5 million). */
    public static int countPrimes(int n) { throw new UnsupportedOperationException("TODO"); }
}
