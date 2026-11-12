package intervals;

import java.util.List;

// The Skyline Problem  [Hard | Sweep line + max-heap / TreeMap of heights]
// buildings[i] = {left, right, height}. Key points {x, height} where the outline height changes.
//
// [[2,9,10],[3,7,15],[5,12,12],[15,20,10],[19,24,8]]
//   -> [[2,10],[3,15],[7,12],[12,0],[15,10],[20,8],[24,0]]

public class SkylineProblem {

    static List<List<Integer>> getSkyline(int[][] buildings) {
        // TODO
        return List.of();
    }

    public static void main(String[] args) {
        System.out.println(getSkyline(new int[][]{{2, 9, 10}, {3, 7, 15}, {5, 12, 12}, {15, 20, 10}, {19, 24, 8}}));
        // [[2, 10], [3, 15], [7, 12], [12, 0], [15, 10], [20, 8], [24, 0]]
    }
}
