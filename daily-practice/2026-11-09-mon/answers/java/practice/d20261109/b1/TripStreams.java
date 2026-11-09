package practice.d20261109.b1;

import static java.util.stream.Collectors.*;

import java.util.*;

public final class TripStreams {
    private TripStreams() {}

    public static Map<String, Long> totalFareByCity(List<Trip> trips) {
        return trips.stream().collect(groupingBy(Trip::city, TreeMap::new, summingLong(Trip::fareCents)));
    }

    /** Inner collector: per-driver totals, then max by total, ties to the alphabetically first driver. */
    public static Map<String, String> topDriverByCity(List<Trip> trips) {
        return trips.stream().collect(groupingBy(Trip::city, TreeMap::new,
                collectingAndThen(groupingBy(Trip::driver, summingLong(Trip::fareCents)),
                        m -> m.entrySet().stream()
                                .max(Map.Entry.<String, Long>comparingByValue().thenComparing(Map.Entry.comparingByKey(Comparator.reverseOrder())))
                                .orElseThrow().getKey())));
    }

    /** rating (descending keys) -> distinct drivers, alphabetical. */
    public static Map<Integer, List<String>> driversByRating(List<Trip> trips) {
        return trips.stream().collect(groupingBy(Trip::rating, () -> new TreeMap<>(Comparator.reverseOrder()),
                collectingAndThen(mapping(Trip::driver, toCollection(TreeSet::new)), ArrayList::new)));
    }

    public static Map<Boolean, Long> longTripSplit(List<Trip> trips, int longKm) {
        return trips.stream().collect(partitioningBy(t -> t.km() >= longKm, counting()));
    }

    /** takeWhile: ids of the leading run of trips rated >= 4. */
    public static List<String> highlyRatedPrefix(List<Trip> trips) {
        return trips.stream().takeWhile(t -> t.rating() >= 4).map(Trip::id).toList();
    }

    /** dropWhile: everything after the leading run of trips shorter than minKm. */
    public static List<Trip> skipShortStart(List<Trip> trips, int minKm) {
        return trips.stream().dropWhile(t -> t.km() < minKm).toList();
    }

    /** "[Ann, Bob]" - distinct drivers, sorted. */
    public static String driverSummary(List<Trip> trips) {
        return trips.stream().map(Trip::driver).distinct().sorted().collect(joining(", ", "[", "]"));
    }
}
