package practice.d20261029.c2;

public final class SearchOnAnswer {
    private SearchOnAnswer() {}

    /**
     * Koko eats bananas: piles[i] bananas, h hours, one pile per hour at most (a pile of 3 at speed 2 takes 2 hours, the hour's leftover is wasted).
     * Return the minimum integer speed so that all piles are eaten within h hours. Guaranteed piles.length <= h, piles[i] up to 10^9,
     * so the hour count does not fit in an int. Example: [3,6,7,11], h=8 -> 4; [30,11,23,4,20], h=5 -> 30; [30,11,23,4,20], h=6 -> 23.
     */
    public static int minEatingSpeed(int[] piles, int h) { throw new UnsupportedOperationException("TODO"); }

    /**
     * Split `nums` (non-negative ints, 1 <= k <= nums.length) into k non-empty CONTIGUOUS parts so that the largest part sum is as small
     * as possible; return that sum. [7,2,5,10,8], k=2 -> 18; [1,2,3,4,5], k=2 -> 9; [1,4,4], k=3 -> 4. Target O(n log(sum)), not O(k n^2).
     */
    public static int splitArray(int[] nums, int k) { throw new UnsupportedOperationException("TODO"); }
}
