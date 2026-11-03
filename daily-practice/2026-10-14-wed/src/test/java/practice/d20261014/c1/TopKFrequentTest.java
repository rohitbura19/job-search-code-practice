package practice.d20261014.c1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class TopKFrequentTest {

    @Test @DisplayName("sheet examples")
    void examples() {
        assertEquals("[1, 2]", Arrays.toString(TopKFrequent.topKFrequent(new int[]{1, 1, 1, 2, 2, 3}, 2)));
        assertEquals("[1]", Arrays.toString(TopKFrequent.topKFrequent(new int[]{1}, 1)));
    }

    @Test @DisplayName("negatives, zero, k equals the number of distinct values")
    void negatives() {
        assertEquals("[-1, 0, 5]", Arrays.toString(TopKFrequent.topKFrequent(new int[]{5, -1, -1, -1, 0, 0, 5, -1}, 3)));
        assertEquals("[7]", Arrays.toString(TopKFrequent.topKFrequent(new int[]{7, 7, 7}, 1)));
        assertEquals("[]", Arrays.toString(TopKFrequent.topKFrequent(new int[]{1, 2, 3}, 0)));
    }

    @Test @DisplayName("ties: any order inside the same frequency, but the right set")
    void ties() {
        int[] r = TopKFrequent.topKFrequent(new int[]{4, 4, 4, 1, 1, 2, 2, 3}, 3);
        assertEquals(4, r[0]);
        int[] tail = {r[1], r[2]}; Arrays.sort(tail);
        assertEquals("[1, 2]", Arrays.toString(tail));
    }

    @Test @DisplayName("random inputs with distinct frequencies against a sort oracle")
    void random() {
        Random rnd = new Random(14);
        for (int t = 0; t < 200; t++) {
            int distinct = 1 + rnd.nextInt(15);
            List<Integer> vals = new ArrayList<>();
            for (int i = 0; i < distinct; i++) vals.add(rnd.nextInt(1000) - 500);
            vals = new ArrayList<>(new java.util.LinkedHashSet<>(vals));
            List<Integer> data = new ArrayList<>();
            int[] byFreq = new int[vals.size()];
            for (int i = 0; i < vals.size(); i++) {           // value i appears i+1 times: all frequencies differ
                for (int c = 0; c <= i; c++) data.add(vals.get(i));
                byFreq[vals.size() - 1 - i] = vals.get(i);     // highest frequency first
            }
            Collections.shuffle(data, rnd);
            int k = 1 + rnd.nextInt(vals.size());
            int[] expect = Arrays.copyOf(byFreq, k);
            assertEquals(Arrays.toString(expect),
                    Arrays.toString(TopKFrequent.topKFrequent(data.stream().mapToInt(Integer::intValue).toArray(), k)));
        }
    }

    @Test @DisplayName("2 million elements, few distinct: linear")
    void large() {
        int[] a = new int[2_000_000];
        Random rnd = new Random(1);
        for (int i = 0; i < a.length; i++) a[i] = rnd.nextInt(5) == 0 ? 42 : rnd.nextInt(1000);
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> assertEquals(42, TopKFrequent.topKFrequent(a, 1)[0]));
    }

    @Test @DisplayName("200k distinct values (worst case for buckets and heaps)")
    void manyDistinct() {
        int[] a = new int[200_000];
        for (int i = 0; i < a.length; i++) a[i] = i;
        a[5] = 199_999; a[6] = 199_999;       // 199999 now appears three times
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> assertEquals(199_999, TopKFrequent.topKFrequent(a, 2)[0]));
    }
}
