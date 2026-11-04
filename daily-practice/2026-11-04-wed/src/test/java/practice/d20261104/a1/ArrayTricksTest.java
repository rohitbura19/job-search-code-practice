package practice.d20261104.a1;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.*;
import org.junit.jupiter.api.Test;

class ArrayTricksTest {
    @Test void missingNumberExamples() {
        assertEquals(2, ArrayTricks.missingNumber(new int[]{3, 0, 1}));
        assertEquals(2, ArrayTricks.missingNumber(new int[]{0, 1}));
        assertEquals(8, ArrayTricks.missingNumber(new int[]{9, 6, 4, 2, 3, 5, 7, 0, 1}));
        assertEquals(0, ArrayTricks.missingNumber(new int[]{}));
        assertEquals(1, ArrayTricks.missingNumber(new int[]{0}));
        assertEquals(0, ArrayTricks.missingNumber(new int[]{1}));
    }

    @Test void missingNumberLargeNoOverflow() {
        int n = 1_000_000;
        List<Integer> all = new ArrayList<>();
        for (int i = 0; i <= n; i++) all.add(i);
        Collections.shuffle(all, new Random(1));
        int missing = all.remove(12345);
        int[] a = all.stream().mapToInt(Integer::intValue).toArray();
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> assertEquals(missing, ArrayTricks.missingNumber(a)));
    }

    @Test void findDuplicatesExamples() {
        assertEquals(List.of(2, 3), ArrayTricks.findDuplicates(new int[]{4, 3, 2, 7, 8, 2, 3, 1}));
        assertEquals(List.of(1), ArrayTricks.findDuplicates(new int[]{1, 1, 2}));
        assertEquals(List.of(), ArrayTricks.findDuplicates(new int[]{1}));
        assertEquals(List.of(), ArrayTricks.findDuplicates(new int[]{}));
        assertEquals(List.of(1, 2), ArrayTricks.findDuplicates(new int[]{2, 1, 2, 1}));
    }

    @Test void findDuplicatesRestoresInput() {
        int[] a = {4, 3, 2, 7, 8, 2, 3, 1};
        int[] copy = a.clone();
        ArrayTricks.findDuplicates(a);
        assertArrayEquals(copy, a);
    }

    @Test void findDuplicatesRandomVsSet() {
        Random rnd = new Random(7);
        for (int t = 0; t < 300; t++) {
            int n = 1 + rnd.nextInt(30);
            int[] a = new int[n];
            Map<Integer, Integer> cnt = new TreeMap<>();
            for (int i = 0; i < n; i++) {
                a[i] = 1 + rnd.nextInt(n);
                cnt.merge(a[i], 1, Integer::sum);
            }
            // the contract says each value appears at most twice; keep only such inputs
            if (cnt.values().stream().anyMatch(c -> c > 2)) continue;
            List<Integer> expected = cnt.entrySet().stream().filter(e -> e.getValue() == 2).map(Map.Entry::getKey).toList();
            assertEquals(expected, ArrayTricks.findDuplicates(a), Arrays.toString(a));
        }
    }

    @Test void firstUniqChar() {
        assertEquals(0, ArrayTricks.firstUniqChar("leetcode"));
        assertEquals(2, ArrayTricks.firstUniqChar("loveleetcode"));
        assertEquals(-1, ArrayTricks.firstUniqChar("aabb"));
        assertEquals(-1, ArrayTricks.firstUniqChar(""));
        assertEquals(0, ArrayTricks.firstUniqChar("z"));
        assertEquals(2, ArrayTricks.firstUniqChar("aabxa"));
        String big = "ab".repeat(500_000) + "c";
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> assertEquals(1_000_000, ArrayTricks.firstUniqChar(big)));
    }
}
