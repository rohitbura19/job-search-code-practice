package practice.d20261030.c1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * merge: sort a copy by start (Integer.compare, never a - b), extend the last output interval while next.start <= last.end. O(n log n).
 * insert: copy everything that ends before x starts, absorb everything that starts <= x.end into x, copy the rest. O(n).
 * minMeetingRooms: sort starts and ends separately; a start at time t reuses a room iff some end <= t (half-open). Two pointers, O(n log n).
 */
public final class Intervals {
    private Intervals() {}

    public static int[][] merge(int[][] intervals) {
        int[][] a = Arrays.stream(intervals).map(int[]::clone).toArray(int[][]::new);
        Arrays.sort(a, (p, q) -> Integer.compare(p[0], q[0]));
        List<int[]> out = new ArrayList<>();
        for (int[] cur : a) {
            if (!out.isEmpty() && cur[0] <= out.get(out.size() - 1)[1]) {
                int[] last = out.get(out.size() - 1);
                last[1] = Math.max(last[1], cur[1]);
            } else {
                out.add(cur);
            }
        }
        return out.toArray(new int[0][]);
    }

    public static int[][] insert(int[][] sorted, int[] x) {
        List<int[]> out = new ArrayList<>();
        int i = 0, n = sorted.length;
        int lo = x[0], hi = x[1];
        while (i < n && sorted[i][1] < lo) out.add(sorted[i++].clone());
        while (i < n && sorted[i][0] <= hi) {
            lo = Math.min(lo, sorted[i][0]);
            hi = Math.max(hi, sorted[i][1]);
            i++;
        }
        out.add(new int[] {lo, hi});
        while (i < n) out.add(sorted[i++].clone());
        return out.toArray(new int[0][]);
    }

    public static int minMeetingRooms(int[][] meetings) {
        int n = meetings.length;
        int[] s = new int[n], e = new int[n];
        for (int i = 0; i < n; i++) { s[i] = meetings[i][0]; e[i] = meetings[i][1]; }
        Arrays.sort(s);
        Arrays.sort(e);
        int rooms = 0, best = 0, j = 0;
        for (int i = 0; i < n; i++) {
            while (e[j] <= s[i]) { j++; rooms--; }
            rooms++;
            best = Math.max(best, rooms);
        }
        return best;
    }
}
