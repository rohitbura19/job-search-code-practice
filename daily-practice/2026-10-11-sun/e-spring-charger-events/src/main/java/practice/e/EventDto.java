package practice.e;

import java.time.Instant;

/**
 * TODO: add Bean Validation annotations to the components (jakarta.validation.constraints):
 * eventId and chargerId not blank, status not null, kwh zero or positive, eventTime not null.
 */
public record EventDto(String eventId, String chargerId, ChargerEvent.Status status, double kwh, Instant eventTime) {}
