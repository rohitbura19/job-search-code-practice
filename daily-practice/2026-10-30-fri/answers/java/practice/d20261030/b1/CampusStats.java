package practice.d20261030.b1;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.IntStream;

/** Each method is one pipeline, O(n) over the course entries plus the sorts (O(n log n) worst case). */
public final class CampusStats {
    private CampusStats() {}

    private static int credits(Student s) { return s.courses().stream().mapToInt(Course::credits).sum(); }

    public static SortedSet<String> courseCodes(List<Student> students) {
        return students.stream().flatMap(s -> s.courses().stream()).map(Course::code)
                .collect(java.util.stream.Collectors.toCollection(TreeSet::new));
    }

    public static Map<String, Integer> creditsById(List<Student> students) {
        return students.stream().collect(toMap(Student::id, CampusStats::credits, (a, b) -> a, LinkedHashMap::new));
    }

    public static Map<String, Long> enrollmentByCourse(List<Student> students) {
        Map<String, Long> counts = students.stream()
                .flatMap(s -> s.courses().stream().map(Course::code).distinct())
                .collect(groupingBy(c -> c, counting()));
        return counts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed().thenComparing(Map.Entry.comparingByKey()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, LinkedHashMap::new));
    }

    public static Map<String, String> namesByCity(List<Student> students) {
        return students.stream().collect(groupingBy(Student::city, TreeMap::new,
                collectingAndThen(mapping(Student::name, toList()),
                        names -> names.stream().sorted().collect(joining(", ")))));
    }

    public static Optional<Student> heaviestLoad(List<Student> students) {
        return students.stream().max(Comparator.comparingInt(CampusStats::credits)
                .thenComparing(Student::name, Comparator.reverseOrder())
                .thenComparing(Student::id, Comparator.reverseOrder()));
    }

    public static <T> List<List<T>> chunk(List<T> items, int size) {
        if (size < 1) throw new IllegalArgumentException("size < 1");
        return IntStream.range(0, (items.size() + size - 1) / size)
                .mapToObj(i -> List.copyOf(items.subList(i * size, Math.min(items.size(), (i + 1) * size))))
                .toList();
    }
}
