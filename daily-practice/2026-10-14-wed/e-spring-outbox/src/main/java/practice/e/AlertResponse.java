package practice.e;

import java.time.Instant;
import java.util.UUID;

/** GIVEN. */
public record AlertResponse(UUID id, String siteId, Alert.Severity severity, String message, Instant createdAt) {
    public static AlertResponse from(Alert a) {
        return new AlertResponse(a.getId(), a.getSiteId(), a.getSeverity(), a.getMessage(), a.getCreatedAt());
    }
}
