package practice.d20261111.a2;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;

class QueueBasicsTest {
    @Test void movingAverageExample() {
        QueueBasics.MovingAverage m = new QueueBasics.MovingAverage(3);
        assertEquals(1.0, m.next(1), 1e-9);
        assertEquals(5.5, m.next(10), 1e-9);
        assertEquals(14.0 / 3, m.next(3), 1e-9);
        assertEquals(6.0, m.next(5), 1e-9);
    }

    @Test void movingAverageNoOverflowAndSizeOne() {
        QueueBasics.MovingAverage m = new QueueBasics.MovingAverage(3);
        m.next(Integer.MAX_VALUE); m.next(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, m.next(Integer.MAX_VALUE), 1e-3);
        QueueBasics.MovingAverage one = new QueueBasics.MovingAverage(1);
        assertEquals(-4.0, one.next(-4), 1e-9);
        assertEquals(9.0, one.next(9), 1e-9);
    }

    @Test void movingAverageRandomVsBrute() {
        Random r = new Random(5);
        for (int size = 1; size <= 6; size++) {
            QueueBasics.MovingAverage m = new QueueBasics.MovingAverage(size);
            List<Integer> all = new ArrayList<>();
            for (int i = 0; i < 100; i++) {
                int v = r.nextInt(2000) - 1000;
                all.add(v);
                double avg = all.subList(Math.max(0, all.size() - size), all.size()).stream().mapToInt(Integer::intValue).average().orElseThrow();
                assertEquals(avg, m.next(v), 1e-9);
            }
        }
    }

    @Test void recentCounterExample() {
        QueueBasics.RecentCounter c = new QueueBasics.RecentCounter();
        assertEquals(1, c.ping(1));
        assertEquals(2, c.ping(100));
        assertEquals(3, c.ping(3001));
        assertEquals(3, c.ping(3002));
        assertEquals(1, c.ping(10_000_000));
    }

    @Test void recentCounterLargeIsFast() {
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {
            QueueBasics.RecentCounter c = new QueueBasics.RecentCounter();
            int last = 0;
            for (int t = 1; t <= 2_000_000; t++) last = c.ping(t);
            assertEquals(3001, last);
            assertEquals(1, c.ping(Integer.MAX_VALUE));
        });
    }

    @Test void backspaceExamples() {
        assertTrue(QueueBasics.backspaceCompare("ab#c", "ad#c"));
        assertTrue(QueueBasics.backspaceCompare("ab##", "c#d#"));
        assertFalse(QueueBasics.backspaceCompare("a#c", "b"));
        assertTrue(QueueBasics.backspaceCompare("", "#"));
        assertTrue(QueueBasics.backspaceCompare("a##b", "b"));
        assertFalse(QueueBasics.backspaceCompare("abc", "ab"));
        assertTrue(QueueBasics.backspaceCompare("###", ""));
    }

    @Test void backspaceRandomVsStack() {
        Random r = new Random(7);
        for (int t = 0; t < 3000; t++) {
            String a = rnd(r), b = rnd(r);
            assertEquals(type(a).equals(type(b)), QueueBasics.backspaceCompare(a, b), a + " vs " + b);
        }
    }

    private static String rnd(Random r) {
        StringBuilder sb = new StringBuilder();
        for (int i = r.nextInt(9); i > 0; i--) sb.append("ab#".charAt(r.nextInt(3)));
        return sb.toString();
    }

    private static String type(String s) {
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) { if (c == '#') { if (sb.length() > 0) sb.setLength(sb.length() - 1); } else sb.append(c); }
        return sb.toString();
    }
}
