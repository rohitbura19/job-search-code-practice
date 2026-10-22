package practice.d20261013.h2;

import java.time.LocalDate;
import java.util.List;

/** H2 (record style). Same rules as Booking; use a COMPACT constructor to validate and copy. */
public record BookingRecord(String id, List<String> guests, LocalDate checkIn) {
    // TODO: compact constructor
}
