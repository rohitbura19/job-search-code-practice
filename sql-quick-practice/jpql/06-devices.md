# 06. devices - repository queries

_From daily-practice/2026-10-27-tue. > Project: `e-spring-devices/` (own `pom.xml`). Tests: `DeviceRepositoryTest` (`@DataJpaTest`), `DeviceControllerTest` (`@WebMvcTest`), `DeviceServiceTest` (Mockito + fixed `Clock`), `SerialValidatorTest` (plain JUnit)._

Write the annotation + query for every TODO (JPQL unless it says native). Answers: `daily-practice/2026-10-27-tue/answers/`.

## Entity `Device`

```java
    private Long id;
    private String serial;
    private String model;
    private Firmware firmware;
    private DeviceStatus status;
    private Instant lastSeen;
```

## `DeviceRepository`

```java
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
```
