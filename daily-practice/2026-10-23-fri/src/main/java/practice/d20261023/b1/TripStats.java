package practice.d20261023.b1;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/** Use streams and collectors. "Completed" means not cancelled. Returned maps iterate in key order unless stated. */
public final class TripStats {
    private TripStats() {}

    /** city -> sum of fareCents over completed trips (cities with only cancelled trips are absent). */
    public static Map<String, Long> revenueByCity(List<Trip> trips) { throw new UnsupportedOperationException("TODO"); }

    /** driver -> average km over completed trips. */
    public static Map<String, Double> avgKmByDriver(List<Trip> trips) { throw new UnsupportedOperationException("TODO"); }

    /** city -> distinct driver names, sorted, over ALL trips (cancelled ones included). */
    public static Map<String, List<String>> driversByCity(List<Trip> trips) { throw new UnsupportedOperationException("TODO"); }

    /** Driver with the highest completed revenue; a tie goes to the alphabetically smaller name; empty when no completed trips. */
    public static Optional<String> topDriverByRevenue(List<Trip> trips) { throw new UnsupportedOperationException("TODO"); }

    /** {true -> number of cancelled, false -> number completed}; BOTH keys always present, even for an empty list. */
    public static Map<Boolean, Long> cancelledSplit(List<Trip> trips) { throw new UnsupportedOperationException("TODO"); }

    /** "Mumbai:3,Pune:3,Delhi:1" - completed trip count per city, count descending then city ascending; "" when none. */
    public static String cityReport(List<Trip> trips) { throw new UnsupportedOperationException("TODO"); }

    /** Min and max fare over completed trips in ONE pass (hint: Collectors.teeing); empty when there are none. */
    public static Optional<FareRange> fareRange(List<Trip> trips) { throw new UnsupportedOperationException("TODO"); }
}
