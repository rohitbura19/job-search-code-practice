package practice.e;

import java.math.BigDecimal;
import java.time.Instant;

/** Given. */
public record TransferView(Long id, String idempotencyKey, Long fromId, Long toId, BigDecimal amount, Instant createdAt) {
    static TransferView of(Transfer t) {
        return new TransferView(t.getId(), t.getIdempotencyKey(), t.getFromId(), t.getToId(), t.getAmount(), t.getCreatedAt());
    }
}
