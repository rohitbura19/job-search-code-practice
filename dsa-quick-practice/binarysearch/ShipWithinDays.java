package binarysearch;

// Capacity to Ship Packages Within D Days  [Medium | Binary search on the answer]
// Ship weights in order, at most `days` days. Smallest ship capacity that works.
//
// [1,2,3,4,5,6,7,8,9,10], days=5 -> 15
// [3,2,2,4,1,4], days=3          -> 6

public class ShipWithinDays {

    static int shipWithinDays(int[] weights, int days) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(shipWithinDays(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, 5)); // 15
        System.out.println(shipWithinDays(new int[]{3, 2, 2, 4, 1, 4}, 3));             // 6
    }
}
