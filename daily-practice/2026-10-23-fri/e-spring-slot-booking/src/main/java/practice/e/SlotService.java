package practice.e;

import java.time.Instant;
import java.util.List;

// TODO: make this a Spring service. Writes are transactional; the read is read-only.
public class SlotService {
    private final SlotRepository repo;

    public SlotService(SlotRepository repo) { this.repo = repo; }

    /** Save a new slot and return its view (remaining == capacity). */
    public SlotView create(CreateSlotRequest request) { throw new UnsupportedOperationException("TODO"); }

    /** Available slots of a station from the given instant, earliest first. */
    public List<SlotView> available(String stationId, Instant from) { throw new UnsupportedOperationException("TODO"); }

    /**
     * Reserve one place: repo.reserve(id). If it changed 0 rows, decide between SlotNotFoundException (no such id) and
     * SlotFullException. Return the fresh view. Do NOT read first and then write.
     */
    public SlotView reserve(long id) { throw new UnsupportedOperationException("TODO"); }
}
