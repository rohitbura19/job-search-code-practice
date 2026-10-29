package practice.d20261029.g;

import java.util.Set;

public interface SeatHoldService {
    /**
     * All-or-nothing: either every seat is held for `userId` until now + ttl, or nothing changes.
     * Empty `seatIds` -> IllegalArgumentException; more than maxSeatsPerHold -> TOO_MANY_SEATS; unknown event -> UNKNOWN_EVENT;
     * unknown seat -> UNKNOWN_SEAT; a seat that is BOOKED or held by an unexpired hold -> SEAT_UNAVAILABLE.
     * A hold that has expired no longer blocks anyone.
     */
    Hold hold(String eventId, Set<String> seatIds, String userId);

    /**
     * Turn an active hold into a booking. Unknown id -> NOT_FOUND; expired -> EXPIRED; another user -> NOT_OWNER.
     * Idempotent: confirming an already confirmed hold again (same user) returns the SAME Booking.
     */
    Booking confirm(String holdId, String userId);

    /** Free the seats of an active hold now. Wrong user -> NOT_OWNER. Unknown, expired, already released or confirmed: no-op. */
    void release(String holdId, String userId);

    /** AVAILABLE, HELD (unexpired hold) or BOOKED. Unknown event/seat -> the matching HoldException. */
    SeatState state(String eventId, String seatId);

    /** Number of seats of the event that are currently AVAILABLE. */
    int availableCount(String eventId);
}
