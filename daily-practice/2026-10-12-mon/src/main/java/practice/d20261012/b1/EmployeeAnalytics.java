package practice.d20261012.b1;

import java.util.List;
import java.util.Map;

/** B1 - see questions/B1-employee-analytics.md. Test: EmployeeAnalyticsTest. One stream pipeline per method, no loops. */
public final class EmployeeAnalytics {
    private EmployeeAnalytics() {}

    /** 1. true -> earning >= 3,000,000 (30 lakh), false -> the rest. Both keys always present; encounter order kept. */
    public static Map<Boolean, List<Employee>> splitByThirtyLakh(List<Employee> emps) {
        throw new UnsupportedOperationException("TODO");
    }

    /** 2. Highest paid employee per department. */
    public static Map<String, Employee> topPerDept(List<Employee> emps) {
        throw new UnsupportedOperationException("TODO");
    }

    /** 3. All distinct skills across employees, sorted. */
    public static List<String> distinctSkills(List<Employee> emps) {
        throw new UnsupportedOperationException("TODO");
    }

    /** 4. [lowest paid, highest paid] in a single pass (Collectors.teeing). Empty input: NoSuchElementException. */
    public static List<Employee> minAndMax(List<Employee> emps) {
        throw new UnsupportedOperationException("TODO");
    }
}
