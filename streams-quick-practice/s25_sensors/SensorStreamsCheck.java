package s25_sensors;

import static check.Assert.*;

import java.time.Duration;
import java.util.*;
import check.Test;

class SensorStreamsCheck {
    static final List<Reading> R = List.of(
            new Reading("t1", "north", 0, 20), new Reading("t1", "north", 5, 30), new Reading("t2", "north", 5, 30),
            new Reading("t3", "south", 1, 10), new Reading("t4", "south", 2, 15), new Reading("t4", "south", 9, 25),
            new Reading("t5", "east", 3, 40));

    @Test void peak() {
        assertEquals(Map.of("east", 40, "north", 30, "south", 25), SensorStreams.peakPerSite(R));
        assertEquals(List.of("east", "north", "south"), new ArrayList<>(SensorStreams.peakPerSite(R).keySet()));
        assertTrue(SensorStreams.peakPerSite(List.of()).isEmpty());
    }

    @Test void averageNeedsTwoReadings() {
        Map<String, Double> m = SensorStreams.averagePerSensor(R);
        assertEquals(List.of("t1", "t4"), new ArrayList<>(m.keySet()));
        assertEquals(25.0, m.get("t1"), 1e-9);
        assertEquals(20.0, m.get("t4"), 1e-9);
    }

    @Test void alwaysAboveIsStrictAndPerSensor() {
        assertEquals(List.of("t1", "t2", "t5"), SensorStreams.alwaysAbove(R, 19));
        assertEquals(List.of("t2", "t5"), SensorStreams.alwaysAbove(R, 20));
        assertEquals(List.of(), SensorStreams.alwaysAbove(R, 40));
        assertEquals(List.of(), SensorStreams.alwaysAbove(List.of(), 0));
    }

    @Test void topValuesKeepsDuplicates() {
        assertEquals(List.of(30, 30), SensorStreams.topValuesPerSite(R, 2).get("north"));
        assertEquals(List.of(25, 15, 10), SensorStreams.topValuesPerSite(R, 10).get("south"));
        assertEquals(List.of(), SensorStreams.topValuesPerSite(R, 0).get("east"));
        assertEquals(List.of("east", "north", "south"), new ArrayList<>(SensorStreams.topValuesPerSite(R, 1).keySet()));
    }

    @Test void busiest() {
        assertEquals(Optional.of("north"), SensorStreams.busiestSite(R));
        assertEquals(Optional.empty(), SensorStreams.busiestSite(List.of()));
        // tie north=1 vs alpha=1 -> alphabetically smaller wins
        assertEquals(Optional.of("alpha"), SensorStreams.busiestSite(List.of(new Reading("a", "north", 0, 1), new Reading("b", "alpha", 0, 1))));
    }

    @Test void activeWindowIsHalfOpen() {
        assertEquals(Map.of("east", List.of("t5"), "north", List.of("t1", "t2"), "south", List.of("t3", "t4")), SensorStreams.activeSensors(R, 1, 9));
        assertEquals(Map.of("east", List.of("t5"), "north", List.of("t1"), "south", List.of("t3", "t4")), SensorStreams.activeSensors(R, 0, 5));
        assertTrue(SensorStreams.activeSensors(R, 10, 20).isEmpty());
        assertEquals(List.of("north"), new ArrayList<>(SensorStreams.activeSensors(R, 0, 1).keySet()));
    }

    @Test void largeInputWithinTimeout() {
        Random r = new Random(5);
        List<Reading> big = new ArrayList<>();
        for (int i = 0; i < 300_000; i++) big.add(new Reading("s" + r.nextInt(5000), "site" + r.nextInt(100), r.nextInt(1440), r.nextInt(1000)));
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> {
            SensorStreams.peakPerSite(big);
            SensorStreams.averagePerSensor(big);
            SensorStreams.alwaysAbove(big, 3);
            SensorStreams.topValuesPerSite(big, 5);
            SensorStreams.busiestSite(big);
            SensorStreams.activeSensors(big, 100, 200);
        });
    }

    public static void main(String[] args) {
        check.Runner.run(SensorStreamsCheck.class);
    }
}
