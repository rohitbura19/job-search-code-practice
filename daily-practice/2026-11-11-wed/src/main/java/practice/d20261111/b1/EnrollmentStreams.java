package practice.d20261111.b1;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/** Streams / collectors only (no manual loops over the input). Every returned Map must iterate in the key order stated, so pick the map factory deliberately. */
public final class EnrollmentStreams {
    private EnrollmentStreams() {}

    /** course -> average grade (exact double), keys in natural order. */
    public static Map<String, Double> averageByCourse(List<Enrollment> list) { throw new UnsupportedOperationException("TODO"); }

    /** course -> best student (highest grade; ties -> alphabetically smallest name), keys in natural order. */
    public static Map<String, Optional<String>> topStudentPerCourse(List<Enrollment> list) { throw new UnsupportedOperationException("TODO"); }

    /** Students with at least minCourses enrollments AND average grade >= minAvg; sorted by average descending, then name ascending. */
    public static List<String> honourRoll(List<Enrollment> list, int minCourses, double minAvg) { throw new UnsupportedOperationException("TODO"); }

    /** Both keys always present: true = grade >= passMark, false = below; value = number of enrollments. */
    public static Map<Boolean, Long> passFailCounts(List<Enrollment> list, int passMark) { throw new UnsupportedOperationException("TODO"); }

    /** student -> distinct courses, sorted alphabetically; students in natural order. */
    public static Map<String, List<String>> coursesByStudent(List<Enrollment> list) { throw new UnsupportedOperationException("TODO"); }

    /** Decile histogram: grade/10*10 as key (0,10,...,90; a grade of 100 counts in bucket 90), only non-empty buckets, ascending keys. */
    public static Map<Integer, Long> gradeBuckets(List<Enrollment> list) { throw new UnsupportedOperationException("TODO"); }
}
