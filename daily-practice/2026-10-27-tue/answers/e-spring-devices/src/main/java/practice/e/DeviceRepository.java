package practice.e;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * existsBy runs "select 1 ... limit 1" (no entity materialised). ContainingIgnoreCase = lower(model) like lower(?) escaped,
 * which cannot use a normal index (use a trigram/functional index if it gets hot).
 * The bulk UPDATE bypasses the persistence context and dirty checking: one statement instead of N loads and N updates, but
 * managed entities are now stale, hence clearAutomatically = true (flushAutomatically if pending changes must go first).
 */
public interface DeviceRepository extends JpaRepository<Device, Long> {
    Optional<Device> findBySerial(String serial);

    boolean existsBySerial(String serial);

    List<Device> findByModelContainingIgnoreCaseAndStatusOrderBySerialAsc(String fragment, DeviceStatus status);

    List<Device> findByFirmwareChannelOrderBySerialAsc(Channel channel);

    @Modifying(clearAutomatically = true)
    @Query("update Device d set d.status = practice.e.DeviceStatus.RETIRED "
            + "where d.status = practice.e.DeviceStatus.ACTIVE and d.lastSeen < :cutoff")
    int retireSeenBefore(@Param("cutoff") Instant cutoff);
}
