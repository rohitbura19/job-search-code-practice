package practice.d20261104.a2;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.*;
import org.junit.jupiter.api.Test;

class BinarySearchTest {
    @Test void searchInsertExamples() {
        int[] a = {1, 3, 5, 6};
        assertEquals(2, BinarySearch.searchInsert(a, 5));
        assertEquals(1, BinarySearch.searchInsert(a, 2));
        assertEquals(4, BinarySearch.searchInsert(a, 7));
        assertEquals(0, BinarySearch.searchInsert(a, 0));
        assertEquals(0, BinarySearch.searchInsert(new int[]{}, 3));
        assertEquals(1, BinarySearch.searchInsert(new int[]{4}, 9));
        assertEquals(0, BinarySearch.searchInsert(new int[]{4}, 4));
        assertEquals(1, BinarySearch.searchInsert(new int[]{1, 2, 2, 2, 3}, 2), "first of the duplicates");
    }

    @Test void firstLastExamples() {
        assertArrayEquals(new int[]{3, 4}, BinarySearch.firstLast(new int[]{5, 7, 7, 8, 8, 10}, 8));
        assertArrayEquals(new int[]{-1, -1}, BinarySearch.firstLast(new int[]{5, 7, 7, 8, 8, 10}, 6));
        assertArrayEquals(new int[]{-1, -1}, BinarySearch.firstLast(new int[]{}, 0));
        assertArrayEquals(new int[]{0, 0}, BinarySearch.firstLast(new int[]{1}, 1));
        assertArrayEquals(new int[]{0, 3}, BinarySearch.firstLast(new int[]{2, 2, 2, 2}, 2));
        assertArrayEquals(new int[]{-1, -1}, BinarySearch.firstLast(new int[]{1, 2}, 3));
    }

    @Test void extremeValues() {
        int[] a = {Integer.MIN_VALUE, 0, Integer.MAX_VALUE, Integer.MAX_VALUE};
        assertArrayEquals(new int[]{2, 3}, BinarySearch.firstLast(a, Integer.MAX_VALUE));
        assertArrayEquals(new int[]{0, 0}, BinarySearch.firstLast(a, Integer.MIN_VALUE));
        assertEquals(2, BinarySearch.searchInsert(a, Integer.MAX_VALUE));
        assertEquals(1, BinarySearch.searchInsert(a, Integer.MIN_VALUE + 1));
        assertEquals(Integer.MIN_VALUE, BinarySearch.findMin(new int[]{0, Integer.MAX_VALUE, Integer.MIN_VALUE}));
    }

    @Test void findMinExamples() {
        assertEquals(1, BinarySearch.findMin(new int[]{3, 4, 5, 1, 2}));
        assertEquals(0, BinarySearch.findMin(new int[]{4, 5, 6, 7, 0, 1, 2}));
        assertEquals(11, BinarySearch.findMin(new int[]{11, 13, 15, 17}));
        assertEquals(5, BinarySearch.findMin(new int[]{5}));
        assertEquals(1, BinarySearch.findMin(new int[]{2, 1}));
        assertEquals(1, BinarySearch.findMin(new int[]{1, 2}));
    }

    @Test void randomVsLinearScan() {
        Random rnd = new Random(3);
        for (int t = 0; t < 500; t++) {
            int n = rnd.nextInt(12);
            int[] a = new int[n];
            for (int i = 0; i < n; i++) a[i] = rnd.nextInt(8) - 4;
            Arrays.sort(a);
            int target = rnd.nextInt(10) - 5;
            int ins = 0;
            while (ins < n && a[ins] < target) ins++;
            assertEquals(ins, BinarySearch.searchInsert(a, target), Arrays.toString(a) + " " + target);
            int first = -1, last = -1;
            for (int i = 0; i < n; i++) if (a[i] == target) { if (first < 0) first = i; last = i; }
            assertArrayEquals(new int[]{first, last}, BinarySearch.firstLast(a, target), Arrays.toString(a) + " " + target);
        }
        for (int t = 0; t < 300; t++) {
            int n = 1 + rnd.nextInt(10);
            int[] a = new int[n];
            for (int i = 0; i < n; i++) a[i] = i * 3 - 7;
            int pivot = rnd.nextInt(n);
            int[] rot = new int[n];
            for (int i = 0; i < n; i++) rot[i] = a[(i + pivot) % n];
            assertEquals(-7, BinarySearch.findMin(rot), Arrays.toString(rot));
        }
    }

    @Test void largeInputIsFast() {
        int[] a = new int[5_000_000];
        for (int i = 0; i < a.length; i++) a[i] = i / 2;
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            for (int i = 0; i < 100_000; i++) BinarySearch.firstLast(a, i % 2_500_000);
        });
    }
}
