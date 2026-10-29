package practice.d20261029.g;

import java.time.Clock;
import java.util.Map;
import java.util.Set;

/** Implement SeatHoldService. Thread-safe: different events should not block each other. Time comes ONLY from the Clock. */
public class InMemorySeatHoldService implements SeatHoldService {
    /** seatsByEvent: event id -> its seat ids (all start AVAILABLE). Hold ids "H1", "H2", ..., booking ids "B1", "B2", ... */
    public InMemorySeatHoldService(Clock clock, HoldConfig config, Map<String, Set<String>> seatsByEvent) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override public Hold hold(String eventId, Set<String> seatIds, String userId) { throw new UnsupportedOperationException("TODO"); }
    @Override public Booking confirm(String holdId, String userId) { throw new UnsupportedOperationException("TODO"); }
    @Override public void release(String holdId, String userId) { throw new UnsupportedOperationException("TODO"); }
    @Override public SeatState state(String eventId, String seatId) { throw new UnsupportedOperationException("TODO"); }
    @Override public int availableCount(String eventId) { throw new UnsupportedOperationException("TODO"); }
}
