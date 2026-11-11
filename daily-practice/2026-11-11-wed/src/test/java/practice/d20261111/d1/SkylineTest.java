package practice.d20261111.d1;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.*;
import org.junit.jupiter.api.Test;

class SkylineTest {
    static String str(List<int[]> l) { return l.stream().map(Arrays::toString).toList().toString(); }

    @Test void examples() {
        assertEquals("[[2, 10], [3, 15], [7, 12], [12, 0], [15, 10], [20, 8], [24, 0]]",
                str(Skyline.skyline(new int[][]{{2, 9, 10}, {3, 7, 15}, {5, 12, 12}, {15, 20, 10}, {19, 24, 8}})));
        assertEquals("[[0, 3], [5, 0]]", str(Skyline.skyline(new int[][]{{0, 2, 3}, {2, 5, 3}})));
        assertEquals("[]", str(Skyline.skyline(new int[][]{})));
        assertEquals("[[1, 5], [2, 0]]", str(Skyline.skyline(new int[][]{{1, 2, 5}})));
    }

    @Test void touchingAndNestedAndEqualHeights() {
        assertEquals("[[1, 2], [4, 0]]", str(Skyline.skyline(new int[][]{{1, 3, 2}, {3, 4, 2}})));
        assertEquals("[[0, 5], [10, 0]]", str(Skyline.skyline(new int[][]{{0, 10, 5}, {2, 4, 3}, {4, 6, 5}, {5, 10, 1}})));
        assertEquals("[[1, 4], [2, 6], [3, 4], [5, 0]]", str(Skyline.skyline(new int[][]{{1, 5, 4}, {2, 3, 6}})));
        assertEquals("[[0, 7], [3, 0]]", str(Skyline.skyline(new int[][]{{0, 3, 7}, {0, 3, 7}, {0, 3, 2}})));
    }

    @Test void extremeCoordinates() {
        assertEquals("[[0, 1], [2147483647, 0]]", str(Skyline.skyline(new int[][]{{0, Integer.MAX_VALUE, 1}})));
        assertEquals("[[0, 1000000000], [2147483647, 0]]", str(Skyline.skyline(new int[][]{{0, Integer.MAX_VALUE, 1_000_000_000}, {5, 10, 7}})));
    }

    @Test void randomVsPerUnitBrute() {
        Random r = new Random(23);
        for (int t = 0; t < 3000; t++) {
            int n = r.nextInt(8);
            int[][] b = new int[n][];
            for (int i = 0; i < n; i++) { int l = r.nextInt(20); b[i] = new int[]{l, l + 1 + r.nextInt(8), 1 + r.nextInt(5)}; }
            Arrays.sort(b, Comparator.comparingInt(x -> x[0]));
            assertEquals(str(brute(b)), str(Skyline.skyline(b)), Arrays.deepToString(b));
        }
    }

    static List<int[]> brute(int[][] b) {
        int[] h = new int[40];
        for (int[] x : b) for (int i = x[0]; i < x[1]; i++) h[i] = Math.max(h[i], x[2]);
        List<int[]> out = new ArrayList<>();
        int prev = 0;
        for (int i = 0; i < h.length; i++) if (h[i] != prev) { out.add(new int[]{i, h[i]}); prev = h[i]; }
        return out;
    }

    @Test void largeInputIsFast() {
        Random r = new Random(29);
        int n = 100_000;
        int[][] b = new int[n][];
        for (int i = 0; i < n; i++) { int l = r.nextInt(1_000_000_000); b[i] = new int[]{l, l + 1 + r.nextInt(1_000_000), 1 + r.nextInt(1_000_000_000)}; }
        Arrays.sort(b, Comparator.comparingInt(x -> x[0]));
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> {
            List<int[]> res = Skyline.skyline(b);
            assertEquals(0, res.get(res.size() - 1)[1]);
            for (int i = 1; i < res.size(); i++) { assertTrue(res.get(i)[0] > res.get(i - 1)[0]); assertNotEquals(res.get(i)[1], res.get(i - 1)[1]); }
        });
    }
}
