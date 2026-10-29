package practice.d20261029.g;

import java.util.Set;

public record Booking(String bookingId, String holdId, String eventId, Set<String> seats, String userId) {}
