package practice.d20261025.b1;

import java.time.LocalDate;

public record Booking(String id, String city, String guest, int nights, long amountPaise, LocalDate checkIn) {}
