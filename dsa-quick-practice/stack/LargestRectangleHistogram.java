package stack;

// Largest Rectangle in Histogram  [Hard | Monotonic stack]
// Bars of width 1. Area of the largest rectangle under the bars. O(n).
//
// [2,1,5,6,2,3] -> 10
// [2,4]         -> 4

public class LargestRectangleHistogram {

    static int largestRectangleArea(int[] heights) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(largestRectangleArea(new int[]{2, 1, 5, 6, 2, 3})); // 10
        System.out.println(largestRectangleArea(new int[]{2, 4}));             // 4
    }
}
