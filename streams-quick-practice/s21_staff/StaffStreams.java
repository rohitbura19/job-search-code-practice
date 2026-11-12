package s21_staff;

import java.util.*;

/** Streams practice: teeing, flatMapping, filtering, partitioningBy with a downstream, collectingAndThen. Do not mutate the input. */
public class StaffStreams {
    public record Employee(String name, String dept, String city, double salary, List<String> skills) {}

    /** {lowest salary, highest salary} in ONE pass (Collectors.teeing). Empty list -> empty array. */
    public static double[] spread(List<Employee> staff) { throw new UnsupportedOperationException("TODO"); }

    /** dept -> all distinct skills of its employees, sorted. Keys sorted (TreeMap). Use flatMapping. */
    public static Map<String, SortedSet<String>> skillsByDept(List<Employee> staff) { throw new UnsupportedOperationException("TODO"); }

    /** city -> sorted names of employees with salary STRICTLY above threshold. Keys sorted. Cities with nobody above still appear with an empty list. */
    public static Map<String, List<String>> highEarnersByCity(List<Employee> staff, double threshold) { throw new UnsupportedOperationException("TODO"); }

    /** true -> how many have salary >= threshold, false -> how many have less. Both keys always present. */
    public static Map<Boolean, Long> headcount(List<Employee> staff, double threshold) { throw new UnsupportedOperationException("TODO"); }

    /** dept -> names of its n best paid employees (salary descending, ties by name ascending). Keys sorted. n >= 0. */
    public static Map<String, List<String>> topNPerDept(List<Employee> staff, int n) { throw new UnsupportedOperationException("TODO"); }
}
