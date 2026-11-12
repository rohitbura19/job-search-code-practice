package s09_bookings;

import java.time.LocalDate;

public record Booking(String id, String city, String guest, int nights, long amountPaise, LocalDate checkIn) {}
