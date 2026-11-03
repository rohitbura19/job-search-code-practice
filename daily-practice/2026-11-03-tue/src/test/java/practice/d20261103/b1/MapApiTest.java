package practice.d20261103.b1;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import org.junit.jupiter.api.Test;

class MapApiTest {
    @Test void wordCounts() {
        assertEquals(Map.of(), MapApi.wordCounts(List.of()));
        assertEquals(Map.of("a", 2, "b", 1, "A", 1), MapApi.wordCounts(List.of("a", "b", "a", "A")));
    }

    @Test void byInitialLowerCasesKeysAndKeepsOrder() {
        SortedMap<Character, List<String>> m = MapApi.byInitial(List.of("Banana", "apple", "Avocado", "blueberry", "cherry"));
        assertEquals(List.of('a', 'b', 'c'), new ArrayList<>(m.keySet()));
        assertEquals(List.of("apple", "Avocado"), m.get('a'));
        assertEquals(List.of("Banana", "blueberry"), m.get('b'));
        assertTrue(MapApi.byInitial(List.of()).isEmpty());
    }

    @Test void removeShortInPlace() {
        List<String> w = new ArrayList<>(List.of("a", "bb", "ccc", "", "dd"));
        assertEquals(2, MapApi.removeShort(w, 2));   // "a" and ""
        assertEquals(List.of("bb", "ccc", "dd"), w);
        assertEquals(0, MapApi.removeShort(w, 0));
        assertEquals(3, MapApi.removeShort(w, 10));
        assertTrue(w.isEmpty());
    }

    @Test void mergeAllDropsZeroTotals() {
        Map<String, Integer> r = MapApi.mergeAll(List.of(Map.of("x", 5, "y", 2), Map.of("x", -5, "z", 1), Map.of("y", 1, "x", 2)));
        assertEquals(Map.of("x", 2, "y", 3, "z", 1), r);
        assertEquals(Map.of(), MapApi.mergeAll(List.of(Map.of("a", 1), Map.of("a", -1))));
        assertEquals(Map.of(), MapApi.mergeAll(List.of()));
        // a total that returns to non-zero after passing through zero must be kept
        assertEquals(Map.of("a", 4), MapApi.mergeAll(List.of(Map.of("a", 1), Map.of("a", -1), Map.of("a", 4))));
    }

    @Test void invertSortedBothLevels() {
        Map<Integer, List<String>> r = MapApi.invert(Map.of("pear", 2, "fig", 2, "kiwi", 5, "plum", 1, "date", 2));
        assertEquals(List.of(1, 2, 5), new ArrayList<>(r.keySet()));
        assertEquals(List.of("date", "fig", "pear"), r.get(2));
        assertTrue(MapApi.invert(Map.of()).isEmpty());
    }

    @Test void topKTiesAndShortInput() {
        Map<String, Integer> c = Map.of("b", 3, "a", 3, "c", 5, "d", 1);
        assertEquals(List.of("c", "a", "b"), MapApi.topK(c, 3));
        assertEquals(List.of("c", "a", "b", "d"), MapApi.topK(c, 10));
        assertEquals(List.of(), MapApi.topK(c, 0));
        assertEquals(List.of(), MapApi.topK(Map.of(), 2));
    }

    @Test void randomAgainstManualLoops() {
        Random rnd = new Random(11);
        for (int t = 0; t < 200; t++) {
            List<String> words = new ArrayList<>();
            for (int i = rnd.nextInt(30); i > 0; i--) words.add("w" + rnd.nextInt(6));
            Map<String, Integer> expect = new HashMap<>();
            for (String w : words) expect.put(w, expect.getOrDefault(w, 0) + 1);
            assertEquals(expect, MapApi.wordCounts(words));
        }
    }
}
