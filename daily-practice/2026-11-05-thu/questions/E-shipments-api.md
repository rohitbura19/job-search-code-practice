# E. Spring Boot + Kafka: shipments (30 min)

Project `e-spring-shipments/` (Spring Boot 3.3, Java 21, H2). No broker is needed: tests use `@DataJpaTest`, `@WebMvcTest` and mocks. Fill the TODO gaps; the header comment in each gap file says exactly what to write.

| Gap file | What |
|---|---|
| `CreateShipmentRequest` | bean validation annotations |
| `Shipment` | JPA mapping: ids, constraints, `@Enumerated(STRING)`, `@Version` |
| `ShipmentRepository` | derived finders + `@Query` with optional status filter and `countQuery` |
| `ShipmentPublisher` | `KafkaTemplate.send(topic, key, json)`, topic from `@Value` |
| `ShipmentService` | create + publish, duplicate -> 409, paging clamp, **idempotent consumer** (`ProcessedEvent`), forward-only status |
| `CarrierListener` | `@KafkaListener`, JSON -> `CarrierUpdate`, drop poison messages, let real failures propagate |
| `ShipmentController` | `201 + Location`, `@Valid`, defaults for paging params |

Tests: `ShipmentRepositoryTest`, `ShipmentServiceTest`, `ShipmentPublisherTest`, `CarrierListenerTest`, `ShipmentControllerTest`. Run: `cd e-spring-shipments && mvn test` (or one class with `-Dtest=`).

Think about: what if the DB commit fails after `kafkaTemplate.send` succeeded (dual write)? What if the app crashes between commit and offset commit? Answers in the README review.
