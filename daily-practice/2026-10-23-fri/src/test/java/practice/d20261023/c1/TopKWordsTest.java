package practice.d20261023.c1;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class TopKWordsTest {
    static List<String> brute(String[] w, int k) {
        Map<String, Integer> c = new HashMap<>();
        for (String s : w) c.merge(s, 1, Integer::sum);
        return c.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed().thenComparing(Map.Entry.comparingByKey()))
                .limit(k).map(Map.Entry::getKey).collect(Collectors.toList());
    }

    @Test void examples() {
        assertEquals(List.of("i", "love"), TopKWords.topKFrequent("i love leetcode i love coding".split(" "), 2));
        assertEquals(List.of("the", "is", "sunny", "day"),
                TopKWords.topKFrequent("the day is sunny the the the sunny is is".split(" "), 4));
    }

    @Test void tiesAreLexicographic() {
        assertEquals(List.of("a", "b"), TopKWords.topKFrequent(new String[]{"b", "a", "c", "b", "a", "c"}, 2));
        assertEquals(List.of("Z", "a"), TopKWords.topKFrequent(new String[]{"a", "Z"}, 2));   // uppercase sorts before lowercase
    }

    @Test void singleAndAll() {
        assertEquals(List.of("x"), TopKWords.topKFrequent(new String[]{"x", "x", "x"}, 1));
        assertEquals(List.of("a", "b", "c"), TopKWords.topKFrequent(new String[]{"c", "b", "a"}, 3));
    }

    @Test void randomAgainstFullSort() {
        Random rnd = new Random(5);
        for (int t = 0; t < 500; t++) {
            String[] w = new String[1 + rnd.nextInt(40)];
            for (int i = 0; i < w.length; i++) w[i] = "w" + rnd.nextInt(10);
            int distinct = (int) Arrays.stream(w).distinct().count();
            int k = 1 + rnd.nextInt(distinct);
            assertEquals(brute(w, k), TopKWords.topKFrequent(w, k), Arrays.toString(w) + " k=" + k);
        }
    }

    @Test void largeInput() {
        Random rnd = new Random(1);
        String[] w = new String[500_000];
        for (int i = 0; i < w.length; i++) w[i] = "word" + (int) (Math.abs(rnd.nextGaussian()) * 20_000);
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> assertEquals(brute(w, 100), TopKWords.topKFrequent(w, 100)));
    }
}
