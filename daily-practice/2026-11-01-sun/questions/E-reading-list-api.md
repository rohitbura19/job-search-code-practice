# E. Spring Boot: reading-list API with an element collection (30 min)

Project: `e-spring-reading-list/` (own `pom.xml`, Spring Boot 3.3, Java 21, H2 for tests). Files with TODOs: `Article`, `ArticleRepository`, `ReadingProps`, `ArticleRequest`, `ArticleService`, `ArticleController`, `ApiExceptionHandler`, `application.yml`. Given: exceptions, views, `TagCount`, `PagedResponse`, `CacheConfig`, `ReadingApplication`.

Behaviour:
- Entity `Article`: unique `url`, `tags` as an `@ElementCollection` (lazy), `createdAt` from Hibernate.
- Repository: JPQL join over the element collection with paging, an **interface projection** for tag statistics (group by + order), and an `@EntityGraph` derived query that loads tags eagerly for that call only.
- Service: tag normalisation, per-article tag limit from `reading.max-tags`, 409 on duplicate url, page-size cap, `@Cacheable("tagCounts")` + `@CacheEvict` on write, read-only vs read-write transactions.
- Controller: `POST` 201 + `Location`, `GET /{id}`, `GET ?tag&page&size` (parameter validation without `@Validated`), `GET /tags`. Every error is `application/problem+json`.
- `ReadingProps`: immutable validated `@ConfigurationProperties` record with defaults; `application.yml` turns `open-in-view` off.

Tests: `ArticleRepositoryTest` (`@DataJpaTest`), `ArticleControllerTest` (`@WebMvcTest`), `ArticleServiceTest` (Mockito), `TagCountsCacheTest` (Spring cache proxy), `ReadingPropsTest` (`ApplicationContextRunner` + yml check).
Run: `cd e-spring-reading-list && mvn test`. Reference: `answers/e-spring-reading-list/src/`.
Paper: why must the cached value be a plain record and not the projection proxy? What does `open-in-view=false` change for a lazy `tags` access in the controller? Why does the join query need no `distinct`, and when would it?
