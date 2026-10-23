package practice.d20261023.b1;

import static java.util.stream.Collectors.*;

import java.util.*;
import java.util.stream.Stream;

/**
 * groupingBy with a TreeMap factory gives sorted keys; the downstream collector does the per-group work (summingLong, averagingInt,
 * mapping + a sorted set). partitioningBy always has both keys. teeing runs two collectors in one pass and merges them.
 * Everything is O(n) except the sorts (O(n log n)).
 */
public final class TripStats {
    private TripStats() {}

    private static Stream<Trip> done(List<Trip> t) { return t.stream().filter(x -> !x.cancelled()); }

    public static Map<String, Long> revenueByCity(List<Trip> trips) {
        return done(trips).collect(groupingBy(Trip::city, TreeMap::new, summingLong(Trip::fareCents)));
    }

    public static Map<String, Double> avgKmByDriver(List<Trip> trips) {
        return done(trips).collect(groupingBy(Trip::driver, TreeMap::new, averagingInt(Trip::km)));
    }

    public static Map<String, List<String>> driversByCity(List<Trip> trips) {
        return trips.stream().collect(groupingBy(Trip::city, TreeMap::new,
                collectingAndThen(mapping(Trip::driver, toCollection(TreeSet::new)), s -> List.copyOf(s))));
    }

    public static Optional<String> topDriverByRevenue(List<Trip> trips) {
        Map<String, Long> rev = done(trips).collect(groupingBy(Trip::driver, summingLong(Trip::fareCents)));
        return rev.entrySet().stream()
                .max(Map.Entry.<String, Long>comparingByValue().thenComparing(Map.Entry.comparingByKey(Comparator.reverseOrder())))
                .map(Map.Entry::getKey);
    }

    public static Map<Boolean, Long> cancelledSplit(List<Trip> trips) {
        return trips.stream().collect(partitioningBy(Trip::cancelled, counting()));
    }

    public static String cityReport(List<Trip> trips) {
        return done(trips).collect(groupingBy(Trip::city, counting())).entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed().thenComparing(Map.Entry.comparingByKey()))
                .map(e -> e.getKey() + ":" + e.getValue())
                .collect(joining(","));
    }

    public static Optional<FareRange> fareRange(List<Trip> trips) {
        return done(trips).collect(teeing(
                minBy(Comparator.comparingLong(Trip::fareCents)),
                maxBy(Comparator.comparingLong(Trip::fareCents)),
                (lo, hi) -> lo.isPresent() ? Optional.of(new FareRange(lo.get().fareCents(), hi.get().fareCents())) : Optional.empty()));
    }
}
