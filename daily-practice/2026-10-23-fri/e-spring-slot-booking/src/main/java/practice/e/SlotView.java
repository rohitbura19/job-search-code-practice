package practice.e;

import java.time.Instant;

public record SlotView(Long id, String stationId, Instant startsAt, int capacity, int remaining) {
    static SlotView of(Slot s) { return new SlotView(s.getId(), s.getStationId(), s.getStartsAt(), s.getCapacity(), s.getCapacity() - s.getBooked()); }
}
