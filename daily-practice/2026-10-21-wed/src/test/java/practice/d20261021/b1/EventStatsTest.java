package practice.d20261021.b1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

class EventStatsTest {

    static final List<Ev> EVS = List.of(
            new Ev("e1", "siteA", "CHARGE", 12, "ok"),
            new Ev("e2", "siteB", "CHARGE", 8, null),
            new Ev("e3", "siteA", "EXPORT", 5, "grid"),
            new Ev("e4", "siteC", "CHARGE", 20, null),
            new Ev("e5", "siteB", "EXPORT", 3, "peak"),
            new Ev("e6", "siteA", "CHARGE", 7, "ok"));

    @Test @DisplayName("1. statistics per site, sorted by site")
    void statsBySite() {
        TreeMap<String, IntSummaryStatistics> st = EventStats.statsBySite(EVS);
        assertEquals(List.of("siteA", "siteB", "siteC"), List.copyOf(st.keySet()));
        IntSummaryStatistics a = st.get("siteA");
        assertEquals(3, a.getCount()); assertEquals(24, a.getSum()); assertEquals(5, a.getMin()); assertEquals(12, a.getMax());
        assertEquals(8.0, a.getAverage(), 1e-9);
        assertEquals(5.5, st.get("siteB").getAverage(), 1e-9);
    }

    @Test @DisplayName("1b. empty input gives an empty map")
    void statsEmpty() { assertTrue(EventStats.statsBySite(List.of()).isEmpty()); }

    @Test @DisplayName("2. average per site and total per type")
    void averagesAndTotals() {
        assertEquals(new TreeMap<>(Map.of("siteA", 8.0, "siteB", 5.5, "siteC", 20.0)), EventStats.averageBySite(EVS));
        assertEquals(new TreeMap<>(Map.of("CHARGE", 47, "EXPORT", 8)), EventStats.totalByType(EVS));
    }

    @Test @DisplayName("3. sort by note (nulls last), then kwh descending")
    void sortWithNulls() {
        assertEquals(List.of("e3", "e1", "e6", "e5", "e4", "e2"), EventStats.idsByNoteThenKwhDesc(EVS));
    }

    @Test @DisplayName("3b. all notes null: pure kwh descending")
    void allNullNotes() {
        List<Ev> evs = List.of(new Ev("x", "s", "T", 1, null), new Ev("y", "s", "T", 9, null));
        assertEquals(List.of("y", "x"), EventStats.idsByNoteThenKwhDesc(evs));
    }

    @Test @DisplayName("4. powers of 3 below a limit")
    void powers() {
        assertEquals(List.of(1, 3, 9, 27, 81), EventStats.powersOf3Below(200));
        assertEquals(List.of(), EventStats.powersOf3Below(1));
        assertEquals(List.of(1), EventStats.powersOf3Below(2));
    }

    @Test @DisplayName("5. flatten nested lists and sum")
    void flatten() {
        assertEquals(6, EventStats.flattenSum(List.of(List.of(1, 2), List.of(), List.of(3))));
        assertEquals(0, EventStats.flattenSum(List.of()));
    }
}
