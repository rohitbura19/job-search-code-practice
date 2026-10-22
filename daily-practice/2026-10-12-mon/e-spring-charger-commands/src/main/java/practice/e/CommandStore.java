package practice.e;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.Optional;

/** GIVEN - stands in for the Spring Data repository (paper version: JpaRepository + derived queries + @Modifying @Query). */
public interface CommandStore {
    Optional<DeviceCommand> findByIdempotencyKey(String key);
    DeviceCommand save(DeviceCommand command);
    /** Newest first. */
    Page<DeviceCommand> history(String chargerId, Pageable pageable);
    /** Bulk update: every command in `from` whose expiresAt is before `now` becomes `to`. Returns the rows changed. */
    int expire(CommandStatus from, CommandStatus to, Instant now);
}
