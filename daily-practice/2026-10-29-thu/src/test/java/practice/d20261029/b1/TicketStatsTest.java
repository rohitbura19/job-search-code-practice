package practice.d20261029.b1;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import org.junit.jupiter.api.Test;

class TicketStatsTest {
    static Ticket open(String id, String team, String who, int p) { return new Ticket(id, team, who, p, false, 0); }
    static Ticket done(String id, String team, String who, int p, int h) { return new Ticket(id, team, who, p, true, h); }

    static final List<Ticket> DATA = List.of(
            open("T5", "billing", "asha", 1), open("T1", "billing", "ben", 3), open("T3", "core", "asha", 2),
            open("T2", "core", null, 4), done("T4", "core", "ben", 2, 10), done("T6", "core", "asha", 3, 5),
            done("T7", "ops", "cy", 4, 7), open("T8", "ops", "cy", 3), open("T9", "ops", "cy", 4));

    @Test void openCount() {
        Map<String, Long> m = TicketStats.openCountByTeam(DATA);
        assertEquals(List.of("billing", "core", "ops"), new ArrayList<>(m.keySet()));
        assertEquals(List.of(2L, 2L, 2L), new ArrayList<>(m.values()));
        assertTrue(TicketStats.openCountByTeam(List.of()).isEmpty());
        assertTrue(TicketStats.openCountByTeam(List.of(done("x", "t", "a", 1, 1))).isEmpty());
    }

    @Test void split() {
        Map<Boolean, List<String>> m = TicketStats.splitUrgent(DATA);
        assertEquals(List.of("T3", "T4", "T5"), m.get(true));
        assertEquals(List.of("T1", "T2", "T6", "T7", "T8", "T9"), m.get(false));
        Map<Boolean, List<String>> empty = TicketStats.splitUrgent(List.of());
        assertEquals(List.of(), empty.get(true));
        assertEquals(List.of(), empty.get(false));
    }

    @Test void averages() {
        Map<String, Double> m = TicketStats.avgResolveHoursByTeam(DATA);
        assertEquals(List.of("core", "ops"), new ArrayList<>(m.keySet()));
        assertEquals(7.5, m.get("core"), 1e-9);
        assertEquals(7.0, m.get("ops"), 1e-9);
    }

    @Test void busiestAndTop() {
        assertEquals(Optional.of("asha"), TicketStats.busiestAssignee(DATA));   // asha 2 open, cy 2 open, ben 1: tie -> asha
        assertEquals(Optional.empty(), TicketStats.busiestAssignee(List.of(open("a", "t", null, 1))));
        assertEquals(List.of("asha", "cy", "ben"), TicketStats.topAssignees(DATA, 10));
        assertEquals(List.of("asha"), TicketStats.topAssignees(DATA, 1));
        assertEquals(List.of(), TicketStats.topAssignees(DATA, 0));
        assertEquals(List.of(), TicketStats.topAssignees(DATA, -3));
    }

    @Test void rangeInOnePass() {
        assertEquals(Optional.of(new Range(5, 10)), TicketStats.resolveRange(DATA));
        assertEquals(Optional.empty(), TicketStats.resolveRange(List.of(open("a", "t", "x", 1))));
        assertEquals(Optional.of(new Range(3, 3)), TicketStats.resolveRange(List.of(done("a", "t", "x", 1, 3))));
    }

    @Test void inputIsNotMutated() {
        List<Ticket> copy = new ArrayList<>(DATA);
        TicketStats.splitUrgent(copy); TicketStats.topAssignees(copy, 2); TicketStats.openCountByTeam(copy);
        assertEquals(DATA, copy);
    }
}
