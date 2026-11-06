package practice.d20261106.b1;

import static java.util.stream.Collectors.*;

import java.util.*;

/**
 * spread: Collectors.teeing runs two collectors (minBy, maxBy) in ONE pass and merges them. O(n).
 * skillsByDept: flatMapping flattens each employee's skills into the group's TreeSet. O(total skills * log).
 * highEarnersByCity: filtering (unlike stream.filter) keeps groups that end up empty. O(n log n).
 * headcount: partitioningBy always has both keys. topN: group, then sort + limit inside collectingAndThen.
 */
public class StaffStreams {
    public record Employee(String name, String dept, String city, double salary, List<String> skills) {}

    public static double[] spread(List<Employee> staff) {
        return staff.stream().collect(teeing(
                minBy(Comparator.comparingDouble(Employee::salary)),
                maxBy(Comparator.comparingDouble(Employee::salary)),
                (lo, hi) -> lo.isPresent() ? new double[]{lo.get().salary(), hi.get().salary()} : new double[0]));
    }

    public static Map<String, SortedSet<String>> skillsByDept(List<Employee> staff) {
        return staff.stream().collect(groupingBy(Employee::dept, TreeMap::new,
                flatMapping(e -> e.skills().stream(), toCollection(TreeSet::new))));
    }

    public static Map<String, List<String>> highEarnersByCity(List<Employee> staff, double threshold) {
        return staff.stream().collect(groupingBy(Employee::city, TreeMap::new,
                filtering(e -> e.salary() > threshold,
                        collectingAndThen(mapping(Employee::name, toList()), l -> l.stream().sorted().toList()))));
    }

    public static Map<Boolean, Long> headcount(List<Employee> staff, double threshold) {
        return staff.stream().collect(partitioningBy(e -> e.salary() >= threshold, counting()));
    }

    public static Map<String, List<String>> topNPerDept(List<Employee> staff, int n) {
        Comparator<Employee> order = Comparator.comparingDouble(Employee::salary).reversed().thenComparing(Employee::name);
        return staff.stream().collect(groupingBy(Employee::dept, TreeMap::new,
                collectingAndThen(toList(), l -> l.stream().sorted(order).limit(n).map(Employee::name).toList())));
    }
}
