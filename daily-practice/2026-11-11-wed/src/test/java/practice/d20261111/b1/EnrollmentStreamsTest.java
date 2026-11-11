package practice.d20261111.b1;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class EnrollmentStreamsTest {
    static Enrollment e(String s, String c, int g) { return new Enrollment(s, c, "2026-1", g); }

    static final List<Enrollment> DATA = List.of(
            e("ann", "math", 90), e("ann", "art", 100), e("bob", "math", 90), e("bob", "art", 50), e("cy", "math", 59),
            e("ann", "math", 70), e("dee", "cs", 100), e("dee", "cs", 60));

    @Test void averageByCourseSortedKeys() {
        Map<String, Double> m = EnrollmentStreams.averageByCourse(DATA);
        assertEquals(List.of("art", "cs", "math"), List.copyOf(m.keySet()));
        assertEquals(75.0, m.get("art"), 1e-9);
        assertEquals(80.0, m.get("cs"), 1e-9);
        assertEquals((90 + 90 + 59 + 70) / 4.0, m.get("math"), 1e-9);
        assertTrue(EnrollmentStreams.averageByCourse(List.of()).isEmpty());
    }

    @Test void topStudentTieBreaksAlphabetically() {
        Map<String, Optional<String>> m = EnrollmentStreams.topStudentPerCourse(DATA);
        assertEquals(Optional.of("ann"), m.get("math"));      // ann and bob both 90 -> ann
        assertEquals(Optional.of("ann"), m.get("art"));
        assertEquals(Optional.of("dee"), m.get("cs"));
        assertEquals(List.of("art", "cs", "math"), List.copyOf(m.keySet()));
    }

    @Test void honourRollFiltersAndSorts() {
        // averages: ann (90+100+70)/3=86.67, bob 70, cy 59, dee 80
        assertEquals(List.of("ann", "dee"), EnrollmentStreams.honourRoll(DATA, 2, 75));
        assertEquals(List.of("ann"), EnrollmentStreams.honourRoll(DATA, 3, 0));
        assertEquals(List.of("ann", "dee", "bob"), EnrollmentStreams.honourRoll(DATA, 2, 70));
        assertEquals(List.of(), EnrollmentStreams.honourRoll(DATA, 5, 0));
        List<Enrollment> tie = List.of(e("zed", "x", 80), e("amy", "x", 80));
        assertEquals(List.of("amy", "zed"), EnrollmentStreams.honourRoll(tie, 1, 80));
    }

    @Test void passFailHasBothKeys() {
        assertEquals(Map.of(true, 5L, false, 3L), EnrollmentStreams.passFailCounts(DATA, 70));
        assertEquals(Map.of(true, 0L, false, 0L), EnrollmentStreams.passFailCounts(List.of(), 50));
        assertEquals(Map.of(true, 8L, false, 0L), EnrollmentStreams.passFailCounts(DATA, 0));
    }

    @Test void coursesByStudentDistinctSorted() {
        Map<String, List<String>> m = EnrollmentStreams.coursesByStudent(DATA);
        assertEquals(List.of("ann", "bob", "cy", "dee"), List.copyOf(m.keySet()));
        assertEquals(List.of("art", "math"), m.get("ann"));
        assertEquals(List.of("cs"), m.get("dee"));
        assertThrows(UnsupportedOperationException.class, () -> m.get("ann").add("x"));
    }

    @Test void bucketsAscendingAndHundredInTopBucket() {
        Map<Integer, Long> m = EnrollmentStreams.gradeBuckets(DATA);
        assertEquals(Map.of(50, 2L, 60, 1L, 70, 1L, 90, 4L), m);
        assertEquals(List.of(50, 60, 70, 90), List.copyOf(m.keySet()));
        assertEquals(Map.of(0, 1L), EnrollmentStreams.gradeBuckets(List.of(e("a", "b", 0))));
    }

    @Test void randomBucketsVsLoop() {
        Random r = new Random(9);
        List<Enrollment> list = new ArrayList<>();
        for (int i = 0; i < 500; i++) list.add(e("s" + r.nextInt(20), "c" + r.nextInt(5), r.nextInt(101)));
        Map<Integer, Long> expect = new TreeMap<>();
        for (Enrollment x : list) expect.merge(Math.min(x.grade() / 10 * 10, 90), 1L, Long::sum);
        assertEquals(expect, EnrollmentStreams.gradeBuckets(list));
        Map<String, Double> avg = new TreeMap<>();
        Map<String, List<Integer>> g = list.stream().collect(Collectors.groupingBy(Enrollment::course, Collectors.mapping(Enrollment::grade, Collectors.toList())));
        g.forEach((k, v) -> avg.put(k, v.stream().mapToInt(Integer::intValue).average().orElse(0)));
        assertEquals(avg, EnrollmentStreams.averageByCourse(list));
    }
}
