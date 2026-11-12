package arrays;

// Container With Most Water  [Medium | Two pointers]
// Max over i < j of min(h[i], h[j]) * (j - i).
//
// [1,8,6,2,5,4,8,3,7] -> 49
// [1,1]               -> 1

public class ContainerWithMostWater {

    static int maxArea(int[] height) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(maxArea(new int[]{1, 8, 6, 2, 5, 4, 8, 3, 7})); // 49
        System.out.println(maxArea(new int[]{1, 1}));                      // 1
    }
}
