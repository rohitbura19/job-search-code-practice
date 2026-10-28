package practice.d20261028.b1;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.*;
import org.junit.jupiter.api.Test;

class LoanStatsTest {
    static LocalDate d(int day) { return LocalDate.of(2026, 10, day); }
    static Loan l(String m, String b, String g, int from, Integer to) { return new Loan(m, b, g, d(from), to == null ? null : d(to)); }

    static final List<Loan> LOANS = List.of(
            l("asha", "Dune", "scifi", 1, 8),
            l("ben", "Dune", "scifi", 2, 20),
            l("asha", "Emma", "classic", 3, 4),
            l("cy", "Ubik", "scifi", 5, null),
            l("ben", "Emma", "classic", 6, null),
            l("cy", "Dune", "scifi", 7, 9),
            l("ben", "Ubik", "scifi", 10, null));

    @Test void genres() {
        Map<String, Set<String>> m = LoanStats.genresByMember(LOANS);
        assertEquals(List.of("asha", "ben", "cy"), new ArrayList<>(m.keySet()));
        assertEquals(List.of("classic", "scifi"), new ArrayList<>(m.get("asha")));
        assertEquals(List.of("scifi"), new ArrayList<>(m.get("cy")));
        assertTrue(m instanceof SortedMap);
        assertTrue(LoanStats.genresByMember(List.of()).isEmpty());
    }

    @Test void leading() {
        assertEquals(LOANS.subList(0, 3), LoanStats.leadingReturned(LOANS));
        assertEquals(List.of(), LoanStats.leadingReturned(LOANS.subList(3, 5)));
        List<Loan> allDone = List.of(LOANS.get(0), LOANS.get(1));
        assertEquals(allDone, LoanStats.leadingReturned(allDone));
        assertEquals(List.of(), LoanStats.leadingReturned(List.of()));
    }

    @Test void overdue() {
        // allowed 5 days, today = Oct 20
        Map<String, Long> m = LoanStats.overdueDays(LOANS, d(20), 5);
        // asha: Dune due 6, returned 8 -> 2; Emma due 8, returned 4 -> 0 => 2
        // ben: Dune due 7, returned 20 -> 13; Emma due 11, open -> 9; Ubik due 15, open -> 5 => 27
        // cy: Ubik due 10, open -> 10; Dune due 12, returned 9 -> 0 => 10
        assertEquals(List.of("ben", "cy", "asha"), new ArrayList<>(m.keySet()));
        assertEquals(List.of(27L, 10L, 2L), new ArrayList<>(m.values()));
        assertEquals(Map.of(), LoanStats.overdueDays(LOANS, d(1), 100));
    }

    @Test void overdueTiesBrokenByMember() {
        List<Loan> t = List.of(l("zed", "A", "g", 1, 5), l("amy", "B", "g", 1, 5));
        assertEquals(List.of("amy", "zed"), new ArrayList<>(LoanStats.overdueDays(t, d(30), 2).keySet()));
    }

    @Test void mostBorrowedAndTies() {
        assertEquals(Optional.of("Dune"), LoanStats.mostBorrowed(LOANS));
        List<Loan> tie = List.of(l("a", "Zorro", "g", 1, 2), l("a", "Alpha", "g", 1, 2));
        assertEquals(Optional.of("Alpha"), LoanStats.mostBorrowed(tie));
        assertEquals(Optional.empty(), LoanStats.mostBorrowed(List.of()));
    }

    @Test void topBooks() {
        Map<String, List<String>> m = LoanStats.topBooksPerGenre(LOANS, 2);
        assertEquals(List.of("classic", "scifi"), new ArrayList<>(m.keySet()));
        assertEquals(List.of("Emma"), m.get("classic"));
        assertEquals(List.of("Dune", "Ubik"), m.get("scifi"));
        assertEquals(List.of("Dune"), LoanStats.topBooksPerGenre(LOANS, 1).get("scifi"));
        assertEquals(List.of(), LoanStats.topBooksPerGenre(LOANS, 0).get("scifi"));
    }

    @Test void holders() {
        assertEquals("[ben, cy]", LoanStats.holdersCsv(LOANS));
        assertEquals("[]", LoanStats.holdersCsv(LOANS.subList(0, 3)));
        assertEquals("[]", LoanStats.holdersCsv(List.of()));
    }

    @Test void inputNotModified() {
        List<Loan> copy = new ArrayList<>(LOANS);
        LoanStats.overdueDays(copy, d(20), 5);
        LoanStats.topBooksPerGenre(copy, 2);
        assertEquals(LOANS, copy);
    }
}
