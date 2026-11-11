package practice.d20261111.d1;

import java.util.List;

public final class Skyline {
    private Skyline() {}

    /**
     * The Skyline Problem. buildings[i] = {left, right, height}, left < right, height >= 1, sorted by left (not by right). Coordinates are any non-negative ints
     * (right may be Integer.MAX_VALUE). Return the key points {x, height} of the outline, left to right: a key point is the left end of a horizontal
     * segment, i.e. where the max height changes. The last point has height 0. Neighbouring points never share a height, and no point repeats an x.
     * [[2,9,10],[3,7,15],[5,12,12],[15,20,10],[19,24,8]] -> [[2,10],[3,15],[7,12],[12,0],[15,10],[20,8],[24,0]].
     * n up to 100,000: O(n log n).
     */
    public static List<int[]> skyline(int[][] buildings) { throw new UnsupportedOperationException("TODO"); }
}
