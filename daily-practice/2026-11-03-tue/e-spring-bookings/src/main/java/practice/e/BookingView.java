package practice.e;

import java.time.Instant;

/** Given. */
public record BookingView(Long id, String room, Instant startAt, Instant endAt, String bookedBy) {
    static BookingView from(Booking b) { return new BookingView(b.getId(), b.getRoom(), b.getStartAt(), b.getEndAt(), b.getBookedBy()); }
}
