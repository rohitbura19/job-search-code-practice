package practice.d20261111.a1;

/** thirdMax: keep top three distinct values in longs (Long.MIN_VALUE can never equal an int, so it is a safe "empty" marker). O(n)/O(1).
 *  longestOnesWithOneFlip: sliding window holding at most one zero. O(n)/O(1).
 *  canPlaceFlowers: greedy left to right, plant whenever both neighbours are empty (earliest planting never hurts later plots). O(n)/O(1). */
public final class ArrayBasics {
    private ArrayBasics() {}

    public static int thirdMax(int[] a) {
        long m1 = Long.MIN_VALUE, m2 = Long.MIN_VALUE, m3 = Long.MIN_VALUE;
        for (int x : a) {
            if (x == m1 || x == m2 || x == m3) continue;
            if (x > m1) { m3 = m2; m2 = m1; m1 = x; }
            else if (x > m2) { m3 = m2; m2 = x; }
            else if (x > m3) m3 = x;
        }
        return (int) (m3 == Long.MIN_VALUE ? m1 : m3);
    }

    public static int longestOnesWithOneFlip(int[] bits) {
        int best = 0, left = 0, zeros = 0;
        for (int right = 0; right < bits.length; right++) {
            if (bits[right] == 0) zeros++;
            while (zeros > 1) if (bits[left++] == 0) zeros--;
            best = Math.max(best, right - left + 1);
        }
        return best;
    }

    public static boolean canPlaceFlowers(int[] bed, int n) {
        int planted = 0;
        boolean prevFull = false;                       // is the plot to the left occupied (including one we just planted)?
        for (int i = 0; i < bed.length && planted < n; i++) {
            boolean full = bed[i] == 1;
            boolean nextFull = i + 1 < bed.length && bed[i + 1] == 1;
            if (!full && !prevFull && !nextFull) { planted++; full = true; }
            prevFull = full;
        }
        return planted >= n;
    }
}
