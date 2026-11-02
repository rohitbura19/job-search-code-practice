package practice.d20261102.b1;

import static java.util.stream.Collectors.*;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Function;

/**
 * groupingBy(classifier, TreeMap::new, downstream) gives ordered keys; downstream collectors do the per-group work.
 * partitioningBy always has both keys. LRU = LinkedHashMap(accessOrder=true) + removeEldestEntry. All O(n log n) or better.
 */
public final class StaffStats {
    private StaffStats() {}

    private static final Comparator<Employee> PAY_DESC_NAME =
            Comparator.comparingDouble(Employee::salary).reversed().thenComparing(Employee::name);

    public static Map<String, Double> avgSalaryByDept(List<Employee> es) {
        return es.stream().collect(groupingBy(Employee::dept, TreeMap::new, averagingDouble(Employee::salary)));
    }

    public static Optional<Employee> highestPaid(List<Employee> es, String dept) {
        return es.stream().filter(e -> e.dept().equals(dept)).min(PAY_DESC_NAME);
    }

    public static Map<String, List<String>> namesBySalaryDesc(List<Employee> es) {
        return es.stream().sorted(PAY_DESC_NAME)
                .collect(groupingBy(Employee::dept, TreeMap::new, mapping(Employee::name, toList())));
    }

    public static List<String> topPerDept(List<Employee> es) {
        return es.stream()
                .collect(groupingBy(Employee::dept, collectingAndThen(minBy(PAY_DESC_NAME), Optional::get)))
                .values().stream()
                .sorted(Comparator.comparingDouble(Employee::salary).reversed().thenComparing(Employee::dept))
                .map(Employee::name)
                .toList();
    }

    public static Map<Boolean, Long> seniorCount(List<Employee> es, int threshold) {
        return es.stream().collect(partitioningBy(e -> e.age() >= threshold, counting()));
    }

    public static Map<String, Long> wordFrequency(List<String> lines) {
        return lines.stream()
                .flatMap(l -> Arrays.stream(l.toLowerCase().split("[^a-z]+")))
                .filter(w -> !w.isEmpty())
                .collect(groupingBy(Function.identity(), TreeMap::new, counting()));
    }

    public static <K, V> Map<K, V> lruCache(int capacity) {
        if (capacity < 1) throw new IllegalArgumentException("capacity");
        return new LinkedHashMap<>(16, 0.75f, true) {
            @Override protected boolean removeEldestEntry(Map.Entry<K, V> eldest) { return size() > capacity; }
        };
    }
}
