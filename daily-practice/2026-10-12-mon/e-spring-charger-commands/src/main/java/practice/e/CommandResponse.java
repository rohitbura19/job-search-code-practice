package practice.e;

import java.time.Instant;
import java.util.UUID;

/** GIVEN - the response body. */
public record CommandResponse(UUID id, String chargerId, CommandType type, CommandStatus status, Instant expiresAt) {
    public static CommandResponse from(DeviceCommand c) {
        return new CommandResponse(c.getId(), c.getChargerId(), c.getType(), c.getStatus(), c.getExpiresAt());
    }
}
