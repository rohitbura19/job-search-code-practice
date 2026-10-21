> In this project, the service throws the GIVEN `StaleVersionException` (instead of `ObjectOptimisticLockingFailureException`) so no database is needed. Code: the separate Spring Boot project `e-spring-support-case/` (web layer: `ChargerSerial`, `CreateCaseRequest`, `UpdateCaseRequest`, `CaseController`, `ApiExceptionHandler`). Test: `CaseControllerTest`. The entity, `@Version`, SQL, YAML and Kafka-producer parts: write them on paper / in a scratch file and compare with answers.

## E) Notepad code-completion: support-case REST API — Bean Validation + `ProblemDetail` + `@Version` with `ETag` / `If-Match` + `Pageable` limits + Kafka PRODUCER after commit (25 min)

*This is the area Light & Wonder (Spring Boot / microservices) and IDFC R1 (Kafka + DB) will probe. Say out loud, while you fill it in: status codes (201, 400, 404, 409, 428), optimistic locking, transaction boundary, at-least-once, ordering by key.*

**Task.** Complete the TODOs without an IDE. Support staff open and update cases about chargers. `POST /api/v1/cases` creates a case (201 + `Location` + `ETag`); `GET /api/v1/cases?status=&page=&size=&sort=` lists them (page size capped at 100, sorting only on `createdAt`, `severity`, `status`); `PATCH /api/v1/cases/{id}` changes the status and REQUIRES an `If-Match` header carrying the version the client last saw. Rules: bad input is a 400 with a list of field errors (RFC 9457 `application/problem+json`); a stale version is a 409; a missing `If-Match` is a 428; an illegal status move (`CLOSED` to anything) is a 409; an unknown id is a 404. After a case is OPENED and the transaction has COMMITTED, publish a `CaseOpenedMessage` to the Kafka topic `support.case-opened.v1`, key = chargerSerial, JSON value. Spring Boot 3.3+, Java 17+, Hibernate 6, Postgres.

```java
// ---------- Request / response ----------
// TODO: a composed constraint @ChargerSerial (not blank, matches [A-Z0-9-]{6,32}, ONE message when it fails: which meta-annotation?)
// TODO: record CreateCaseRequest(chargerSerial, severity, summary (max 200), contactEmail (valid, max 120))  -- all required
// TODO: record UpdateCaseRequest(status)   record CaseView(id, version, chargerSerial, severity, status, summary, createdAt)

// ---------- Entity ----------
@Entity @Table(name = "support_case")
public class SupportCase {
    // TODO: UUID id assigned by the app; version field for optimistic locking (primitive long or Long? Spring Data's isNew() decides between persist and merge: which one is right here and why?)
    // TODO: chargerSerial, severity and status as STRING enums, summary, contactEmail, createdAt (Instant, from an injected Clock, not Instant.now() inside the entity)
    // TODO: moveTo(CaseStatus next): throws InvalidTransitionException when the move is not allowed
}

// ---------- Repository ----------
// TODO: JpaRepository<SupportCase, UUID> + Page<SupportCase> findByStatus(CaseStatus, Pageable)

// ---------- Service ----------
@Service
public class CaseService {
    // TODO: constructor injection: CaseRepository, ApplicationEventPublisher, Clock
    // TODO: open(req): save, publish an in-process CaseOpened event, return the view. Transaction settings?
    // TODO: update(id, expectedVersion, req): 404 when missing; reject a stale expectedVersion BEFORE writing; apply moveTo; make sure the returned view carries the NEW version. Why does that need a flush?
    // TODO: list(status, pageable): reject a sort property outside the whitelist (why? what happens with ?sort=password,desc when you do not check?)
    public CaseView open(CreateCaseRequest r) { return null; }
    public CaseView update(UUID id, long expectedVersion, UpdateCaseRequest r) { return null; }
    public Page<CaseView> list(CaseStatus status, Pageable p) { return null; }
}

// ---------- Kafka producer ----------
@Component
class CaseEventsPublisher {
    // TODO: send ONLY after the database commit, key = chargerSerial; log (do not block on) the result. What happens to the listener when open() runs WITHOUT a transaction?
}

// ---------- Controller ----------
// TODO: the three endpoints; 201 with Location and ETag; the page returned in a stable JSON shape (not a raw PageImpl); parse If-Match ("\"3\"" -> 3), 428 when absent

// ---------- Errors ----------
@RestControllerAdvice
public class ApiExceptionHandler /* TODO: extends the base class that already maps the standard Spring MVC exceptions to ProblemDetail */ {
    // TODO: override the validation handler to add an "errors" array [{field, message}] and the title "Validation failed"
    // TODO: handlers for not found (404), stale version (409), invalid transition (409), missing precondition (428)
}

// ---------- Tests ----------
// TODO 1 (@WebMvcTest): an invalid body -> 400, content type application/problem+json, "errors" lists exactly chargerSerial, summary and contactEmail, and the service is never called.
// TODO 2 (@DataJpaTest, no surrounding transaction): two detached copies of the same row; the first save wins, the second throws ObjectOptimisticLockingFailureException.
// TODO 3 (say, do not write): how would you prove that NO Kafka message is sent when open() rolls back, and that exactly one is sent when it commits?
```
```sql
-- V7__support_case.sql  TODO
create table support_case (
    id uuid primary key,
    version bigint not null,
    charger_serial varchar(__) not null,
    severity varchar(16) not null,
    status varchar(16) not null,
    summary varchar(___) not null,
    contact_email varchar(___) not null,
    created_at timestamptz not null
);
create index idx_case_status_created on support_case (______, ______ desc);    -- the list query: where status = ? order by created_at desc
```
```yaml
# application.yml  TODO
app.topics.case-opened: support.case-opened.v1
spring:
  data.web.pageable:
    default-page-size: ____
    max-page-size: ____                       # the Spring default is 2000: why is that a problem?
  jackson.deserialization.fail-on-unknown-properties: ____    # strict or lenient for a public API? what is the cost of strict?
  jpa.open-in-view: false
  kafka:
    bootstrap-servers: ${KAFKA_BOOTSTRAP}
    producer:
      acks: ____                              # what does it trade? which TOPIC setting must go with it?
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: ____
      properties:
        enable.idempotence: ____              # what does it prevent?
        delivery.timeout.ms: 30000
        linger.ms: ____                       # batching: latency or throughput?
        spring.json.add.type.headers: ____    # should the consumer learn our Java class name?
```

**Interviewer follow-ups:** Which exceptions does `@Valid` on a body raise, and which does `@Validated` on a `@RequestParam` raise in Spring 6.1? 400 or 422 for a validation failure, and why does it not matter as long as you are consistent? Show me the SQL Hibernate runs for the `PATCH` and say exactly when the version is checked and incremented. Two support agents press Save at the same time: what does each one see? Why is `If-Match` better than a `version` field in the body? Why 428 and not 400 when the header is missing? You publish to Kafka inside the `@Transactional` method instead of after the commit: name both failure cases (rollback after send; send after a commit that then fails to flush). You publish AFTER commit and the pod dies between the commit and the send: what is lost, and what does the outbox pattern change? What does `acks=all` give you without `min.insync.replicas=2`? What do idempotence and `max.in.flight.requests.per.connection` do for ordering when the producer retries? How do you get per-charger ordering, and what breaks it (a null key, a partition-count change)? What does `kafka.send(...).get()` inside a request thread cost? Why is returning an entity from the controller dangerous (mass assignment, lazy collections, leaking columns)? A `count(*)` query runs on every `Page` request: when does it hurt, and what do you use instead (`Slice`, keyset pagination)?
