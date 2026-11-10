package practice.d20261110.b1;

import java.util.*;

/** Streams / collectors only: no explicit loops, no mutable accumulators outside the collectors. Maps with sorted keys are TreeMaps unless said otherwise. */
public class SensorStreams {
    /** Highest value per site, TreeMap ordered by site. */
    public static Map<String, Integer> peakPerSite(List<Reading> readings) { throw new UnsupportedOperationException("TODO"); }

    /** Average value per sensor, but only for sensors with at least 2 readings (TreeMap by sensor). */
    public static Map<String, Double> averagePerSensor(List<Reading> readings) { throw new UnsupportedOperationException("TODO"); }

    /** Sensors whose EVERY reading is strictly above the threshold, sorted alphabetically. */
    public static List<String> alwaysAbove(List<Reading> readings, int threshold) { throw new UnsupportedOperationException("TODO"); }

    /** For each site the n highest values (duplicates kept), descending (TreeMap by site). n >= 0; n larger than the count returns all. */
    public static Map<String, List<Integer>> topValuesPerSite(List<Reading> readings, int n) { throw new UnsupportedOperationException("TODO"); }

    /** Site with the most readings; ties go to the alphabetically smaller site; empty input -> Optional.empty(). */
    public static Optional<String> busiestSite(List<Reading> readings) { throw new UnsupportedOperationException("TODO"); }

    /** Per site: distinct sensors (sorted) that reported at least one reading in the half-open minute window [from, to). Sites with none are omitted. TreeMap by site. */
    public static Map<String, List<String>> activeSensors(List<Reading> readings, int from, int to) { throw new UnsupportedOperationException("TODO"); }
}
