package practice.d20261025.b1;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class BookingStats {
    private BookingStats() {}

    public record CountRevenue(long count, long revenuePaise) {}

    /** City -> total amount, as a LinkedHashMap ordered by revenue descending, ties by city ascending. */
    public static Map<String, Long> revenueByCity(List<Booking> bookings) { throw new UnsupportedOperationException("TODO"); }

    /** City -> guest with the most nights in that city (nights summed over their bookings); ties -> alphabetically smaller guest. TreeMap by city. */
    public static Map<String, String> topGuestPerCity(List<Booking> bookings) { throw new UnsupportedOperationException("TODO"); }

    /** Check-in month -> average nights per booking, TreeMap ordered by month. */
    public static Map<YearMonth, Double> avgNightsByMonth(List<Booking> bookings) { throw new UnsupportedOperationException("TODO"); }

    /** true -> ids of bookings with nights >= 7, false -> the rest. Both keys always present; ids keep the input order. */
    public static Map<Boolean, List<String>> splitLongStays(List<Booking> bookings) { throw new UnsupportedOperationException("TODO"); }

    /** Number of bookings and total revenue in ONE pass over the stream (Collectors.teeing). */
    public static CountRevenue countAndRevenue(List<Booking> bookings) { throw new UnsupportedOperationException("TODO"); }

    /** The distinct guests (case-insensitive, keep the first spelling seen) sorted case-insensitively and joined with ", ". Empty list -> "". */
    public static String guestRoll(List<Booking> bookings) { throw new UnsupportedOperationException("TODO"); }

    /** The booking with the highest amount; ties -> the earliest check-in, then the smaller id. Empty -> Optional.empty(). */
    public static Optional<Booking> biggest(List<Booking> bookings) { throw new UnsupportedOperationException("TODO"); }
}
