package practice.d20261112.c2;

import java.util.*;

/** Sort-then-sweep family. merge O(n log n); insert O(n) on sorted input; minMeetingRooms O(n log n) with sorted starts/ends two-pointer; eraseOverlap greedy by earliest end, O(n log n). */
public class Intervals {
    public static int[][] merge(int[][] intervals) {
        int[][] a = intervals.clone();
        Arrays.sort(a, Comparator.comparingInt(x -> x[0]));
        List<int[]> out = new ArrayList<>();
        for (int[] cur : a) {
            if (!out.isEmpty() && cur[0] <= out.get(out.size() - 1)[1]) {
                int[] last = out.get(out.size() - 1);
                last[1] = Math.max(last[1], cur[1]);
            } else out.add(new int[]{cur[0], cur[1]});
        }
        return out.toArray(new int[0][]);
    }

    public static int[][] insert(int[][] sorted, int[] ni) {
        List<int[]> out = new ArrayList<>();
        int i = 0, n = sorted.length, lo = ni[0], hi = ni[1];
        while (i < n && sorted[i][1] < lo) out.add(sorted[i++]);
        while (i < n && sorted[i][0] <= hi) { lo = Math.min(lo, sorted[i][0]); hi = Math.max(hi, sorted[i][1]); i++; }
        out.add(new int[]{lo, hi});
        while (i < n) out.add(sorted[i++]);
        return out.toArray(new int[0][]);
    }

    public static int minMeetingRooms(int[][] intervals) {
        int n = intervals.length;
        int[] s = new int[n], e = new int[n];
        for (int i = 0; i < n; i++) { s[i] = intervals[i][0]; e[i] = intervals[i][1]; }
        Arrays.sort(s); Arrays.sort(e);
        int rooms = 0, best = 0, j = 0;
        for (int i = 0; i < n; i++) {
            while (e[j] <= s[i]) { j++; rooms--; }
            rooms++;
            best = Math.max(best, rooms);
        }
        return best;
    }

    public static int eraseOverlapIntervals(int[][] intervals) {
        int[][] a = intervals.clone();
        Arrays.sort(a, Comparator.comparingInt(x -> x[1]));
        int kept = 0;
        long end = Long.MIN_VALUE;
        for (int[] iv : a) if (iv[0] >= end) { kept++; end = iv[1]; }
        return a.length - kept;
    }
}
