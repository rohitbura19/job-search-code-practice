package practice.d20261103.c1;

/** prereq[i] = {course, prerequisite}: the prerequisite must be taken strictly before the course. Courses are 0..n-1. 1 <= n <= 100_000. */
public final class CourseOrder {
    private CourseOrder() {}

    /** True iff all n courses can be completed (the graph has no cycle). */
    public static boolean canFinish(int n, int[][] prereq) { throw new UnsupportedOperationException("TODO"); }

    /** A valid order of all courses; among all valid orders return the lexicographically SMALLEST. Empty array if impossible. */
    public static int[] order(int n, int[][] prereq) { throw new UnsupportedOperationException("TODO"); }

    /** Fewest semesters if you may take any number of available courses per semester (prerequisites must be from EARLIER semesters). -1 if impossible. */
    public static int minSemesters(int n, int[][] prereq) { throw new UnsupportedOperationException("TODO"); }
}
