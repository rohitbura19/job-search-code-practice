package practice.d20261111.b1;

import static java.util.stream.Collectors.*;

import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.TreeSet;

/** All O(n) single passes except honourRoll (sorts the qualifying students, O(s log s)). The map factory (TreeMap::new) is the second argument of groupingBy. */
public final class EnrollmentStreams {
    private EnrollmentStreams() {}

    public static Map<String, Double> averageByCourse(List<Enrollment> list) {
        return list.stream().collect(groupingBy(Enrollment::course, TreeMap::new, averagingInt(Enrollment::grade)));
    }

    public static Map<String, Optional<String>> topStudentPerCourse(List<Enrollment> list) {
        Comparator<Enrollment> best = Comparator.comparingInt(Enrollment::grade).thenComparing(Enrollment::student, Comparator.reverseOrder());
        return list.stream().collect(groupingBy(Enrollment::course, TreeMap::new,
                collectingAndThen(maxBy(best), o -> o.map(Enrollment::student))));
    }

    public static List<String> honourRoll(List<Enrollment> list, int minCourses, double minAvg) {
        Map<String, IntSummaryStatistics> stats = list.stream().collect(groupingBy(Enrollment::student, summarizingInt(Enrollment::grade)));
        return stats.entrySet().stream()
                .filter(e -> e.getValue().getCount() >= minCourses && e.getValue().getAverage() >= minAvg)
                .sorted(Comparator.<Map.Entry<String, IntSummaryStatistics>>comparingDouble(e -> -e.getValue().getAverage()).thenComparing(Map.Entry::getKey))
                .map(Map.Entry::getKey)
                .toList();
    }

    public static Map<Boolean, Long> passFailCounts(List<Enrollment> list, int passMark) {
        return list.stream().collect(partitioningBy(e -> e.grade() >= passMark, counting()));
    }

    public static Map<String, List<String>> coursesByStudent(List<Enrollment> list) {
        return list.stream().collect(groupingBy(Enrollment::student, TreeMap::new,
                collectingAndThen(mapping(Enrollment::course, toCollection(TreeSet::new)), List::copyOf)));
    }

    public static Map<Integer, Long> gradeBuckets(List<Enrollment> list) {
        return list.stream().collect(groupingBy(e -> Math.min(e.grade() / 10 * 10, 90), TreeMap::new, counting()));
    }
}
