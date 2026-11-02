package practice.d20261102.b1;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import org.junit.jupiter.api.Test;

class StaffStatsTest {
    static final List<Employee> STAFF = List.of(
            new Employee("Asha", "eng", 150, 41), new Employee("Bo", "eng", 120, 29),
            new Employee("Cy", "eng", 150, 35), new Employee("Di", "ops", 90, 52),
            new Employee("Ed", "ops", 95, 27), new Employee("Flo", "hr", 150, 45));

    @Test void averages() {
        Map<String, Double> m = StaffStats.avgSalaryByDept(STAFF);
        assertEquals(List.of("eng", "hr", "ops"), new ArrayList<>(m.keySet()));
        assertEquals(140.0, m.get("eng"), 1e-9);
        assertEquals(92.5, m.get("ops"), 1e-9);
        assertTrue(StaffStats.avgSalaryByDept(List.of()).isEmpty());
    }

    @Test void highestPaidWithTieAndMissingDept() {
        assertEquals("Asha", StaffStats.highestPaid(STAFF, "eng").orElseThrow().name());
        assertEquals("Ed", StaffStats.highestPaid(STAFF, "ops").orElseThrow().name());
        assertTrue(StaffStats.highestPaid(STAFF, "legal").isEmpty());
        assertTrue(StaffStats.highestPaid(List.of(), "eng").isEmpty());
    }

    @Test void namesOrdered() {
        Map<String, List<String>> m = StaffStats.namesBySalaryDesc(STAFF);
        assertEquals(List.of("eng", "hr", "ops"), new ArrayList<>(m.keySet()));
        assertEquals(List.of("Asha", "Cy", "Bo"), m.get("eng"));
        assertEquals(List.of("Ed", "Di"), m.get("ops"));
        assertTrue(StaffStats.namesBySalaryDesc(List.of()).isEmpty());
    }

    @Test void topPerDeptOrdering() {
        assertEquals(List.of("Asha", "Flo", "Ed"), StaffStats.topPerDept(STAFF)); // 150 eng, 150 hr, 95 ops
        assertEquals(List.of(), StaffStats.topPerDept(List.of()));
    }

    @Test void partitionHasBothKeys() {
        assertEquals(Map.of(true, 3L, false, 3L), StaffStats.seniorCount(STAFF, 40));
        assertEquals(Map.of(true, 0L, false, 6L), StaffStats.seniorCount(STAFF, 99));
        assertEquals(Map.of(true, 0L, false, 0L), StaffStats.seniorCount(List.of(), 1));
    }

    @Test void wordFrequencyBasics() {
        Map<String, Long> m = StaffStats.wordFrequency(List.of("The cat, the DOG.", "", "  cat-cat! 42 dog's"));
        assertEquals("{cat=3, dog=2, s=1, the=2}", m.toString());
        assertTrue(StaffStats.wordFrequency(List.of()).isEmpty());
        assertTrue(StaffStats.wordFrequency(List.of("123 !!! ---")).isEmpty());
    }

    @Test void wordFrequencyRandomAgainstLoop() {
        Random r = new Random(6);
        String[] vocab = {"a", "B", "cc", "Dd", "e1f", "-", " "};
        for (int t = 0; t < 100; t++) {
            List<String> lines = new ArrayList<>();
            Map<String, Long> exp = new TreeMap<>();
            for (int i = 0; i < r.nextInt(6); i++) {
                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < r.nextInt(8); j++) sb.append(vocab[r.nextInt(vocab.length)]).append(' ');
                lines.add(sb.toString());
                for (String w : sb.toString().toLowerCase().split("[^a-z]+")) if (!w.isEmpty()) exp.merge(w, 1L, Long::sum);
            }
            assertEquals(exp, StaffStats.wordFrequency(lines));
        }
    }

    @Test void lruEvictsLeastRecentlyUsed() {
        Map<String, Integer> c = StaffStats.lruCache(2);
        c.put("a", 1); c.put("b", 2);
        assertEquals(1, c.get("a"));      // a is now most recent
        c.put("c", 3);                    // evicts b
        assertEquals(Set.of("a", "c"), c.keySet());
        c.put("a", 10);                   // re-put counts as a use
        c.put("d", 4);                    // evicts c
        assertEquals(List.of("a", "d"), new ArrayList<>(c.keySet()));
        assertEquals(2, c.size());
    }

    @Test void lruCapacityOneAndInvalid() {
        Map<Integer, Integer> c = StaffStats.lruCache(1);
        c.put(1, 1); c.put(2, 2);
        assertEquals(Map.of(2, 2), c);
        assertThrows(IllegalArgumentException.class, () -> StaffStats.lruCache(0));
    }
}
