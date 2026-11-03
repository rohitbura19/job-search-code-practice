package practice.e;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.time.Clock;

/**
 * TODO - write create():
 *  - one transaction: add the annotation that makes the Alert row and the outbox row commit or roll back together
 *  - build an Alert from the request (siteId, severity, message, createdAt = clock.instant()) and save it
 *  - build an OutboxEvent: aggregateId = siteId, createdAt = clock.instant(),
 *    payload = JSON of AlertEvent.from(alert) written with the mapper;
 *    if the JSON cannot be written throw IllegalStateException (so the transaction rolls back)
 *  - save the outbox row and return the alert
 */
@Service
public class AlertService {
    private final AlertRepository alerts;
    private final OutboxRepository outbox;
    private final ObjectMapper mapper;
    private final Clock clock;

    public AlertService(AlertRepository alerts, OutboxRepository outbox, ObjectMapper mapper, Clock clock) {
        this.alerts = alerts; this.outbox = outbox; this.mapper = mapper; this.clock = clock;
    }

    public Alert create(AlertRequest r) {
        throw new UnsupportedOperationException("TODO");
    }
}
