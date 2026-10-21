package practice.e;

import java.time.Instant;
import java.util.UUID;

/** GIVEN - the response body. */
public record CaseView(UUID id, long version, String chargerSerial, Severity severity, CaseStatus status, String summary, Instant createdAt) {}
