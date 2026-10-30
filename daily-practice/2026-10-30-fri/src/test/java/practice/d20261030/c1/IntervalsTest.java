package practice.d20261030.c1;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class IntervalsTest {
    static int[][] iv(int... flat) {
        int[][] r = new int[flat.length / 2][];
        for (int i = 0; i < r.length; i++) r[i] = new int[] {flat[2 * i], flat[2 * i + 1]};
        return r;
    }

    @Test void mergeExamples() {
        assertArrayEquals(iv(1, 6, 8, 10), Intervals.merge(iv(1, 3, 8, 10, 2, 6)));
        assertArrayEquals(iv(1, 5), Intervals.merge(iv(1, 4, 4, 5)));          // touching merges
        assertArrayEquals(iv(1, 2, 3, 4), Intervals.merge(iv(3, 4, 1, 2)));    // adjacent integers do not
        assertArrayEquals(iv(2, 2), Intervals.merge(iv(2, 2, 2, 2)));
        assertArrayEquals(iv(), Intervals.merge(iv()));
        assertArrayEquals(iv(1, 10), Intervals.merge(iv(1, 10, 2, 3, 4, 5)));  // contained
    }

    @Test void mergeExtremeValuesAndInputUntouched() {
        int[][] in = iv(Integer.MAX_VALUE - 1, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE + 1, Integer.MIN_VALUE + 1, 0);
        int[][] copy = iv(Integer.MAX_VALUE - 1, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE + 1, Integer.MIN_VALUE + 1, 0);
        assertArrayEquals(iv(Integer.MIN_VALUE, 0, Integer.MAX_VALUE - 1, Integer.MAX_VALUE), Intervals.merge(in));
        assertArrayEquals(copy, in);
    }

    @Test void mergeRandomAgainstCoverageGrid() {
        Random r = new Random(7);
        for (int it = 0; it < 1000; it++) {
            int n = r.nextInt(8);
            int[][] in = new int[n][];
            boolean[] cover = new boolean[80];            // cell 2x = point x, cell 2x+1 = the gap after x
            for (int i = 0; i < n; i++) {
                int a = r.nextInt(30), b = a + r.nextInt(6);
                in[i] = new int[] {a, b};
                for (int c = 2 * a; c <= 2 * b; c++) cover[c] = true;
            }
            List<int[]> expected = new ArrayList<>();
            for (int c = 0; c < cover.length; c++) {
                if (!cover[c]) continue;
                int d = c;
                while (d + 1 < cover.length && cover[d + 1]) d++;
                expected.add(new int[] {c / 2, d / 2});
                c = d;
            }
            assertArrayEquals(expected.toArray(new int[0][]), Intervals.merge(in));
        }
    }

    @Test void insertExamples() {
        assertArrayEquals(iv(1, 2, 3, 10), Intervals.insert(iv(1, 2, 3, 5, 6, 7, 8, 10), new int[] {4, 8}));
        assertArrayEquals(iv(1, 5, 6, 9), Intervals.insert(iv(1, 3, 6, 9), new int[] {2, 5}));
        assertArrayEquals(iv(0, 0, 1, 2), Intervals.insert(iv(1, 2), new int[] {0, 0}));
        assertArrayEquals(iv(1, 2, 5, 5), Intervals.insert(iv(1, 2), new int[] {5, 5}));
        assertArrayEquals(iv(3, 4), Intervals.insert(iv(), new int[] {3, 4}));
        assertArrayEquals(iv(1, 5), Intervals.insert(iv(1, 2, 3, 4), new int[] {2, 5}));
        assertArrayEquals(iv(1, 8), Intervals.insert(iv(2, 3, 4, 5), new int[] {1, 8}));
        assertArrayEquals(iv(1, 5), Intervals.insert(iv(1, 2), new int[] {2, 5}));   // touching
    }

    @Test void insertEqualsMergeOfAll() {
        Random r = new Random(8);
        for (int it = 0; it < 500; it++) {
            int n = r.nextInt(7), pos = 0;
            int[][] sorted = new int[n][];
            for (int i = 0; i < n; i++) {
                int a = pos + 1 + r.nextInt(3), b = a + r.nextInt(4);
                sorted[i] = new int[] {a, b};
                pos = b;
            }
            int a = r.nextInt(30), x[] = {a, a + r.nextInt(8)};
            int[][] all = new int[n + 1][];
            System.arraycopy(sorted, 0, all, 0, n);
            all[n] = x;
            assertArrayEquals(Intervals.merge(all), Intervals.insert(sorted, x));
        }
    }

    @Test void roomsExamples() {
        assertEquals(2, Intervals.minMeetingRooms(iv(0, 30, 5, 10, 15, 20)));
        assertEquals(1, Intervals.minMeetingRooms(iv(0, 5, 5, 10)));
        assertEquals(0, Intervals.minMeetingRooms(iv()));
        assertEquals(3, Intervals.minMeetingRooms(iv(1, 5, 1, 5, 1, 5)));
        assertEquals(1, Intervals.minMeetingRooms(iv(7, 8)));
        assertEquals(1, Intervals.minMeetingRooms(iv(Integer.MIN_VALUE, 0, 0, Integer.MAX_VALUE)));
    }

    @Test void roomsRandomAgainstPointCount() {
        Random r = new Random(9);
        for (int it = 0; it < 1000; it++) {
            int n = r.nextInt(9);
            int[][] m = new int[n][];
            for (int i = 0; i < n; i++) { int a = r.nextInt(20); m[i] = new int[] {a, a + 1 + r.nextInt(8)}; }
            int best = 0;
            for (int t = 0; t < 30; t++) {
                int c = 0;
                for (int[] x : m) if (x[0] <= t && t < x[1]) c++;
                best = Math.max(best, c);
            }
            assertEquals(best, Intervals.minMeetingRooms(m));
        }
    }

    @Test @Timeout(3) void large() {
        Random r = new Random(10);
        int n = 200_000;
        int[][] m = new int[n][];
        for (int i = 0; i < n; i++) { int a = r.nextInt(1_000_000); m[i] = new int[] {a, a + 1 + r.nextInt(50)}; }
        Intervals.minMeetingRooms(m);
        Intervals.merge(m);
    }
}
