package practice.d20261012.d1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class MedianFinderTest {

    static double oracle(List<Integer> xs) {
        List<Integer> s = new ArrayList<>(xs); Collections.sort(s);
        int n = s.size();
        return n % 2 == 1 ? s.get(n / 2) : (s.get(n / 2 - 1) + (double) s.get(n / 2)) / 2.0;
    }

    @Test @DisplayName("examples from the question: 2,3,4 -> 3 and 2,3 -> 2.5")
    void examples() {
        MedianFinder m = new MedianFinder();
        m.addNum(2); assertEquals(2.0, m.findMedian(), 1e-9);
        m.addNum(3); assertEquals(2.5, m.findMedian(), 1e-9);
        m.addNum(4); assertEquals(3.0, m.findMedian(), 1e-9);
    }

    @Test @DisplayName("single value, duplicates, negatives, descending input")
    void edges() {
        MedianFinder m = new MedianFinder();
        m.addNum(-5); assertEquals(-5.0, m.findMedian(), 1e-9);
        m.addNum(-5); assertEquals(-5.0, m.findMedian(), 1e-9);
        m.addNum(-1); assertEquals(-5.0, m.findMedian(), 1e-9);
        m.addNum(-1); assertEquals(-3.0, m.findMedian(), 1e-9);
        MedianFinder d = new MedianFinder();
        for (int i = 10; i >= 1; i--) d.addNum(i);
        assertEquals(5.5, d.findMedian(), 1e-9);
    }

    @Test @DisplayName("no int overflow when averaging two huge values")
    void overflow() {
        MedianFinder m = new MedianFinder();
        m.addNum(Integer.MAX_VALUE); m.addNum(Integer.MAX_VALUE);
        assertEquals((double) Integer.MAX_VALUE, m.findMedian(), 1e-9);
        MedianFinder n = new MedianFinder();
        n.addNum(Integer.MIN_VALUE); n.addNum(Integer.MIN_VALUE);
        assertEquals((double) Integer.MIN_VALUE, n.findMedian(), 1e-9);
        MedianFinder o = new MedianFinder();
        o.addNum(Integer.MAX_VALUE); o.addNum(Integer.MIN_VALUE);
        assertEquals(-0.5, o.findMedian(), 1e-9);
    }

    @Test @DisplayName("random streams match sort-and-pick after EVERY insert")
    void randomOracle() {
        Random rnd = new Random(31);
        for (int t = 0; t < 40; t++) {
            MedianFinder m = new MedianFinder(); List<Integer> seen = new ArrayList<>();
            int range = t % 2 == 0 ? 10 : 1_000_000;
            for (int i = 0; i < 300; i++) {
                int x = rnd.nextInt(2 * range) - range;
                m.addNum(x); seen.add(x);
                assertEquals(Double.valueOf(oracle(seen)), Double.valueOf(m.findMedian()), "after " + seen.size() + " values");
            }
        }
    }

    @Test @DisplayName("200,000 adds each followed by a median query finish fast (re-sorting would not)")
    void bigIsFast() {
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> {
            MedianFinder m = new MedianFinder(); Random rnd = new Random(2);
            double last = 0;
            for (int i = 0; i < 200_000; i++) { m.addNum(rnd.nextInt()); last = m.findMedian(); }
            assertTrue(Math.abs(last) < 1.0e9);          // the median of uniform ints is near 0
        });
    }

    // ---------- small value range ----------

    @Test @DisplayName("SmallRangeMedian: examples, one value, even/odd counts")
    void smallRange() {
        SmallRangeMedian m = new SmallRangeMedian();
        m.add(2); assertEquals(2.0, m.median(), 1e-9);
        m.add(3); assertEquals(2.5, m.median(), 1e-9);
        m.add(4); assertEquals(3.0, m.median(), 1e-9);
        m.add(0); m.add(100);
        assertEquals(3.0, m.median(), 1e-9);
        m.add(100);
        assertEquals(3.5, m.median(), 1e-9);
    }

    @Test @DisplayName("SmallRangeMedian: values outside 0..100 are rejected")
    void smallRangeRejects() {
        SmallRangeMedian m = new SmallRangeMedian();
        assertThrows(IllegalArgumentException.class, () -> m.add(-1));
        assertThrows(IllegalArgumentException.class, () -> m.add(101));
        m.add(0); m.add(100);
        assertEquals(50.0, m.median(), 1e-9);
    }

    @Test @DisplayName("SmallRangeMedian: random streams match the oracle; 2,000,000 adds are fast")
    void smallRangeRandom() {
        Random rnd = new Random(77);
        SmallRangeMedian m = new SmallRangeMedian(); List<Integer> seen = new ArrayList<>();
        for (int i = 0; i < 500; i++) {
            int x = rnd.nextInt(101); m.add(x); seen.add(x);
            assertEquals(Double.valueOf(oracle(seen)), Double.valueOf(m.median()), "after " + seen.size());
        }
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> {
            SmallRangeMedian big = new SmallRangeMedian();
            for (int i = 0; i < 2_000_000; i++) big.add(i % 101);
            assertEquals(50.0, big.median(), 1.0);
        });
    }
}
