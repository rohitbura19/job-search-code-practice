package s04_telemetrystreams;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** B1 - one stream pipeline per method (see questions/B1). */
public final class TelemetryStats {
    private TelemetryStats() {}

    /** 1. Latest reading per device (by ts), keys sorted (TreeMap). */
    public static Map<String, Reading> latestPerDevice(List<Reading> readings) { throw new UnsupportedOperationException("TODO"); }

    /** 2. metric -> deviceId -> average value; both levels sorted. */
    public static Map<String, Map<String, Double>> averageByMetricAndDevice(List<Reading> readings) { throw new UnsupportedOperationException("TODO"); }

    /** 3. metric -> distinct tags seen for that metric; both levels sorted; one pipeline. */
    public static Map<String, Set<String>> tagsByMetric(List<Reading> readings) { throw new UnsupportedOperationException("TODO"); }

    /** 4. Device ids (sorted) with at least 2 "temp" readings with value strictly above 80. */
    public static List<String> hotDevices(List<Reading> readings) { throw new UnsupportedOperationException("TODO"); }

    /** 5. Known devices (in the iteration order of known) with no reading strictly after cutoff. */
    public static List<String> silentSince(Set<String> known, List<Reading> readings, Instant cutoff) { throw new UnsupportedOperationException("TODO"); }
}
