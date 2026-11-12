package s17_staff;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/** Streams / collections. Prefer one pipeline per method; no mutable state captured in lambdas. */
public final class StaffStats {
    private StaffStats() {}

    /** dept -> average salary; iteration order by dept ascending. Empty list -> empty map. */
    public static Map<String, Double> avgSalaryByDept(List<Employee> es) { throw new UnsupportedOperationException("TODO"); }

    /** Highest paid employee of the dept; ties: smaller name wins. Empty if the dept has nobody. */
    public static Optional<Employee> highestPaid(List<Employee> es, String dept) { throw new UnsupportedOperationException("TODO"); }

    /** dept (ascending) -> names ordered by salary descending, then name ascending. */
    public static Map<String, List<String>> namesBySalaryDesc(List<Employee> es) { throw new UnsupportedOperationException("TODO"); }

    /** Name of the highest paid employee per dept (ties: smaller name), listed by that salary descending, then dept ascending. */
    public static List<String> topPerDept(List<Employee> es) { throw new UnsupportedOperationException("TODO"); }

    /** {true -> count with age >= threshold, false -> count with age < threshold}. Both keys always present. */
    public static Map<Boolean, Long> seniorCount(List<Employee> es, int threshold) { throw new UnsupportedOperationException("TODO"); }

    /** Case-insensitive word counts over all lines; a word is a maximal run of ASCII letters. Keys ascending. */
    public static Map<String, Long> wordFrequency(List<String> lines) { throw new UnsupportedOperationException("TODO"); }

    /**
     * A map holding at most capacity entries; inserting beyond that evicts the least recently used one.
     * Both get and put count as a use. capacity < 1 -> IllegalArgumentException. Hint: LinkedHashMap.
     */
    public static <K, V> Map<K, V> lruCache(int capacity) { throw new UnsupportedOperationException("TODO"); }
}
