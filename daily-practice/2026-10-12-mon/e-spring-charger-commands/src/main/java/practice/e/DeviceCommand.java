package practice.e;

import java.time.Instant;
import java.util.UUID;

/**
 * GIVEN - a plain object so no database is needed. In the paper version (questions/E) this is the JPA entity:
 * @Entity, @Id (UUID assigned by the app), @Version, a unique idempotency key and @Enumerated(EnumType.STRING).
 */
public class DeviceCommand {
    private final UUID id = UUID.randomUUID();
    private String idempotencyKey;
    private String chargerId;
    private CommandType type;
    private CommandStatus status = CommandStatus.PENDING;
    private Instant createdAt;
    private Instant expiresAt;

    public UUID getId() { return id; }
    public String getIdempotencyKey() { return idempotencyKey; }
    public void setIdempotencyKey(String idempotencyKey) { this.idempotencyKey = idempotencyKey; }
    public String getChargerId() { return chargerId; }
    public void setChargerId(String chargerId) { this.chargerId = chargerId; }
    public CommandType getType() { return type; }
    public void setType(CommandType type) { this.type = type; }
    public CommandStatus getStatus() { return status; }
    public void setStatus(CommandStatus status) { this.status = status; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getExpiresAt() { return expiresAt; }
    public void setExpiresAt(Instant expiresAt) { this.expiresAt = expiresAt; }
}
