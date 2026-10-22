package practice.e;

import org.springframework.http.ResponseEntity;

/**
 * TODO: a REST controller at /api/v1/chargers. Constructor injection of the repository AND the service.
 *  - GET  /{chargerId}/status  -> 200 with the latest event as an EventDto (from findFirstByChargerIdOrderByEventTimeDesc), or 404 with no body.
 *  - POST /events              -> body is a VALIDATED EventDto (@Valid @RequestBody); call service.save(dto); respond 202 Accepted with no body.
 */
public class ChargerController {
    public ChargerController(ChargerEventRepository repo, ChargerEventService service) {
        // TODO
    }

    public ResponseEntity<EventDto> status(String chargerId) {
        return null;
    }

    public ResponseEntity<Void> ingest(EventDto dto) {
        return null;
    }
}
