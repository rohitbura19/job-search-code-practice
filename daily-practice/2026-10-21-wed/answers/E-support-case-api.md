# Answers: E-support-case-api

(Spoilers. Open only after your attempt.)

**Reference**
```java
@Documented @Retention(RetentionPolicy.RUNTIME) @Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Constraint(validatedBy = {})
@NotBlank @Pattern(regexp = "[A-Z0-9-]{6,32}") @ReportAsSingleViolation          // composed: ONE violation, with our message
public @interface ChargerSerial {
    String message() default "must be 6 to 32 characters: A-Z, 0-9 and '-'";
    Class<?>[] groups() default {};  Class<? extends Payload>[] payload() default {};
}
public record CreateCaseRequest(@ChargerSerial String chargerSerial, @NotNull Severity severity,
        @NotBlank @Size(max = 200) String summary, @NotBlank @Email @Size(max = 120) String contactEmail) {}
public record UpdateCaseRequest(@NotNull CaseStatus status) {}
public record CaseView(UUID id, long version, String chargerSerial, Severity severity, CaseStatus status, String summary, Instant createdAt) {
    static CaseView of(SupportCase c) { return new CaseView(c.getId(), c.getVersion(), c.getChargerSerial(), c.getSeverity(), c.getStatus(), c.getSummary(), c.getCreatedAt()); }
}

public enum Severity { LOW, MEDIUM, HIGH, CRITICAL }
public enum CaseStatus {
    OPEN, IN_PROGRESS, RESOLVED, CLOSED;
    boolean canMoveTo(CaseStatus n) {
        return switch (this) { case OPEN -> n == IN_PROGRESS || n == CLOSED; case IN_PROGRESS -> n == RESOLVED || n == OPEN;
                               case RESOLVED -> n == CLOSED || n == IN_PROGRESS; case CLOSED -> false; };
    }
}

@Entity @Table(name = "support_case")
public class SupportCase {
    @Id private UUID id = UUID.randomUUID();                                     // assigned by the app
    @Version private Long version;                                                // WRAPPER type: null means "new", so save() calls persist (a primitive long would make an assigned id look like an existing row => a SELECT and a merge)
    @Column(nullable = false, length = 32) private String chargerSerial;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 16) private Severity severity;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 16) private CaseStatus status = CaseStatus.OPEN;
    @Column(nullable = false, length = 200) private String summary;
    @Column(nullable = false, length = 120) private String contactEmail;
    @Column(nullable = false) private Instant createdAt;
    protected SupportCase() {}                                                    // for Hibernate
    public SupportCase(String serial, Severity sev, String summary, String email, Instant now) {
        this.chargerSerial = serial; this.severity = sev; this.summary = summary; this.contactEmail = email; this.createdAt = now;
    }
    public void moveTo(CaseStatus next) { if (!status.canMoveTo(next)) throw new InvalidTransitionException(status, next); status = next; }
    // getters only: no setters, so the only way to change the status is moveTo()
}
public interface CaseRepository extends JpaRepository<SupportCase, UUID> { Page<SupportCase> findByStatus(CaseStatus status, Pageable pageable); }

public record CaseOpened(UUID caseId, String chargerSerial, Severity severity, Instant occurredAt) {}          // in-process event
public record CaseOpenedMessage(UUID caseId, String chargerSerial, Severity severity, Instant occurredAt) {}    // the Kafka contract: separate class, versioned by the topic name

@Service
public class CaseService {
    private static final Set<String> SORTABLE = Set.of("createdAt", "severity", "status");
    private final CaseRepository repo; private final ApplicationEventPublisher events; private final Clock clock;
    // constructor omitted

    @Transactional
    public CaseView open(CreateCaseRequest r) {
        SupportCase c = repo.save(new SupportCase(r.chargerSerial(), r.severity(), r.summary(), r.contactEmail(), clock.instant()));
        events.publishEvent(new CaseOpened(c.getId(), c.getChargerSerial(), c.getSeverity(), c.getCreatedAt()));     // held until the transaction ends
        return CaseView.of(c);
    }
    @Transactional
    public CaseView update(UUID id, long expectedVersion, UpdateCaseRequest r) {
        SupportCase c = repo.findById(id).orElseThrow(() -> new CaseNotFoundException(id));
        if (c.getVersion() != expectedVersion) throw new ObjectOptimisticLockingFailureException(SupportCase.class, id);   // the client's copy is already stale: fail before writing
        c.moveTo(r.status());
        repo.saveAndFlush(c);        // UPDATE support_case SET status=?, version=? WHERE id=? AND version=?   0 rows => ObjectOptimisticLockingFailureException; AND the entity now holds the NEW version for the ETag
        return CaseView.of(c);
    }
    @Transactional(readOnly = true)
    public Page<CaseView> list(CaseStatus status, Pageable p) {
        p.getSort().forEach(o -> { if (!SORTABLE.contains(o.getProperty())) throw new UnsupportedSortException(o.getProperty()); });
        Page<SupportCase> page = status == null ? repo.findAll(p) : repo.findByStatus(status, p);
        return page.map(CaseView::of);
    }
}

@Component
class CaseEventsPublisher {
    private static final Logger log = LoggerFactory.getLogger(CaseEventsPublisher.class);
    private final KafkaTemplate<String, CaseOpenedMessage> kafka; private final String topic;
    CaseEventsPublisher(KafkaTemplate<String, CaseOpenedMessage> kafka, @Value("${app.topics.case-opened}") String topic) { this.kafka = kafka; this.topic = topic; }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)           // runs only if the transaction committed; NOT at all when there is no transaction (unless fallbackExecution = true)
    void onCaseOpened(CaseOpened e) {
        kafka.send(topic, e.chargerSerial(), new CaseOpenedMessage(e.caseId(), e.chargerSerial(), e.severity(), e.occurredAt()))   // key = serial: one charger, one partition, in order
             .whenComplete((res, ex) -> {                                          // async: the request thread is not held by the broker
                 if (ex != null) log.error("case-opened NOT published, case={} (row exists, event does not)", e.caseId(), ex);
                 else log.debug("published {}-{}@{}", res.getRecordMetadata().topic(), res.getRecordMetadata().partition(), res.getRecordMetadata().offset());
             });
    }
}

@RestController @RequestMapping("/api/v1/cases")
public class CaseController {
    private final CaseService service;
    public CaseController(CaseService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<CaseView> open(@Valid @RequestBody CreateCaseRequest req, UriComponentsBuilder uri) {
        CaseView v = service.open(req);
        return ResponseEntity.created(uri.path("/api/v1/cases/{id}").buildAndExpand(v.id()).toUri()).eTag(etag(v.version())).body(v);
    }
    @GetMapping
    public PagedModel<CaseView> list(@RequestParam(required = false) CaseStatus status,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return new PagedModel<>(service.list(status, pageable));                    // a stable {content, page{size,number,totalElements,totalPages}} shape
    }
    @PatchMapping("/{id}")
    public ResponseEntity<CaseView> update(@PathVariable UUID id, @RequestHeader(value = HttpHeaders.IF_MATCH, required = false) String ifMatch,
            @Valid @RequestBody UpdateCaseRequest req) {
        CaseView v = service.update(id, parseVersion(ifMatch), req);
        return ResponseEntity.ok().eTag(etag(v.version())).body(v);
    }
    private static String etag(long version) { return "\"" + version + "\""; }
    private static long parseVersion(String ifMatch) {
        if (ifMatch == null || ifMatch.isBlank()) throw new PreconditionRequiredException("send If-Match with the ETag of the case you read");
        try { return Long.parseLong(ifMatch.replace("\"", "").trim()); }
        catch (NumberFormatException e) { throw new PreconditionRequiredException("If-Match must be a quoted version, for example \"3\""); }
    }
}

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {          // already maps ~20 Spring MVC exceptions to ProblemDetail
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ProblemDetail pd = ex.getBody(); pd.setTitle("Validation failed");
        pd.setProperty("errors", ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> Map.of("field", fe.getField(), "message", String.valueOf(fe.getDefaultMessage()))).toList());
        return handleExceptionInternal(ex, pd, headers, status, request);
    }
    @ExceptionHandler(CaseNotFoundException.class)
    ProblemDetail notFound(CaseNotFoundException e) { return problem(HttpStatus.NOT_FOUND, "Case not found", e.getMessage()); }
    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    ProblemDetail stale(ObjectOptimisticLockingFailureException e) { return problem(HttpStatus.CONFLICT, "Stale version", "The case was changed by someone else. Reload it and try again."); }
    @ExceptionHandler(InvalidTransitionException.class)
    ProblemDetail transition(InvalidTransitionException e) { return problem(HttpStatus.CONFLICT, "Invalid status change", e.getMessage()); }
    @ExceptionHandler(PreconditionRequiredException.class)
    ProblemDetail precondition(PreconditionRequiredException e) { return problem(HttpStatus.PRECONDITION_REQUIRED, "If-Match required", e.getMessage()); }
    @ExceptionHandler(UnsupportedSortException.class)
    ProblemDetail badSort(UnsupportedSortException e) { return problem(HttpStatus.BAD_REQUEST, "Unsupported sort", e.getMessage()); }
    private static ProblemDetail problem(HttpStatus s, String title, String detail) { ProblemDetail pd = ProblemDetail.forStatusAndDetail(s, detail); pd.setTitle(title); return pd; }
}
```
```java
@WebMvcTest(CaseController.class) @Import(ApiExceptionHandler.class)
class CaseControllerTest {
    @Autowired MockMvc mvc;
    @MockitoBean CaseService service;                                               // @MockBean before Boot 3.4

    @Test void invalidBodyIsProblemJsonWithFieldErrors() throws Exception {
        mvc.perform(post("/api/v1/cases").contentType(MediaType.APPLICATION_JSON)
                .content("{\"chargerSerial\":\"ab\",\"severity\":\"HIGH\",\"summary\":\" \",\"contactEmail\":\"nope\"}"))
           .andExpect(status().isBadRequest())
           .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
           .andExpect(jsonPath("$.title").value("Validation failed"))
           .andExpect(jsonPath("$.errors[*].field", containsInAnyOrder("chargerSerial", "summary", "contactEmail")));
        verifyNoInteractions(service);
    }
    @Test void missingIfMatchIs428() throws Exception {
        mvc.perform(patch("/api/v1/cases/{id}", UUID.randomUUID()).contentType(MediaType.APPLICATION_JSON).content("{\"status\":\"RESOLVED\"}"))
           .andExpect(status().isPreconditionRequired());
    }
    @Test void staleVersionIs409() throws Exception {
        UUID id = UUID.randomUUID();
        when(service.update(eq(id), eq(3L), any())).thenThrow(new ObjectOptimisticLockingFailureException(SupportCase.class, id));
        mvc.perform(patch("/api/v1/cases/{id}", id).header(HttpHeaders.IF_MATCH, "\"3\"").contentType(MediaType.APPLICATION_JSON).content("{\"status\":\"RESOLVED\"}"))
           .andExpect(status().isConflict());
    }
}

@DataJpaTest @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) @Testcontainers
@Transactional(propagation = Propagation.NOT_SUPPORTED)                             // no surrounding transaction: each TransactionTemplate call really commits
class SupportCaseLockingTest {
    @Container @ServiceConnection static PostgreSQLContainer<?> pg = new PostgreSQLContainer<>("postgres:16");
    @Autowired CaseRepository repo; @Autowired PlatformTransactionManager tm;

    @Test void secondWriterLoses() {
        TransactionTemplate tx = new TransactionTemplate(tm);
        UUID id = tx.execute(s -> repo.saveAndFlush(new SupportCase("EVC-0001", Severity.HIGH, "no power", "a@b.example", Instant.now())).getId());
        SupportCase a = tx.execute(s -> repo.findById(id).orElseThrow());          // detached copy, version 0
        SupportCase b = tx.execute(s -> repo.findById(id).orElseThrow());          // detached copy, version 0
        a.moveTo(CaseStatus.IN_PROGRESS); tx.executeWithoutResult(s -> repo.saveAndFlush(a));       // row version 0 -> 1
        b.moveTo(CaseStatus.CLOSED);
        assertThatThrownBy(() -> tx.executeWithoutResult(s -> repo.saveAndFlush(b))).isInstanceOf(ObjectOptimisticLockingFailureException.class);
    }
}
// TODO 3 answer: @SpringBootTest with @MockitoBean KafkaTemplate. Commit case: open() through the real proxy, then verify(kafka).send(eq(topic), eq("EVC-0001"), any()) exactly once.
//   Rollback case: call open() from a test method that throws after the service returns inside a TransactionTemplate (or make the repository save throw), then verifyNoInteractions(kafka).
//   For a real broker use @EmbeddedKafka or Testcontainers Kafka and read the record back with KafkaTestUtils.
```
```sql
create table support_case (
    id uuid primary key,
    version bigint not null,
    charger_serial varchar(32) not null,
    severity varchar(16) not null,
    status varchar(16) not null,
    summary varchar(200) not null,
    contact_email varchar(120) not null,
    created_at timestamptz not null
);
create index idx_case_status_created on support_case (status, created_at desc);
```
```yaml
app.topics.case-opened: support.case-opened.v1
spring:
  data.web.pageable:
    default-page-size: 20
    max-page-size: 100                        # the default cap is 2000: one request could pull 2000 rows (and sort them) for every caller
  jackson.deserialization.fail-on-unknown-properties: true    # catches typos like "sumary" at once; the cost is that a client sending a newer field breaks (Boot's default is lenient)
  jpa.open-in-view: false
  kafka:
    bootstrap-servers: ${KAFKA_BOOTSTRAP}
    producer:
      acks: all                               # leader AND all in-sync replicas must have it; pair with min.insync.replicas=2 on a replication-factor-3 topic
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer
      properties:
        enable.idempotence: true              # a producer retry cannot create a duplicate or reorder records inside one partition
        delivery.timeout.ms: 30000            # the upper bound on send + retries; after that the callback gets the error
        linger.ms: 5                          # wait up to 5 ms to fill a batch: a little latency for much better throughput
        spring.json.add.type.headers: false   # the consumer should bind to the contract, not to our Java class name
```
Gotchas:
- **Primitive `long` version.** `JpaMetamodelEntityInformation.isNew` uses the version attribute ONLY when it is a wrapper type; with a primitive `long` it falls back to the id, and an id assigned in the constructor is never null, so `save` treats the row as existing: a `SELECT`, then a `merge`. With `Long version` (null until persisted) `save` calls `persist` and issues a single `INSERT`. The same trap exists for an assigned id without a version.
- **When the version is checked.** Hibernate adds `where id = ? and version = ?` to the `UPDATE` and increments the version AT FLUSH. If the flush is left to the commit, the DTO built inside the method still carries the OLD version (the `ETag` would be wrong), and a conflict surfaces after the method returned, from the transaction proxy. `saveAndFlush` fixes both: the exception comes out of your own method and the entity has the new version. The explicit check before writing is only a fast path for a client that is already stale; the `WHERE version = ?` is what protects against two writers that both read version 3.
- **Why `ETag` / `If-Match` instead of a body field.** It is the standard HTTP way to say "apply this only if the resource is still the version I read" (conditional requests), works the same for any resource, and caches and proxies understand it. A missing header on an endpoint that requires it is a 428 Precondition Required (RFC 6585): it tells the client what to do, where a 400 only says it is wrong. A mismatch is 409 here (412 Precondition Failed is the strictly standard answer to a failed `If-Match`; pick one and document it).
- **After commit, and what it does not give you.** An event published inside the transaction can leave for Kafka and then the transaction rolls back (a message about a case that does not exist); an event published after the commit can be lost if the pod dies in between, or if the broker is down (the row exists, the message does not). Kafka transactions cannot span the database. The reliable fix is the outbox: insert the message into an `outbox` table in the SAME database transaction as the case, and let a relay publish it (at-least-once, with an event id so the consumer can de-duplicate). `@TransactionalEventListener(AFTER_COMMIT)` is the cheap, honest version when a lost event is acceptable or a reconciliation job exists. It also does nothing when there is no active transaction, unless you set `fallbackExecution = true`, and a failure inside it does not roll anything back.
- **Producer settings in one breath.** `acks=all` waits for the in-sync replicas; with `min.insync.replicas=2` and replication factor 3 one broker can fail without losing an acknowledged record, and if fewer than 2 replicas are in sync the send fails instead of silently accepting it. `enable.idempotence=true` (the default since Kafka 3.0) gives each producer a PID and sequence numbers per partition, so broker-side retries are de-duplicated and, with at most 5 in-flight requests, kept in order. Ordering is per PARTITION: the key (`chargerSerial`) fixes the partition; a null key spreads records round-robin (no order), and adding partitions later changes the key-to-partition mapping. `linger.ms` and `batch.size` trade latency for throughput; `compression.type` (lz4, zstd) saves network on JSON. `send(...).get()` would hold the request thread for the broker round trip and its retries (up to `delivery.timeout.ms`), so use the callback.
- **`ProblemDetail`.** Spring 6 supports RFC 7807 / 9457 natively. If your advice extends `ResponseEntityExceptionHandler`, the standard MVC exceptions (bad JSON, wrong media type, missing parameter, `HandlerMethodValidationException`, 404 for a missing handler when configured) are already mapped; otherwise set `spring.mvc.problemdetails.enabled=true` for Boot to register the equivalent. Never put a stack trace or an SQL message into `detail`; log it with a correlation id and return the id. Keep `type` URIs stable if clients branch on them.
- **Validation layers.** `@Valid` on a `@RequestBody` raises `MethodArgumentNotValidException`; Spring 6.1 method validation for `@RequestParam`/`@PathVariable` constraints raises `HandlerMethodValidationException` (Boot 3.2+; earlier `ConstraintViolationException` with `@Validated` on the class). Bean Validation checks SHAPE (not blank, size, format); the business rule "this status move is illegal" lives in the domain (`moveTo`), and uniqueness belongs to a database constraint, because a check-then-insert races. A composed constraint such as `@ChargerSerial` keeps the rule in one place; without `@ReportAsSingleViolation` the client would get one error per inner constraint.
- **`Pageable`.** Cap `max-page-size`. A client-chosen `sort` is turned into `order by <property>`; an unknown property raises `PropertyReferenceException` (a 500 unless handled) and a valid but unindexed one makes a full sort, so whitelist the sort keys and index the default order (`status, created_at desc`). A `Page` runs an extra `count(*)`, which on millions of rows costs more than the data query: use `Slice` (it fetches `size + 1` rows) or keyset pagination (`where created_at < :last order by created_at desc limit :n`) for deep or infinite-scroll lists. `PagedModel` (Spring Data 3.3) is the stable JSON shape; serialising a raw `PageImpl` triggers a warning because its shape is not a contract.
- **Do not expose the entity.** A request bound to an entity allows mass assignment (`{"status":"CLOSED","version":99}`), a response from an entity leaks columns and lazy collections; DTO records with explicit fields are the contract. With `open-in-view: false` the controller never touches a lazy field anyway.
- **Time.** `Instant.now()` inside an entity or service cannot be tested; inject a `Clock` (the same lesson as the reservation book and the rating engine).
