package practice.d20261012.a2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class SearchInsertTest {

    @Test @DisplayName("examples from the question")
    void examples() {
        int[] a = {1, 3, 5, 6};
        assertEquals(2, SearchInsert.searchInsert(a, 5));
        assertEquals(1, SearchInsert.searchInsert(a, 2));
        assertEquals(4, SearchInsert.searchInsert(a, 7));
        assertEquals(0, SearchInsert.searchInsert(a, 0));
    }

    @Test @DisplayName("empty and single element arrays, extreme values")
    void edges() {
        assertEquals(0, SearchInsert.searchInsert(new int[]{}, 5));
        assertEquals(0, SearchInsert.searchInsert(new int[]{5}, 5));
        assertEquals(1, SearchInsert.searchInsert(new int[]{5}, 6));
        assertEquals(0, SearchInsert.searchInsert(new int[]{5}, 4));
        int[] b = {Integer.MIN_VALUE, 0, Integer.MAX_VALUE};
        assertEquals(2, SearchInsert.searchInsert(b, Integer.MAX_VALUE));
        assertEquals(0, SearchInsert.searchInsert(b, Integer.MIN_VALUE));
    }

    @Test @DisplayName("firstOccurrence with duplicates")
    void firstOcc() {
        int[] a = {1, 2, 2, 2, 3, 3, 9};
        assertEquals(1, SearchInsert.firstOccurrence(a, 2));
        assertEquals(4, SearchInsert.firstOccurrence(a, 3));
        assertEquals(0, SearchInsert.firstOccurrence(a, 1));
        assertEquals(6, SearchInsert.firstOccurrence(a, 9));
        assertEquals(-1, SearchInsert.firstOccurrence(a, 4));
        assertEquals(-1, SearchInsert.firstOccurrence(a, 0));
        assertEquals(-1, SearchInsert.firstOccurrence(new int[]{}, 1));
        assertEquals(0, SearchInsert.firstOccurrence(new int[]{7, 7, 7, 7}, 7));
    }

    @Test @DisplayName("random arrays match a linear scan")
    void randomOracle() {
        Random rnd = new Random(3);
        for (int t = 0; t < 3000; t++) {
            int n = rnd.nextInt(12);
            int[] a = new int[n]; for (int i = 0; i < n; i++) a[i] = rnd.nextInt(15) - 7;
            Arrays.sort(a);
            int target = rnd.nextInt(19) - 9;
            int exp = 0; while (exp < n && a[exp] < target) exp++;
            assertEquals(exp, SearchInsert.searchInsert(a, target), Arrays.toString(a) + " t=" + target);
            int first = (exp < n && a[exp] == target) ? exp : -1;
            assertEquals(first, SearchInsert.firstOccurrence(a, target), Arrays.toString(a) + " t=" + target);
        }
    }

    @Test @DisplayName("1,000,000 queries on a 10,000,000 element array finish fast (O(log n) each)")
    void bigIsFast() {
        int[] a = new int[10_000_000]; for (int i = 0; i < a.length; i++) a[i] = i * 2;
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> {
            Random rnd = new Random(1);
            for (int q = 0; q < 1_000_000; q++) {
                int t = rnd.nextInt(20_000_000);
                assertEquals((t + 1) / 2, SearchInsert.searchInsert(a, t));
            }
        });
    }

    @Test @DisplayName("shipWithinDays examples")
    void shipExamples() {
        assertEquals(15, SearchInsert.shipWithinDays(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, 5));
        assertEquals(6, SearchInsert.shipWithinDays(new int[]{3, 2, 2, 4, 1, 4}, 3));
        assertEquals(3, SearchInsert.shipWithinDays(new int[]{1, 2, 3, 1, 1}, 4));
        assertEquals(10, SearchInsert.shipWithinDays(new int[]{10}, 1));
        assertEquals(55, SearchInsert.shipWithinDays(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, 1));
        assertEquals(10, SearchInsert.shipWithinDays(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, 10));
    }

    @Test @DisplayName("total weight above int range: (lo+hi)/2 in int would overflow")
    void shipOverflow() {
        int m = Integer.MAX_VALUE;
        assertEquals(2L * m, SearchInsert.shipWithinDays(new int[]{m, m}, 1));
        assertEquals((long) m, SearchInsert.shipWithinDays(new int[]{m, m}, 2));
        assertEquals(3L * m, SearchInsert.shipWithinDays(new int[]{m, m, m}, 1));
    }

    @Test @DisplayName("random weights match trying every capacity")
    void shipRandom() {
        Random rnd = new Random(5);
        for (int t = 0; t < 1000; t++) {
            int n = rnd.nextInt(8) + 1; int[] w = new int[n]; int sum = 0, max = 0;
            for (int i = 0; i < n; i++) { w[i] = rnd.nextInt(9) + 1; sum += w[i]; max = Math.max(max, w[i]); }
            int days = rnd.nextInt(n) + 1;
            int cap = max; while (!ok(w, days, cap)) cap++;
            assertEquals(cap, SearchInsert.shipWithinDays(w, days), Arrays.toString(w) + " days=" + days);
        }
    }

    static boolean ok(int[] w, int days, int cap) {
        int d = 1, load = 0;
        for (int x : w) { if (load + x > cap) { d++; load = 0; } load += x; }
        return d <= days;
    }
}
