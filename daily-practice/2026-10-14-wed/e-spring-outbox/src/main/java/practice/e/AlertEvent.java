package practice.e;

import java.util.UUID;

/** GIVEN. The JSON that goes into the outbox payload (createdAt as an ISO string so a plain ObjectMapper can write it). */
public record AlertEvent(UUID id, String siteId, String severity, String message, String createdAt) {
    public static AlertEvent from(Alert a) {
        return new AlertEvent(a.getId(), a.getSiteId(), a.getSeverity().name(), a.getMessage(),
                a.getCreatedAt() == null ? null : a.getCreatedAt().toString());
    }
}
