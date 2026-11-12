package practice.d20261112.c2;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.*;
import org.junit.jupiter.api.Test;

class IntervalsTest {
    @Test void mergeExamples() {
        assertArrayEquals(new int[][]{{1, 6}, {8, 10}, {15, 18}}, Intervals.merge(new int[][]{{8, 10}, {1, 3}, {2, 6}, {15, 18}}));
        assertArrayEquals(new int[][]{{1, 5}}, Intervals.merge(new int[][]{{1, 4}, {4, 5}}));
        assertArrayEquals(new int[][]{{1, 10}}, Intervals.merge(new int[][]{{1, 10}, {2, 3}, {4, 5}}));
        assertArrayEquals(new int[0][], Intervals.merge(new int[0][]));
        assertArrayEquals(new int[][]{{3, 3}}, Intervals.merge(new int[][]{{3, 3}}));
    }
    @Test void mergeDoesNotMutateInput() {
        int[][] in = {{5, 6}, {1, 5}};
        Intervals.merge(in);
        assertArrayEquals(new int[][]{{5, 6}, {1, 5}}, in);
    }
    @Test void insertExamples() {
        assertArrayEquals(new int[][]{{1, 5}, {6, 9}}, Intervals.insert(new int[][]{{1, 3}, {6, 9}}, new int[]{2, 5}));
        assertArrayEquals(new int[][]{{1, 2}, {3, 10}, {12, 16}}, Intervals.insert(new int[][]{{1, 2}, {3, 5}, {6, 7}, {8, 10}, {12, 16}}, new int[]{4, 8}));
        assertArrayEquals(new int[][]{{5, 7}}, Intervals.insert(new int[0][], new int[]{5, 7}));
        assertArrayEquals(new int[][]{{0, 1}, {2, 3}}, Intervals.insert(new int[][]{{2, 3}}, new int[]{0, 1}));
        assertArrayEquals(new int[][]{{2, 3}, {4, 5}}, Intervals.insert(new int[][]{{2, 3}}, new int[]{4, 5}));
        assertArrayEquals(new int[][]{{1, 3}}, Intervals.insert(new int[][]{{1, 2}}, new int[]{2, 3}));
    }
    @Test void insertMatchesMergeOnRandom() {
        Random r = new Random(3);
        for (int t = 0; t < 300; t++) {
            int n = r.nextInt(8);
            int[][] raw = new int[n][];
            for (int i = 0; i < n; i++) { int s = r.nextInt(30); raw[i] = new int[]{s, s + r.nextInt(5)}; }
            int[][] sorted = Intervals.merge(raw);
            int s = r.nextInt(30);
            int[] ni = {s, s + r.nextInt(6)};
            int[][] all = Arrays.copyOf(sorted, sorted.length + 1);
            all[sorted.length] = ni;
            assertArrayEquals(Intervals.merge(all), Intervals.insert(sorted, ni));
        }
    }
    @Test void rooms() {
        assertEquals(2, Intervals.minMeetingRooms(new int[][]{{0, 30}, {5, 10}, {15, 20}}));
        assertEquals(1, Intervals.minMeetingRooms(new int[][]{{7, 10}, {2, 4}}));
        assertEquals(1, Intervals.minMeetingRooms(new int[][]{{0, 5}, {5, 8}}));
        assertEquals(0, Intervals.minMeetingRooms(new int[0][]));
        assertEquals(3, Intervals.minMeetingRooms(new int[][]{{1, 5}, {2, 6}, {3, 7}, {7, 9}}));
    }
    @Test void erase() {
        assertEquals(1, Intervals.eraseOverlapIntervals(new int[][]{{1, 2}, {2, 3}, {3, 4}, {1, 3}}));
        assertEquals(2, Intervals.eraseOverlapIntervals(new int[][]{{1, 2}, {1, 2}, {1, 2}}));
        assertEquals(0, Intervals.eraseOverlapIntervals(new int[][]{{1, 2}, {2, 3}}));
        assertEquals(0, Intervals.eraseOverlapIntervals(new int[0][]));
        assertEquals(1, Intervals.eraseOverlapIntervals(new int[][]{{Integer.MIN_VALUE, Integer.MAX_VALUE}, {0, 1}}));
    }
    @Test void largeIsFast() {
        Random r = new Random(5);
        int[][] big = new int[300_000][];
        for (int i = 0; i < big.length; i++) { int s = r.nextInt(1_000_000); big[i] = new int[]{s, s + 1 + r.nextInt(50)}; }
        assertTimeoutPreemptively(Duration.ofSeconds(4), () -> {
            Intervals.merge(big); Intervals.minMeetingRooms(big); Intervals.eraseOverlapIntervals(big);
        });
    }
}
