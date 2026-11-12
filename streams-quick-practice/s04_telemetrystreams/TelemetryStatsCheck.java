package s04_telemetrystreams;

import check.DisplayName;
import check.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static check.Assert.*;

class TelemetryStatsCheck {

    static final Instant T0 = Instant.parse("2026-10-14T10:00:00Z");
    static Reading r(String dev, String metric, double v, int minute, String... tags) {
        return new Reading(dev, metric, v, T0.plusSeconds(60L * minute), Set.of(tags));
    }
    static List<Reading> sample() {
        return List.of(
            r("d1", "temp", 75, 0, "indoor"),
            r("d2", "temp", 90, 3, "outdoor"),
            r("d1", "temp", 80, 1, "indoor"),
            r("d3", "temp", 50, 1, "garage"),
            r("d2", "temp", 95, 4, "outdoor"),
            r("d1", "volt", 230, 10, "grid"));
    }

    @Test @DisplayName("1. latest per device, keys sorted, input order irrelevant")
    void latest() {
        Map<String, Reading> m = TelemetryStats.latestPerDevice(sample());
        assertEquals("[d1, d2, d3]", m.keySet().toString());
        assertEquals(230.0, m.get("d1").value(), 0.0);
        assertEquals(95.0, m.get("d2").value(), 0.0);
        assertEquals(50.0, m.get("d3").value(), 0.0);
        assertTrue(TelemetryStats.latestPerDevice(List.of()).isEmpty());
    }

    @Test @DisplayName("2. average per metric and device, both levels sorted")
    void average() {
        Map<String, Map<String, Double>> m = TelemetryStats.averageByMetricAndDevice(sample());
        assertEquals("{temp={d1=77.5, d2=92.5, d3=50.0}, volt={d1=230.0}}", m.toString());
        assertTrue(TelemetryStats.averageByMetricAndDevice(List.of()).isEmpty());
    }

    @Test @DisplayName("3. distinct tags per metric, sorted")
    void tags() {
        Map<String, Set<String>> m = TelemetryStats.tagsByMetric(sample());
        assertEquals("{temp=[garage, indoor, outdoor], volt=[grid]}", m.toString());
    }

    @Test @DisplayName("4. hot devices: at least two temp readings strictly above 80")
    void hot() {
        assertEquals("[d2]", TelemetryStats.hotDevices(sample()).toString());
        List<Reading> more = new ArrayList<>(sample());
        more.add(r("d1", "temp", 81, 5)); more.add(r("d1", "temp", 82, 6));
        more.add(r("d9", "volt", 500, 6)); more.add(r("d9", "volt", 600, 7));   // not temp
        assertEquals("[d1, d2]", TelemetryStats.hotDevices(more).toString());
        assertEquals("[]", TelemetryStats.hotDevices(List.of()).toString());
    }

    @Test @DisplayName("5. silent since cutoff: starts from the known ids, so d4 (never reported) is included")
    void silent() {
        Set<String> known = new TreeSet<>(List.of("d1", "d2", "d3", "d4"));
        assertEquals("[d2, d3, d4]", TelemetryStats.silentSince(known, sample(), T0.plusSeconds(300)).toString());
        // a reading exactly AT the cutoff does not count
        assertEquals("[d1, d2, d3, d4]", TelemetryStats.silentSince(known, sample(), T0.plusSeconds(600)).toString());
        assertEquals("[]", TelemetryStats.silentSince(new TreeSet<>(), sample(), T0).toString());
        // an unknown device that reported is ignored
        assertEquals("[d1]", TelemetryStats.silentSince(new TreeSet<>(List.of("d1")), List.of(r("zz", "temp", 1, 5)), T0).toString());
    }

    public static void main(String[] args) {
        check.Runner.run(TelemetryStatsCheck.class);
    }
}
