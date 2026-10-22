package practice.d20261012.a2;

/** A2 - see questions/A2-search-insert-position.md. Test: SearchInsertTest. Use the lower-bound template (hi exclusive). */
public final class SearchInsert {
    private SearchInsert() {}

    /** Sorted DISTINCT ints: index of target, or the index where it would be inserted. */
    public static int searchInsert(int[] a, int target) {
        throw new UnsupportedOperationException("TODO");
    }

    /** Sorted ints WITH duplicates: index of the FIRST occurrence of target, or -1 if absent. */
    public static int firstOccurrence(int[] a, int target) {
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Binary search on the answer: weights must be shipped in the given order, at most `days` days,
     * each day carries a prefix of the remaining packages up to `capacity`. Smallest capacity that works.
     * Weights are positive ints but the total can exceed int range, so use long.
     */
    public static long shipWithinDays(int[] weights, int days) {
        throw new UnsupportedOperationException("TODO");
    }
}
