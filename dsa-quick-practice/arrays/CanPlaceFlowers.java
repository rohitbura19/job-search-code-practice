package arrays;

// Can Place Flowers  [Easy | Greedy]
// 0 empty, 1 planted; no two adjacent. Can n more flowers be planted?
//
// [1,0,0,0,1], n=1 -> true
// [1,0,0,0,1], n=2 -> false

public class CanPlaceFlowers {

    static boolean canPlaceFlowers(int[] bed, int n) {
        // TODO
        return false;
    }

    public static void main(String[] args) {
        System.out.println(canPlaceFlowers(new int[]{1, 0, 0, 0, 1}, 1)); // true
        System.out.println(canPlaceFlowers(new int[]{1, 0, 0, 0, 1}, 2)); // false
    }
}
