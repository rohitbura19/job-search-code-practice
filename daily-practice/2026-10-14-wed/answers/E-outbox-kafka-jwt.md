# Answers: E-outbox-kafka-jwt

(Spoilers. Open only after your attempt.)

**Reference**
```java
@Entity @Table(name = "alert")
public class Alert {
    @Id private UUID id = UUID.randomUUID();
    @Column(nullable = false) private String siteId;
    @Enumerated(EnumType.STRING) private Severity severity;
    @Column(nullable = false, length = 500) private String message;
    private Instant createdAt;
}

@Entity @Table(name = "outbox_event", indexes = @Index(name = "idx_outbox_unsent", columnList = "sentAt, id"))
public class OutboxEvent {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    private String aggregateId;
    @JdbcTypeCode(SqlTypes.JSON) @Column(columnDefinition = "jsonb", nullable = false) private String payload;
    private Instant createdAt;
    private Instant sentAt;
}
// In Postgres a partial index is better: CREATE INDEX idx_outbox_unsent ON outbox_event (id) WHERE sent_at IS NULL;

public interface OutboxRepository extends JpaRepository<OutboxEvent, Long> {
    @Query(value = "select * from outbox_event where sent_at is null order by id limit :n for update skip locked",
           nativeQuery = true)
    List<OutboxEvent> lockBatch(@Param("n") int n);
}

public record AlertRequest(@NotBlank String siteId, @NotNull Alert.Severity severity,
                           @NotBlank @Size(max = 500) String message) {}

@ConfigurationProperties(prefix = "outbox") @Validated
public record OutboxProps(@NotBlank String topic, @DefaultValue("100") @Positive int batchSize) {}

@Service
public class AlertService {
    private final AlertRepository alerts; private final OutboxRepository outbox;
    private final ObjectMapper mapper; private final Clock clock;
    // constructor injection omitted

    @Transactional                                   // Alert row and outbox row commit or roll back together
    public Alert create(AlertRequest r) {
        Alert a = new Alert(); a.setSiteId(r.siteId()); a.setSeverity(r.severity());
        a.setMessage(r.message()); a.setCreatedAt(clock.instant());
        alerts.save(a);
        OutboxEvent e = new OutboxEvent();
        e.setAggregateId(a.getSiteId()); e.setCreatedAt(clock.instant());
        try { e.setPayload(mapper.writeValueAsString(AlertEvent.from(a))); }
        catch (JsonProcessingException ex) { throw new IllegalStateException(ex); }   // rolls back both rows
        outbox.save(e);
        return a;
    }
}

@Component
public class OutboxRelay {
    private final KafkaTemplate<String, String> kafka; private final OutboxRepository repo;
    private final OutboxProps props; private final Clock clock; private final Counter failures;
    public OutboxRelay(KafkaTemplate<String, String> kafka, OutboxRepository repo, OutboxProps props,
                       Clock clock, MeterRegistry registry) {
        this.kafka = kafka; this.repo = repo; this.props = props; this.clock = clock;
        this.failures = Counter.builder("outbox.relay.failures").register(registry);
    }

    @Scheduled(fixedDelayString = "PT0.5S")
    @Transactional                                    // the row locks live until this method returns
    public void relay() {
        for (OutboxEvent e : repo.lockBatch(props.batchSize())) {
            try {
                kafka.send(props.topic(), e.getAggregateId(), e.getPayload()).get(5, TimeUnit.SECONDS);
                e.setSentAt(clock.instant());         // dirty-checking flushes the UPDATE at commit
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt(); failures.increment(); break;
            } catch (ExecutionException | TimeoutException ex) {
                failures.increment(); log.warn("relay failed for outbox {}", e.getId(), ex);
                break;                                // stop: skipping would reorder messages for the same key
            }
        }
    }
}

@Component
public class AlertConsumer {
    @RetryableTopic(attempts = "4", backoff = @Backoff(delay = 1000, multiplier = 2.0),
                    exclude = IllegalArgumentException.class, dltStrategy = DltStrategy.FAIL_ON_ERROR)
    @KafkaListener(topics = "alerts", groupId = "alert-notifier")
    public void on(String payload) { /* parse; must be idempotent: key on the alert id */ }

    @DltHandler
    public void dlt(String payload, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.error("dead letter from {}: {}", topic, payload);
        dltCounter.increment();
    }
}

@RestController @RequestMapping("/api/v1/alerts")
public class AlertController {
    private final AlertService service;
    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_alerts:write')")    // needs @EnableMethodSecurity; URL rule below also enforces it
    public ResponseEntity<AlertResponse> create(@Valid @RequestBody AlertRequest r) {
        Alert a = service.create(r);
        return ResponseEntity.created(URI.create("/api/v1/alerts/" + a.getId())).body(AlertResponse.from(a));
    }
}

@Configuration @EnableMethodSecurity
public class SecurityConfig {
    @Bean SecurityFilterChain chain(HttpSecurity http) throws Exception {
        return http
            .csrf(c -> c.disable())                                          // no cookies/session: a bearer token is not sent automatically by browsers
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(a -> a
                .requestMatchers("/actuator/health/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/alerts").hasAuthority("SCOPE_alerts:write")
                .anyRequest().authenticated())
            .oauth2ResourceServer(o -> o.jwt(Customizer.withDefaults()))
            .build();
    }
}
```
```yaml
spring:
  security.oauth2.resourceserver.jwt.issuer-uri: https://auth.example.com/realms/prod
  jpa.open-in-view: false
  kafka:
    producer:
      acks: all
      properties.enable.idempotence: true
    consumer:
      group-id: alert-notifier
      auto-offset-reset: earliest
outbox:
  topic: alerts
  batch-size: 100
management:
  endpoints.web.exposure.include: health,metrics,prometheus
  endpoint.health.probes.enabled: true
```
Gotchas:
- **Why one transaction**: if you saved the alert and then sent to Kafka directly, a crash between the two loses the event (dual-write problem). With the outbox, the event is durable the moment the alert is; the relay delivers it at least once.
- **Crash after send, before commit**: the row is still unsent, so it is published again after restart. This is at-least-once, so the consumer MUST be idempotent (dedupe on the alert id, unique constraint or conditional write).
- **`SKIP LOCKED`**: without it, 3 pods all select the same top 100 rows and either block on each other's locks or publish duplicates. With it, each pod takes a different slice of unlocked rows. The trade-off: ordering across pods is no longer strict, which is why per-key order needs one relay per partition of keys, or CDC.
- **Open transaction during network I/O** pins a DB connection for up to `batch x 5 s` in the worst case. Keep batches small, or split into claim (short tx marks rows `IN_FLIGHT` with a lease), send, then mark `SENT`. Debezium reading the WAL removes polling and the lock entirely at the cost of running Kafka Connect.
- **Why stop at the first failure**: skipping row 7 and publishing row 8 for the same site reorders its events.
- **`@RetryableTopic`** creates `alerts-retry-0/1/2` and `alerts-dlt`; the main topic is unblocked while a message waits for its retry, so ordering for that key is lost. A blocking `DefaultErrorHandler` + `ExponentialBackOff` retries in place and preserves ordering but stalls the partition. Choose by whether order matters more than throughput. Non-retryable exceptions (bad payload) should go straight to the DLT.
- **Outbox cleanup**: a nightly job deleting rows with `sent_at < now() - 7 days` in small batches, or partition by day and drop partitions.
- **CSRF** protects cookie-authenticated browser sessions; a bearer-token API where nothing is sent automatically has no such attack surface.
- Scheduling note: `@Scheduled` runs on every pod. Here that is deliberate (SKIP LOCKED makes it safe), which is why it works without ShedLock.
