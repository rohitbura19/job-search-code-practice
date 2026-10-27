package practice.e;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

// TODO: complete the repository (the signatures are given; some need annotations, none needs a method body):
//   1. findBySerial, existsBySerial: derived. Which is cheaper when you only need "is it taken?"
//   2. findByModelContainingIgnoreCaseAndStatusOrderBySerialAsc: derived; what SQL does "ContainingIgnoreCase" produce?
//   3. findByFirmwareChannelOrderBySerialAsc: derived through the embedded property (firmware.channel)
//   4. retireSeenBefore: ONE bulk JPQL UPDATE: every ACTIVE device whose lastSeen is before :cutoff becomes RETIRED; returns the number of rows.
//      Needs @Modifying, @Query and @Param. The persistence context is now stale: which @Modifying attribute fixes it for the
//      next read in the same transaction? Use the fully qualified enum literal in JPQL.
public interface DeviceRepository extends JpaRepository<Device, Long> {
    Optional<Device> findBySerial(String serial);

    boolean existsBySerial(String serial);

    List<Device> findByModelContainingIgnoreCaseAndStatusOrderBySerialAsc(String fragment, DeviceStatus status);

    List<Device> findByFirmwareChannelOrderBySerialAsc(Channel channel);

    int retireSeenBefore(Instant cutoff);
}
