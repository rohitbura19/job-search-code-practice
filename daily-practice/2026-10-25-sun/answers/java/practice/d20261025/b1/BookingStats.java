package practice.d20261025.b1;

import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

/** Each method is one pipeline; O(n) or O(n log n) for the sorted ones. Key points are in the comments. */
public final class BookingStats {
    private BookingStats() {}

    public record CountRevenue(long count, long revenuePaise) {}

    public static Map<String, Long> revenueByCity(List<Booking> bookings) {
        return bookings.stream().collect(Collectors.groupingBy(Booking::city, Collectors.summingLong(Booking::amountPaise)))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed().thenComparing(Map.Entry.comparingByKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (x, y) -> x, LinkedHashMap::new));
    }

    public static Map<String, String> topGuestPerCity(List<Booking> bookings) {
        Map<String, Map<String, Integer>> nights = bookings.stream().collect(Collectors.groupingBy(Booking::city, TreeMap::new,
                Collectors.groupingBy(Booking::guest, Collectors.summingInt(Booking::nights))));
        Map<String, String> out = new TreeMap<>();
        nights.forEach((city, byGuest) -> out.put(city, byGuest.entrySet().stream()
                .max(Map.Entry.<String, Integer>comparingByValue().thenComparing(Map.Entry.<String, Integer>comparingByKey().reversed()))
                .orElseThrow().getKey()));
        return out;
    }

    public static Map<YearMonth, Double> avgNightsByMonth(List<Booking> bookings) {
        return bookings.stream().collect(Collectors.groupingBy(b -> YearMonth.from(b.checkIn()), TreeMap::new,
                Collectors.averagingInt(Booking::nights)));
    }

    public static Map<Boolean, List<String>> splitLongStays(List<Booking> bookings) {
        // partitioningBy always has both keys, unlike groupingBy.
        return bookings.stream().collect(Collectors.partitioningBy(b -> b.nights() >= 7,
                Collectors.mapping(Booking::id, Collectors.toList())));
    }

    public static CountRevenue countAndRevenue(List<Booking> bookings) {
        return bookings.stream().collect(Collectors.teeing(Collectors.counting(), Collectors.summingLong(Booking::amountPaise), CountRevenue::new));
    }

    public static String guestRoll(List<Booking> bookings) {
        Map<String, String> firstSpelling = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        bookings.forEach(b -> firstSpelling.putIfAbsent(b.guest(), b.guest()));
        return String.join(", ", firstSpelling.values());   // TreeMap iterates in case-insensitive key order
    }

    public static Optional<Booking> biggest(List<Booking> bookings) {
        return bookings.stream().max(Comparator.comparingLong(Booking::amountPaise)
                .thenComparing(Comparator.comparing(Booking::checkIn).reversed())
                .thenComparing(Comparator.comparing(Booking::id).reversed()));
    }
}
