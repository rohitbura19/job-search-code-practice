package practice.d20261011.b1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SessionStatsTest {

    static Session s(String charger, String site, double kwh) { return new Session(charger, site, kwh, Instant.parse("2026-10-11T10:00:00Z")); }

    static List<Session> sample() {
        return List.of(
            s("c1", "zeta", 10), s("c1", "zeta", 5),      // c1 total 15
            s("c2", "alpha", 20),                           // c2 total 20
            s("c3", "alpha", 4), s("c3", "alpha", 4),       // c3 total 8
            s("c4", "zeta", 12),                            // c4 total 12
            s("c5", "mid", 1));                             // c5 total 1
    }

    @Test @DisplayName("top 3 chargers by total kWh, highest first")
    void top3() {
        assertEquals(List.of("c2", "c1", "c4"), SessionStats.top3Chargers(sample()));
    }

    @Test @DisplayName("top 3 with fewer than 3 chargers, and empty")
    void top3Small() {
        assertEquals(List.of("c1"), SessionStats.top3Chargers(List.of(s("c1", "a", 1), s("c1", "a", 2))));
        assertEquals(List.of(), SessionStats.top3Chargers(List.of()));
    }

    @Test @DisplayName("totals are summed per charger, not per session")
    void totalsNotMax() {
        List<Session> l = List.of(s("big", "a", 9), s("many", "a", 4), s("many", "a", 4), s("many", "a", 4));
        assertEquals(List.of("many", "big"), SessionStats.top3Chargers(l));
    }

    @Test @DisplayName("average kWh per site, sorted by site name")
    void averages() {
        Map<String, Double> m = SessionStats.avgKwhBySite(sample());
        assertEquals(List.of("alpha", "mid", "zeta"), new ArrayList<>(m.keySet()));
        assertEquals(28.0 / 3, m.get("alpha"), 1e-9);   // sessions 20, 4, 4
        assertEquals(1.0, m.get("mid"), 1e-9);
        assertEquals(27.0 / 3, m.get("zeta"), 1e-9);    // sessions 10, 5, 12
    }

    @Test @DisplayName("site with the most distinct chargers (repeat sessions of one charger count once)")
    void mostChargers() {
        assertEquals("zeta", SessionStats.siteWithMostChargers(List.of(
            s("a1", "alpha", 1), s("a1", "alpha", 1), s("a1", "alpha", 1), s("a2", "alpha", 1),
            s("z1", "zeta", 1), s("z2", "zeta", 1), s("z3", "zeta", 1))));
        assertEquals("only", SessionStats.siteWithMostChargers(List.of(s("x", "only", 1))));
        assertNull(SessionStats.siteWithMostChargers(List.of()));
    }

    @Test @DisplayName("empty input gives an empty map")
    void emptyAverages() {
        assertTrue(SessionStats.avgKwhBySite(List.of()).isEmpty());
    }
}
