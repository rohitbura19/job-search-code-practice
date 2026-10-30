package practice.d20261030.b1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class CampusStatsTest {
    static final Course DS = new Course("CS101", 4), DB = new Course("CS205", 3), AI = new Course("CS310", 5), MA = new Course("MA101", 2);

    static final List<Student> DATA = List.of(
            new Student("s1", "Ravi", "Pune", List.of(DS, DB)),
            new Student("s2", "Asha", "Pune", List.of(DS, AI)),
            new Student("s3", "Meera", "Delhi", List.of(DS, DS, MA)),
            new Student("s4", "Zed", "Delhi", List.of()),
            new Student("s5", "Bala", "Chennai", List.of(AI, DB)));

    @Test void courseCodes() {
        assertEquals(List.of("CS101", "CS205", "CS310", "MA101"), new ArrayList<>(CampusStats.courseCodes(DATA)));
        assertTrue(CampusStats.courseCodes(List.of()).isEmpty());
        assertTrue(CampusStats.courseCodes(List.of(new Student("x", "X", "C", List.of()))).isEmpty());
    }

    @Test void creditsByIdKeepsInputOrder() {
        Map<String, Integer> m = CampusStats.creditsById(DATA);
        assertEquals(List.of("s1", "s2", "s3", "s4", "s5"), new ArrayList<>(m.keySet()));
        assertEquals(List.of(7, 9, 10, 0, 8), new ArrayList<>(m.values()));
    }

    @Test void enrollmentOrderedByCountThenCode() {
        Map<String, Long> m = CampusStats.enrollmentByCourse(DATA);
        assertEquals(List.of("CS101", "CS205", "CS310", "MA101"), new ArrayList<>(m.keySet()));
        assertEquals(List.of(3L, 2L, 2L, 1L), new ArrayList<>(m.values()));   // Meera's duplicate CS101 counts once
        assertTrue(CampusStats.enrollmentByCourse(List.of()).isEmpty());
    }

    @Test void namesByCitySortedAndJoined() {
        Map<String, String> m = CampusStats.namesByCity(DATA);
        assertEquals(List.of("Chennai", "Delhi", "Pune"), new ArrayList<>(m.keySet()));
        assertEquals("Bala", m.get("Chennai"));
        assertEquals("Meera, Zed", m.get("Delhi"));
        assertEquals("Asha, Ravi", m.get("Pune"));
    }

    @Test void heaviestLoadTieBreaks() {
        assertEquals("s3", CampusStats.heaviestLoad(DATA).orElseThrow().id());
        List<Student> tie = List.of(
                new Student("b", "Kim", "X", List.of(DS)), new Student("a", "Kim", "X", List.of(DS)),
                new Student("c", "Ann", "X", List.of(new Course("Q", 1), new Course("R", 3))));
        assertEquals("c", CampusStats.heaviestLoad(tie).orElseThrow().id());                 // equal credits -> smaller name
        assertEquals("a", CampusStats.heaviestLoad(tie.subList(0, 2)).orElseThrow().id());   // equal name -> smaller id
        assertEquals(Optional.empty(), CampusStats.heaviestLoad(List.of()));
    }

    @Test void chunk() {
        assertEquals(List.of(List.of(1, 2), List.of(3, 4), List.of(5)), CampusStats.chunk(List.of(1, 2, 3, 4, 5), 2));
        assertEquals(List.of(List.of(1, 2, 3)), CampusStats.chunk(List.of(1, 2, 3), 10));
        assertEquals(List.of(), CampusStats.chunk(List.<Integer>of(), 3));
        assertEquals(List.of(List.of("a"), List.of("b")), CampusStats.chunk(List.of("a", "b"), 1));
        assertThrows(IllegalArgumentException.class, () -> CampusStats.chunk(List.of(1), 0));
    }

    @Test void inputNotModified() {
        List<Student> copy = new ArrayList<>(DATA);
        CampusStats.enrollmentByCourse(copy);
        CampusStats.namesByCity(copy);
        assertEquals(DATA, copy);
    }
}
