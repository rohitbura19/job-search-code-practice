package practice.e;

import java.time.Instant;
import java.util.List;

// TODO: make this a REST controller under /api and complete the endpoints (delegate to the service; no logic here):
//   POST /api/slots                        201 + Location /api/slots/{id} + the SlotView; invalid body -> 400
//   GET  /api/stations/{stationId}/slots?from=   `from` is a required ISO-8601 instant; missing or malformed -> 400
//   POST /api/slots/{id}/reservations      200 + the updated SlotView (404 / 409 come from the exception handler)
public class SlotController {
    private final SlotService service;

    public SlotController(SlotService service) { this.service = service; }

    public Object create(CreateSlotRequest request) { return null; }

    public List<SlotView> available(String stationId, Instant from) { return null; }

    public SlotView reserve(long id) { return null; }
}
