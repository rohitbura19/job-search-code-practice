package practice.d20261012.b1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeAnalyticsTest {

    static final Employee ASHA = new Employee("Asha", "eng", 4_000_000, List.of("java", "aws"));
    static final Employee BALA = new Employee("Bala", "eng", 2_500_000, List.of("java", "sql"));
    static final Employee CHITRA = new Employee("Chitra", "ops", 3_000_000, List.of("linux"));
    static final Employee DEV = new Employee("Dev", "ops", 1_200_000, List.of("aws", "linux"));
    static final Employee ESHA = new Employee("Esha", "hr", 2_000_000, List.of("excel"));
    static final List<Employee> ALL = List.of(ASHA, BALA, CHITRA, DEV, ESHA);

    @Test @DisplayName("partition: 30 lakh is inclusive, order kept")
    void partition() {
        Map<Boolean, List<Employee>> m = EmployeeAnalytics.splitByThirtyLakh(ALL);
        assertEquals(List.of(ASHA, CHITRA), m.get(true));
        assertEquals(List.of(BALA, DEV, ESHA), m.get(false));
    }

    @Test @DisplayName("partition: both keys exist even when one side is empty")
    void partitionBothKeys() {
        Map<Boolean, List<Employee>> m = EmployeeAnalytics.splitByThirtyLakh(List.of(ASHA));
        assertTrue(m.containsKey(true));
        assertTrue(m.containsKey(false));
        assertTrue(m.get(false).isEmpty());
        Map<Boolean, List<Employee>> e = EmployeeAnalytics.splitByThirtyLakh(List.of());
        assertTrue(e.get(true).isEmpty() && e.get(false).isEmpty());
    }

    @Test @DisplayName("highest paid per department")
    void topPerDept() {
        Map<String, Employee> m = EmployeeAnalytics.topPerDept(ALL);
        assertEquals(3, m.size());
        assertSame(ASHA, m.get("eng"));
        assertSame(CHITRA, m.get("ops"));
        assertSame(ESHA, m.get("hr"));
        assertTrue(EmployeeAnalytics.topPerDept(List.of()).isEmpty());
    }

    @Test @DisplayName("distinct sorted skills")
    void skills() {
        assertEquals(List.of("aws", "excel", "java", "linux", "sql"), EmployeeAnalytics.distinctSkills(ALL));
        assertEquals(List.of(), EmployeeAnalytics.distinctSkills(List.of()));
        assertEquals(List.of(), EmployeeAnalytics.distinctSkills(List.of(new Employee("X", "d", 1, List.of()))));
    }

    @Test @DisplayName("min and max salary")
    void minMax() {
        assertEquals(List.of(DEV, ASHA), EmployeeAnalytics.minAndMax(ALL));
        assertEquals(List.of(ESHA, ESHA), EmployeeAnalytics.minAndMax(List.of(ESHA)));
    }

    @Test @DisplayName("min and max of an empty list throws NoSuchElementException")
    void minMaxEmpty() {
        assertThrows(NoSuchElementException.class, () -> EmployeeAnalytics.minAndMax(List.of()));
    }
}
