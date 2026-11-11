package practice.d20261111.a2;

public final class QueueBasics {
    private QueueBasics() {}

    /** Average of the last `size` values (fewer at the start). Values may be near Integer.MAX_VALUE: the sum must not overflow. O(1) per call, O(size) memory. */
    public static final class MovingAverage {
        public MovingAverage(int size) { throw new UnsupportedOperationException("TODO"); }
        public double next(int val) { throw new UnsupportedOperationException("TODO"); }
    }

    /** ping(t): t strictly increases between calls; return how many pings happened in the closed window [t - 3000, t] (including this one). Amortised O(1). */
    public static final class RecentCounter {
        public RecentCounter() { throw new UnsupportedOperationException("TODO"); }
        public int ping(int t) { throw new UnsupportedOperationException("TODO"); }
    }

    /** '#' is a backspace (on an empty text it does nothing). Are the two typed strings equal? O(1) extra space: no StringBuilder, no stack. */
    public static boolean backspaceCompare(String s, String t) { throw new UnsupportedOperationException("TODO"); }
}
