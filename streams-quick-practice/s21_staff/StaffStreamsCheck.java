package s21_staff;

import static check.Assert.*;

import java.util.*;
import check.Test;
import s21_staff.StaffStreams.Employee;

class StaffStreamsCheck {
    private static Employee e(String n, String d, String c, double s, String... skills) { return new Employee(n, d, c, s, List.of(skills)); }

    private final List<Employee> staff = List.of(
            e("asha", "eng", "pune", 90, "java", "sql"),
            e("bo", "eng", "pune", 120, "java", "go"),
            e("cy", "ops", "delhi", 50),
            e("dee", "eng", "delhi", 120, "kafka"),
            e("eli", "ops", "pune", 70, "bash", "sql"));

    @Test void spread() {
        assertArrayEquals(new double[]{50, 120}, StaffStreams.spread(staff));
        assertArrayEquals(new double[]{7, 7}, StaffStreams.spread(List.of(e("x", "d", "c", 7))));
        assertEquals(0, StaffStreams.spread(List.of()).length);
    }

    @Test void skills() {
        Map<String, SortedSet<String>> m = StaffStreams.skillsByDept(staff);
        assertEquals(List.of("eng", "ops"), new ArrayList<>(m.keySet()));
        assertEquals(List.of("go", "java", "kafka", "sql"), new ArrayList<>(m.get("eng")));
        assertEquals(List.of("bash", "sql"), new ArrayList<>(m.get("ops")));
        assertTrue(StaffStreams.skillsByDept(List.of()).isEmpty());
        assertTrue(StaffStreams.skillsByDept(List.of(e("z", "x", "c", 1))).get("x").isEmpty());
    }

    @Test void highEarners() {
        Map<String, List<String>> m = StaffStreams.highEarnersByCity(staff, 90);
        assertEquals(List.of("delhi", "pune"), new ArrayList<>(m.keySet()));
        assertEquals(List.of("dee"), m.get("delhi"));
        assertEquals(List.of("bo"), m.get("pune"));
        assertEquals(List.of(), StaffStreams.highEarnersByCity(staff, 500).get("pune"));
    }

    @Test void headcount() {
        assertEquals(Map.of(true, 3L, false, 2L), StaffStreams.headcount(staff, 90));
        assertEquals(Map.of(true, 0L, false, 0L), StaffStreams.headcount(List.of(), 1));
    }

    @Test void topN() {
        Map<String, List<String>> m = StaffStreams.topNPerDept(staff, 2);
        assertEquals(List.of("bo", "dee"), m.get("eng"));
        assertEquals(List.of("eli", "cy"), m.get("ops"));
        assertEquals(List.of(), StaffStreams.topNPerDept(staff, 0).get("eng"));
        assertEquals(List.of("bo", "dee", "asha"), StaffStreams.topNPerDept(staff, 10).get("eng"));
    }

    @Test void inputNotMutated() {
        List<Employee> copy = new ArrayList<>(staff);
        StaffStreams.topNPerDept(staff, 1);
        StaffStreams.skillsByDept(staff);
        assertEquals(copy, staff);
    }

    public static void main(String[] args) {
        check.Runner.run(StaffStreamsCheck.class);
    }
}
