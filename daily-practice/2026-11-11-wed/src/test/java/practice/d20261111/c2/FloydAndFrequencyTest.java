package practice.d20261111.c2;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class FloydAndFrequencyTest {
    @Test void findDuplicateExamples() {
        assertEquals(2, FloydAndFrequency.findDuplicate(new int[]{1, 3, 4, 2, 2}));
        assertEquals(3, FloydAndFrequency.findDuplicate(new int[]{3, 1, 3, 4, 2}));
        assertEquals(1, FloydAndFrequency.findDuplicate(new int[]{1, 1}));
        assertEquals(2, FloydAndFrequency.findDuplicate(new int[]{2, 2, 2, 2, 2}));
    }

    @Test void findDuplicateRandomAndNoMutation() {
        Random r = new Random(13);
        for (int t = 0; t < 1000; t++) {
            int n = 1 + r.nextInt(30);
            List<Integer> l = new ArrayList<>();
            for (int i = 1; i <= n; i++) l.add(i);
            int dup = 1 + r.nextInt(n);
            l.add(dup);
            for (int extra = r.nextInt(3); extra > 0 && l.size() < n + 1; extra--) { l.remove(l.size() - 1); l.add(dup); }
            Collections.shuffle(l, r);
            int[] a = l.stream().mapToInt(Integer::intValue).toArray();
            int[] copy = a.clone();
            int expected = 0;
            Set<Integer> seen = new HashSet<>();
            for (int x : a) if (!seen.add(x)) expected = x;
            assertEquals(expected, FloydAndFrequency.findDuplicate(a));
            assertArrayEquals(copy, a);
        }
    }

    @Test void findDuplicateLargeIsFast() {
        int n = 1_000_000;
        int[] a = new int[n + 1];
        for (int i = 0; i < n; i++) a[i] = i + 1;
        a[n] = 777_777;
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> assertEquals(777_777, FloydAndFrequency.findDuplicate(a)));
    }

    static ListNode chain(int n, int tailTo, ListNode[] nodes) {
        for (int i = 0; i < n; i++) nodes[i] = new ListNode(i);
        for (int i = 0; i + 1 < n; i++) nodes[i].next = nodes[i + 1];
        if (tailTo >= 0) nodes[n - 1].next = nodes[tailTo];
        return nodes[0];
    }

    @Test void cycleStartCases() {
        ListNode[] nodes = new ListNode[6];
        assertNull(FloydAndFrequency.cycleStart(null));
        ListNode head = chain(6, 2, nodes);
        assertSame(nodes[2], FloydAndFrequency.cycleStart(head));
        head = chain(6, 0, nodes);
        assertSame(nodes[0], FloydAndFrequency.cycleStart(head));
        head = chain(6, 5, nodes);                                   // self loop on the tail
        assertSame(nodes[5], FloydAndFrequency.cycleStart(head));
        head = chain(6, -1, nodes);
        assertNull(FloydAndFrequency.cycleStart(head));
        ListNode[] one = new ListNode[1];
        assertNull(FloydAndFrequency.cycleStart(chain(1, -1, one)));
        ListNode loop = chain(1, 0, one);
        assertSame(one[0], FloydAndFrequency.cycleStart(loop));
    }

    @Test void cycleStartRandomVsHashSet() {
        Random r = new Random(17);
        for (int t = 0; t < 500; t++) {
            int n = 1 + r.nextInt(40);
            ListNode[] nodes = new ListNode[n];
            int to = r.nextInt(n + 1) - 1;
            ListNode head = chain(n, to, nodes);
            assertSame(to < 0 ? null : nodes[to], FloydAndFrequency.cycleStart(head));
        }
    }

    @Test void topKExamples() {
        assertEquals(Set.of(1, 2), toSet(FloydAndFrequency.topKFrequent(new int[]{1, 1, 1, 2, 2, 3}, 2)));
        assertEquals(Set.of(1), toSet(FloydAndFrequency.topKFrequent(new int[]{1}, 1)));
        assertEquals(Set.of(-1), toSet(FloydAndFrequency.topKFrequent(new int[]{-1, -1, Integer.MIN_VALUE}, 1)));
        assertEquals(3, FloydAndFrequency.topKFrequent(new int[]{4, 5, 6}, 3).length);
    }

    static Set<Integer> toSet(int[] a) { return Arrays.stream(a).boxed().collect(Collectors.toSet()); }

    @Test void topKRandomFrequenciesMatch() {
        Random r = new Random(19);
        for (int t = 0; t < 500; t++) {
            int[] a = new int[1 + r.nextInt(40)];
            for (int i = 0; i < a.length; i++) a[i] = r.nextInt(8) - 4;
            Map<Integer, Long> f = Arrays.stream(a).boxed().collect(Collectors.groupingBy(x -> x, Collectors.counting()));
            int k = 1 + r.nextInt(f.size());
            int[] got = FloydAndFrequency.topKFrequent(a, k);
            assertEquals(k, got.length);
            assertEquals(k, toSet(got).size(), "values must be distinct");
            List<Long> expected = f.values().stream().sorted(Comparator.reverseOrder()).limit(k).toList();
            List<Long> actual = Arrays.stream(got).mapToObj(f::get).sorted(Comparator.reverseOrder()).toList();
            assertEquals(expected, actual);
        }
    }

    @Test void topKLargeIsFast() {
        int[] a = new int[2_000_000];
        for (int i = 0; i < a.length; i++) a[i] = (int) ((long) i * i % 100_003);
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> assertEquals(10, FloydAndFrequency.topKFrequent(a, 10).length));
    }
}
