package binarysearch;

// Koko Eating Bananas  [Medium | Binary search on the answer]
// One pile per hour at speed k. Smallest k to finish within h hours. Hours can exceed int.
//
// [3,6,7,11], h=8        -> 4
// [30,11,23,4,20], h=5   -> 30
// [30,11,23,4,20], h=6   -> 23

public class KokoEatingBananas {

    static int minEatingSpeed(int[] piles, int h) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(minEatingSpeed(new int[]{3, 6, 7, 11}, 8));      // 4
        System.out.println(minEatingSpeed(new int[]{30, 11, 23, 4, 20}, 5)); // 30
        System.out.println(minEatingSpeed(new int[]{30, 11, 23, 4, 20}, 6)); // 23
    }
}
