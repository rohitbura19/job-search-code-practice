package practice.d20261111.d1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

/** Sweep line over 2n events (x, -h) for a start and (x, +h) for an end. Sorting by (x, signed height) makes, at one x: taller starts first, then starts before ends,
 *  then shorter ends first - so touching buildings never produce a spurious dip and each x emits at most one point (we compare with the previous maximum).
 *  A TreeMap<height, count> is the multiset of active heights (with 0 as ground); lastKey is the current maximum. O(n log n) time, O(n) space. */
public final class Skyline {
    private Skyline() {}

    public static List<int[]> skyline(int[][] buildings) {
        int n = buildings.length;
        int[][] ev = new int[2 * n][];
        for (int i = 0; i < n; i++) {
            ev[2 * i] = new int[]{buildings[i][0], -buildings[i][2]};
            ev[2 * i + 1] = new int[]{buildings[i][1], buildings[i][2]};
        }
        Arrays.sort(ev, (a, b) -> a[0] != b[0] ? Integer.compare(a[0], b[0]) : Integer.compare(a[1], b[1]));
        TreeMap<Integer, Integer> active = new TreeMap<>();
        active.put(0, 1);
        List<int[]> out = new ArrayList<>();
        int prev = 0;
        for (int[] e : ev) {
            if (e[1] < 0) active.merge(-e[1], 1, Integer::sum);
            else active.merge(e[1], -1, (a, b) -> a + b == 0 ? null : a + b);
            int cur = active.lastKey();
            if (cur != prev) { out.add(new int[]{e[0], cur}); prev = cur; }
        }
        return out;
    }
}
