package practice.d20261013.h2;

import java.time.LocalDate;
import java.util.List;

/**
 * H2 (classic class style). Make it REALLY immutable: defensive copy of the list on the way in (List.copyOf), non-null id and checkIn
 * (Objects.requireNonNull), java.time instead of Date. Getters: getId(), getGuests(), getCheckIn().
 */
public final class Booking {
    public Booking(String id, List<String> guests, LocalDate checkIn) {
        throw new UnsupportedOperationException("TODO");
    }

    public String getId() { throw new UnsupportedOperationException("TODO"); }
    public List<String> getGuests() { throw new UnsupportedOperationException("TODO"); }
    public LocalDate getCheckIn() { throw new UnsupportedOperationException("TODO"); }
}
