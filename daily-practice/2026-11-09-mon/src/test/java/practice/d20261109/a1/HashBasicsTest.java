package practice.d20261109.a1;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.*;
import org.junit.jupiter.api.Test;

class HashBasicsTest {
    private static void checkPair(int[] nums, int target, int[] r) {
        assertEquals(2, r.length);
        assertTrue(r[0] < r[1], "indices ascending and different");
        assertEquals(target, (long) nums[r[0]] + nums[r[1]]);
    }

    @Test void twoSumExamples() {
        checkPair(new int[] {2, 7, 11, 15}, 9, HashBasics.twoSum(new int[] {2, 7, 11, 15}, 9));
        assertArrayEquals(new int[] {1, 2}, HashBasics.twoSum(new int[] {3, 2, 4}, 6));
        assertArrayEquals(new int[] {0, 1}, HashBasics.twoSum(new int[] {3, 3}, 6));
        assertArrayEquals(new int[] {0, 3}, HashBasics.twoSum(new int[] {-4, 1, 9, 4}, 0));
    }

    @Test void twoSumOverflowTrap() {
        int[] a = {Integer.MIN_VALUE, Integer.MIN_VALUE + 10, 3, 7};
        checkPair(a, 10, HashBasics.twoSum(a, 10));
        int[] b = {Integer.MAX_VALUE, 1, Integer.MAX_VALUE - 1, 2};
        assertArrayEquals(new int[] {1, 2}, HashBasics.twoSum(b, Integer.MAX_VALUE));
    }

    @Test void twoSumRandomPlanted() {
        Random rnd = new Random(7);
        for (int t = 0; t < 500; t++) {
            int n = 2 + rnd.nextInt(10);
            int[] a = new int[n];
            for (int i = 0; i < n; i++) a[i] = rnd.nextInt(41) - 20;
            int i = rnd.nextInt(n), j;
            do { j = rnd.nextInt(n); } while (j == i);
            int target = a[i] + a[j];
            checkPair(a, target, HashBasics.twoSum(a, target));
        }
    }

    @Test void twoSumLarge() {
        int n = 300_000;
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = 2 * i;
        int target = a[n - 2] + a[n - 1];
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> checkPair(a, target, HashBasics.twoSum(a, target)));
    }

    private static Set<List<String>> norm(List<List<String>> groups) {
        Set<List<String>> out = new HashSet<>();
        for (List<String> g : groups) { List<String> c = new ArrayList<>(g); Collections.sort(c); out.add(c); }
        assertEquals(groups.size(), out.size() == groups.size() ? groups.size() : -1, "groups must be disjoint/unique");
        return out;
    }

    @Test void groupAnagramsExamples() {
        assertEquals(Set.of(List.of("ate", "eat", "tea"), List.of("nat", "tan"), List.of("bat")),
                norm(HashBasics.groupAnagrams(new String[] {"eat", "tea", "tan", "ate", "nat", "bat"})));
        assertEquals(Set.of(List.of("", "")), norm(HashBasics.groupAnagrams(new String[] {"", ""})));
        assertEquals(Set.of(List.of("ab", "ab", "ba")), norm(HashBasics.groupAnagrams(new String[] {"ab", "ab", "ba"})));
        assertTrue(HashBasics.groupAnagrams(new String[0]).isEmpty());
        assertEquals(1, HashBasics.groupAnagrams(new String[] {"a"}).size());
    }

    @Test void groupAnagramsDoesNotMergeDifferentMultisets() {
        assertEquals(2, HashBasics.groupAnagrams(new String[] {"aab", "abb"}).size());
        assertEquals(2, HashBasics.groupAnagrams(new String[] {"a", "aa"}).size());
    }

    @Test void majorityExamples() {
        assertEquals(3, HashBasics.majorityElement(new int[] {3, 2, 3}));
        assertEquals(2, HashBasics.majorityElement(new int[] {2, 2, 1, 1, 1, 2, 2}));
        assertEquals(-5, HashBasics.majorityElement(new int[] {-5}));
        assertEquals(Integer.MIN_VALUE, HashBasics.majorityElement(new int[] {1, Integer.MIN_VALUE, Integer.MIN_VALUE}));
    }

    @Test void majorityRandomPlantedAndLarge() {
        Random rnd = new Random(3);
        for (int t = 0; t < 300; t++) {
            int n = 1 + rnd.nextInt(15), m = n / 2 + 1, maj = rnd.nextInt(5);
            List<Integer> l = new ArrayList<>();
            for (int i = 0; i < m; i++) l.add(maj);
            while (l.size() < n) l.add(rnd.nextInt(5));
            Collections.shuffle(l, rnd);
            assertEquals(maj, HashBasics.majorityElement(l.stream().mapToInt(Integer::intValue).toArray()));
        }
        int[] big = new int[2_000_001];
        for (int i = 0; i < big.length; i++) big[i] = i % 2 == 0 ? 42 : i;
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> assertEquals(42, HashBasics.majorityElement(big)));
    }
}
