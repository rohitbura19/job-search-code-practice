package practice.d20261111.a2;

import java.util.ArrayDeque;

/** MovingAverage: circular buffer + running long sum. RecentCounter: queue, evict from the front while older than t - 3000 (each ping enters and leaves once).
 *  backspaceCompare: walk both strings from the back, counting pending backspaces, and compare the next surviving characters. O(n + m) / O(1). */
public final class QueueBasics {
    private QueueBasics() {}

    public static final class MovingAverage {
        private final int[] buf;
        private int count, head;
        private long sum;

        public MovingAverage(int size) {
            if (size <= 0) throw new IllegalArgumentException("size");
            buf = new int[size];
        }

        public double next(int val) {
            if (count == buf.length) { sum -= buf[head]; buf[head] = val; head = (head + 1) % buf.length; }
            else { buf[(head + count) % buf.length] = val; count++; }
            sum += val;
            return (double) sum / count;
        }
    }

    public static final class RecentCounter {
        private final ArrayDeque<Integer> q = new ArrayDeque<>();

        public RecentCounter() {}

        public int ping(int t) {
            q.addLast(t);
            while (q.peekFirst() < (long) t - 3000) q.pollFirst();
            return q.size();
        }
    }

    public static boolean backspaceCompare(String s, String t) {
        int i = s.length() - 1, j = t.length() - 1;
        while (true) {
            i = nextValid(s, i);
            j = nextValid(t, j);
            if (i < 0 || j < 0) return i < 0 && j < 0;
            if (s.charAt(i) != t.charAt(j)) return false;
            i--; j--;
        }
    }

    /** Index of the next character (at or before i) that survives the backspaces, or -1. */
    private static int nextValid(String s, int i) {
        int skip = 0;
        while (i >= 0) {
            if (s.charAt(i) == '#') { skip++; i--; }
            else if (skip > 0) { skip--; i--; }
            else break;
        }
        return i;
    }
}
