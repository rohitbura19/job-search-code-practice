# E. Spring Boot: stock reservation with Kafka (30 min)

Project: `e-spring-inventory/` (own `pom.xml`, Spring Boot 3.3, Java 21, H2 for tests, spring-kafka). No broker is needed: `KafkaTemplate` is mocked and the listener method is called directly.
Files with TODOs: `StockItem`, `StockRepository`, `ReserveRequest`, `StockProps`, `StockEventPublisher`, `RestockListener`, `StockService`, `StockController`, `ApiExceptionHandler`, `application.yml`.

Behaviour:
- `POST /api/stock/reservations {"sku","qty"}`: `sku` matches `[A-Z0-9-]{3,32}`, `qty` 1..100. Reserve **atomically in one UPDATE** (`where onHand - reserved >= qty`), so two concurrent callers cannot oversell. 200 with the view; 409 `Not enough stock`; 404 unknown SKU.
- After a reservation the service publishes `stock.reserved` (key = sku); if `available <= lowThreshold` also `stock.low`.
- `@KafkaListener` on `stock.restock` (key = sku, value = quantity text) calls `service.restock`; poison messages are logged and skipped, never thrown.
- `GET /api/stock/low?threshold&page&size`: paged, ordered by sku, `size` capped by `stock.max-page-size`; bad parameters give 400.
- `GET /api/stock/{sku}`; errors are `application/problem+json`.
- `StockProps` is an immutable `@ConfigurationProperties` record with defaults and validation; `application.yml` configures JPA and Kafka serializers.

Tests: `StockRepositoryTest` (`@DataJpaTest`), `StockControllerTest` (`@WebMvcTest`), `StockServiceTest` (Mockito), `KafkaPartsTest` (publisher + listener, no broker), `StockPropsTest` (`ApplicationContextRunner` + yml check).
Run: `cd e-spring-inventory && mvn test`. Reference: `answers/e-spring-inventory/src/`.
Paper: why is publishing inside the transaction a dual-write problem, and what would you change? (See README review.)
