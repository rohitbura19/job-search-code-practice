package practice.d20261109.b1;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import org.junit.jupiter.api.Test;

class TripStreamsTest {
    static final List<Trip> T = List.of(
            new Trip("t1", "Bob", "Pune", 3, 120, 5),
            new Trip("t2", "Ann", "Pune", 12, 400, 4),
            new Trip("t3", "Bob", "Pune", 8, 300, 3),
            new Trip("t4", "Cy", "Delhi", 20, 900, 5),
            new Trip("t5", "Ann", "Delhi", 15, 500, 4),
            new Trip("t6", "Dee", "Delhi", 2, 900, 2));

    @Test void totalsAreSortedByCity() {
        Map<String, Long> m = TripStreams.totalFareByCity(T);
        assertEquals(List.of("Delhi", "Pune"), List.copyOf(m.keySet()));
        assertEquals(2300L, m.get("Delhi"));
        assertEquals(820L, m.get("Pune"));
        assertTrue(TripStreams.totalFareByCity(List.of()).isEmpty());
    }

    @Test void topDriverWithTieBreak() {
        Map<String, String> m = TripStreams.topDriverByCity(T);
        assertEquals("Cy", m.get("Delhi"));   // Cy 900 ties Dee 900 -> alphabetical; Ann 500
        assertEquals("Bob", m.get("Pune"));   // Bob 420 vs Ann 400
        assertEquals(List.of("Delhi", "Pune"), List.copyOf(m.keySet()));
    }

    @Test void driversByRatingDescendingKeys() {
        Map<Integer, List<String>> m = TripStreams.driversByRating(T);
        assertEquals(List.of(5, 4, 3, 2), List.copyOf(m.keySet()));
        assertEquals(List.of("Bob", "Cy"), m.get(5));
        assertEquals(List.of("Ann"), m.get(4));
    }

    @Test void partitionHasBothKeys() {
        assertEquals(Map.of(true, 3L, false, 3L), TripStreams.longTripSplit(T, 12));
        Map<Boolean, Long> none = TripStreams.longTripSplit(List.of(), 5);
        assertEquals(0L, none.get(true));
        assertEquals(0L, none.get(false));
    }

    @Test void takeWhileAndDropWhile() {
        assertEquals(List.of("t1", "t2"), TripStreams.highlyRatedPrefix(T));
        assertTrue(TripStreams.highlyRatedPrefix(List.of(T.get(2), T.get(0))).isEmpty());
        assertEquals(List.of("t2", "t3", "t4", "t5", "t6"), TripStreams.skipShortStart(T, 10).stream().map(Trip::id).toList());
        assertEquals(List.of("t4", "t5", "t6"), TripStreams.skipShortStart(T, 13).stream().map(Trip::id).toList());
        assertEquals(6, TripStreams.skipShortStart(T, 1).size());
    }

    @Test void summary() {
        assertEquals("[Ann, Bob, Cy, Dee]", TripStreams.driverSummary(T));
        assertEquals("[]", TripStreams.driverSummary(List.of()));
    }
}
