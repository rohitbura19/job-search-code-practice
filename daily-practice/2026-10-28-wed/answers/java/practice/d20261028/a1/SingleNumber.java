package practice.d20261028.a1;

/**
 * once: XOR cancels pairs (x ^ x = 0, x ^ 0 = x).
 * onceAmongThrees: per bit, count of ones mod 3; `ones`/`twos` are a 2-bit counter per bit position (00 -> 01 -> 10 -> 00).
 * twoSingles: XOR of all = a ^ b != 0; its lowest set bit separates a from b, so XOR each half separately.
 * All O(n) time, O(1) space.
 */
public final class SingleNumber {
    private SingleNumber() {}

    public static int once(int[] nums) {
        int r = 0;
        for (int x : nums) r ^= x;
        return r;
    }

    public static int onceAmongThrees(int[] nums) {
        int ones = 0, twos = 0;
        for (int x : nums) {
            ones = (ones ^ x) & ~twos;
            twos = (twos ^ x) & ~ones;
        }
        return ones;
    }

    public static int[] twoSingles(int[] nums) {
        int all = 0;
        for (int x : nums) all ^= x;
        int low = all & -all; // also right for Integer.MIN_VALUE
        int a = 0, b = 0;
        for (int x : nums) { if ((x & low) == 0) a ^= x; else b ^= x; }
        return a < b ? new int[] {a, b} : new int[] {b, a};
    }
}
