# Answers: E-charger-commands

(Spoilers. Open only after your attempt.)

**Reference**
```java
@Entity @Table(name = "device_command",
       uniqueConstraints = @UniqueConstraint(columnNames = "idempotencyKey"))
public class DeviceCommand {
    @Id private UUID id = UUID.randomUUID();
    @Version private Long version;
    @Column(nullable = false) private String idempotencyKey;
    @Enumerated(EnumType.STRING) private CommandType type;
    @Enumerated(EnumType.STRING) private Status status = Status.PENDING;
    // ...
}

public record CommandRequest(@NotNull DeviceCommand.CommandType type,
                             @Min(1) @Max(3600) Integer ttlSeconds) {}

public interface DeviceCommandRepository extends JpaRepository<DeviceCommand, UUID> {
    Optional<DeviceCommand> findByIdempotencyKey(String key);
    Page<DeviceCommand> findByChargerIdOrderByCreatedAtDesc(String chargerId, Pageable p);

    @Modifying
    @Query("update DeviceCommand c set c.status = :to where c.status = :from and c.expiresAt < :now")
    int expire(@Param("from") Status from, @Param("to") Status to, @Param("now") Instant now);
}

@ConfigurationProperties(prefix = "commands")
public record CommandProps(String topic, Duration defaultTtl) {}
// enable with @EnableConfigurationProperties(CommandProps.class) or @ConfigurationPropertiesScan

@Service
public class CommandService {
    private final DeviceCommandRepository repo;
    private final ApplicationEventPublisher events;
    private final CommandProps props;
    private final Clock clock;
    // constructor injection omitted for brevity

    @Transactional
    public DeviceCommand create(String chargerId, String key, CommandRequest req) {
        return repo.findByIdempotencyKey(key).orElseGet(() -> {
            DeviceCommand c = new DeviceCommand();
            c.setIdempotencyKey(key); c.setChargerId(chargerId); c.setType(req.type());
            Duration ttl = req.ttlSeconds() == null ? props.defaultTtl() : Duration.ofSeconds(req.ttlSeconds());
            c.setCreatedAt(clock.instant()); c.setExpiresAt(clock.instant().plus(ttl));
            repo.save(c);
            events.publishEvent(new CommandCreated(c.getId(), chargerId, c.getType()));
            return c;
        });
        // concurrent duplicate: unique constraint fails -> catch DataIntegrityViolationException in the
        // controller advice (or re-read) and return the existing row
    }
}

@Component
public class CommandPublisher {
    private final KafkaTemplate<String, CommandMessage> kafka;
    private final CommandProps props;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(CommandCreated e) {
        kafka.send(props.topic(), e.chargerId(), CommandMessage.from(e))
             .whenComplete((r, ex) -> { if (ex != null) log.error("publish failed {}", e.id(), ex); });
    }
}

@RestController
@RequestMapping("/api/v1/chargers")
public class CommandController {
    private final CommandService service;
    private final DeviceCommandRepository repo;

    @PostMapping("/{chargerId}/commands")
    public ResponseEntity<CommandResponse> create(@PathVariable String chargerId,
            @RequestHeader("Idempotency-Key") String key, @Valid @RequestBody CommandRequest req) {
        DeviceCommand c = service.create(chargerId, key, req);
        return ResponseEntity.accepted()
                .location(URI.create("/api/v1/commands/" + c.getId()))
                .body(CommandResponse.from(c));
    }

    @GetMapping("/{chargerId}/commands")
    public Page<CommandResponse> history(@PathVariable String chargerId,
            @PageableDefault(size = 20) Pageable p) {
        return repo.findByChargerIdOrderByCreatedAtDesc(chargerId, p).map(CommandResponse::from);
    }
}

@Component @EnableScheduling   // @EnableScheduling goes on a @Configuration class
public class CommandExpiryJob {
    @Scheduled(fixedDelay = 60_000)
    @Transactional
    public void run() { repo.expire(Status.PENDING, Status.EXPIRED, Instant.now()); }
}
```
```yaml
commands:
  topic: charger-commands
  default-ttl: PT5M
spring:
  kafka:
    producer:
      acks: all
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer
      properties:
        enable.idempotence: true
        compression.type: lz4
        linger.ms: 10
server:
  shutdown: graceful
```
Gotchas: `@Modifying` queries need a transaction. The scheduler runs on every instance, so with N pods you need ShedLock or an idempotent query (this one is idempotent). `AFTER_COMMIT` fixes "message sent but rolled back" but not "committed but never sent"; the outbox table (written in the same transaction, relayed by a poller/CDC) fixes that. `@Version` throws `OptimisticLockingFailureException` on concurrent updates (lost update protection). Idempotent producer prevents duplicates from producer retries within a partition only; consumers still must be idempotent. Key = `chargerId` keeps per-device ordering.

In the practice project the controller calls `service.history(...)` (given) instead of the repository, the repository is the `CommandStore` interface, and the expiry job takes `now` from the injected `Clock` (the tests fix the clock).
