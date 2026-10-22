package practice.d20261013.h2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookingTest {

    static final LocalDate D = LocalDate.of(2026, 10, 13);

    @Test @DisplayName("class: getters return what was passed in")
    void basics() {
        Booking b = new Booking("b1", List.of("ann", "bob"), D);
        assertEquals("b1", b.getId());
        assertEquals(List.of("ann", "bob"), b.getGuests());
        assertEquals(D, b.getCheckIn());
    }

    @Test @DisplayName("class: changing the caller's list afterwards does NOT change the booking (break #1)")
    void defensiveCopy() {
        List<String> guests = new ArrayList<>(List.of("ann"));
        Booking b = new Booking("b1", guests, D);
        guests.add("eve");
        assertEquals(List.of("ann"), b.getGuests());
    }

    @Test @DisplayName("class: the list returned by the getter cannot be modified (break #2)")
    void unmodifiableGetter() {
        Booking b = new Booking("b1", new ArrayList<>(List.of("ann")), D);
        assertThrows(UnsupportedOperationException.class, () -> b.getGuests().add("eve"));
        assertThrows(UnsupportedOperationException.class, () -> b.getGuests().clear());
    }

    @Test @DisplayName("class: null id / null date / null guest list are rejected with NullPointerException")
    void nulls() {
        assertThrows(NullPointerException.class, () -> new Booking(null, List.of(), D));
        assertThrows(NullPointerException.class, () -> new Booking("b", List.of(), null));
        assertThrows(NullPointerException.class, () -> new Booking("b", null, D));
    }

    @Test @DisplayName("class: List.copyOf also rejects null elements")
    void nullElement() {
        List<String> withNull = new ArrayList<>(); withNull.add(null);
        assertThrows(NullPointerException.class, () -> new Booking("b", withNull, D));
    }

    // ---------- record style ----------

    @Test @DisplayName("record: copies the list, exposes an unmodifiable one")
    void recordCopy() {
        List<String> guests = new ArrayList<>(List.of("ann"));
        BookingRecord b = new BookingRecord("b1", guests, D);
        guests.add("eve");
        assertEquals(List.of("ann"), b.guests());
        assertThrows(UnsupportedOperationException.class, () -> b.guests().add("x"));
        assertEquals("b1", b.id());
        assertEquals(D, b.checkIn());
    }

    @Test @DisplayName("record: nulls rejected")
    void recordNulls() {
        assertThrows(NullPointerException.class, () -> new BookingRecord(null, List.of(), D));
        assertThrows(NullPointerException.class, () -> new BookingRecord("b", List.of(), null));
        assertThrows(NullPointerException.class, () -> new BookingRecord("b", null, D));
    }
}
