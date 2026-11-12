package graphs;

// Trapping Rain Water II  [Hard | Min-heap flood fill from the border]
// 2D height map. Total water trapped.
//
// [[1,4,3,1,3,2],[3,2,1,3,2,4],[2,3,3,2,3,1]]                         -> 4
// [[3,3,3,3,3],[3,2,2,2,3],[3,2,1,2,3],[3,2,2,2,3],[3,3,3,3,3]]       -> 10

public class TrappingRainWaterII {

    static int trapRainWater(int[][] heightMap) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(trapRainWater(new int[][]{{1, 4, 3, 1, 3, 2}, {3, 2, 1, 3, 2, 4}, {2, 3, 3, 2, 3, 1}})); // 4
        System.out.println(trapRainWater(new int[][]{{3, 3, 3, 3, 3}, {3, 2, 2, 2, 3}, {3, 2, 1, 2, 3}, {3, 2, 2, 2, 3}, {3, 3, 3, 3, 3}})); // 10
    }
}
