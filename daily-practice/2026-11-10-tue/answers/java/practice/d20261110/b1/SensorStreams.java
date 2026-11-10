package practice.d20261110.b1;

import static java.util.stream.Collectors.*;

import java.util.*;

/**
 * One pass each. peakPerSite: toMap(merge Math::max, TreeMap::new). averagePerSensor: groupingBy + filtering on the entry set, or collect to a
 * summarizing map first. alwaysAbove: collect the per-sensor minimum with groupingBy(minBy) -- a sensor is "above" iff its min is. topValuesPerSite:
 * collectingAndThen(mapping(value, toList()), sort desc + limit). busiestSite: counting() then max with a (count, name) comparator.
 * activeSensors: filter first, then groupingBy(site, TreeMap, mapping(sensor, toCollection(TreeSet))), finishing to a list.
 */
public class SensorStreams {
    public static Map<String, Integer> peakPerSite(List<Reading> readings) {
        return readings.stream().collect(toMap(Reading::site, Reading::value, Math::max, TreeMap::new));
    }

    public static Map<String, Double> averagePerSensor(List<Reading> readings) {
        return readings.stream()
                .collect(groupingBy(Reading::sensor, TreeMap::new, summarizingInt(Reading::value)))
                .entrySet().stream()
                .filter(e -> e.getValue().getCount() >= 2)
                .collect(toMap(Map.Entry::getKey, e -> e.getValue().getAverage(), (a, b) -> a, TreeMap::new));
    }

    public static List<String> alwaysAbove(List<Reading> readings, int threshold) {
        return readings.stream()
                .collect(groupingBy(Reading::sensor, TreeMap::new, minBy(Comparator.comparingInt(Reading::value))))
                .entrySet().stream()
                .filter(e -> e.getValue().get().value() > threshold)
                .map(Map.Entry::getKey)
                .toList();
    }

    public static Map<String, List<Integer>> topValuesPerSite(List<Reading> readings, int n) {
        return readings.stream().collect(groupingBy(Reading::site, TreeMap::new,
                collectingAndThen(mapping(Reading::value, toList()),
                        l -> l.stream().sorted(Comparator.reverseOrder()).limit(n).toList())));
    }

    public static Optional<String> busiestSite(List<Reading> readings) {
        return readings.stream().collect(groupingBy(Reading::site, counting()))
                .entrySet().stream()
                .max(Map.Entry.<String, Long>comparingByValue().thenComparing(Map.Entry.comparingByKey(Comparator.reverseOrder())))
                .map(Map.Entry::getKey);
    }

    public static Map<String, List<String>> activeSensors(List<Reading> readings, int from, int to) {
        return readings.stream().filter(r -> r.minute() >= from && r.minute() < to)
                .collect(groupingBy(Reading::site, TreeMap::new,
                        collectingAndThen(mapping(Reading::sensor, toCollection(TreeSet::new)), List::copyOf)));
    }
}
