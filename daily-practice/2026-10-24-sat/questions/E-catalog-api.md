> Project: `e-spring-catalog/` (own `pom.xml`). Tests: `ProductRepositoryTest` (`@DataJpaTest`), `ProductControllerTest` (`@WebMvcTest`), `ProductServiceTest` (Mockito), `CatalogPropertiesTest` (`ApplicationContextRunner`).
> Run: `cd e-spring-catalog && mvn test`. Offline H2 only; no Kafka or Docker needed.

# E. Product catalog: paging, projections, configuration properties, parameter validation (35 min)

Complete every `TODO` (annotations, queries, bodies) in this order, running the tests as you go:
1. `Product` - `@Entity`, named unique constraint on `sku`, an index on `(category, name)`, `active` true by default. Test: `ProductRepositoryTest`.
2. `ProductRepository` - a DERIVED paged query, `existsBySku`, a JPQL query returning the `CategoryCount` interface projection, and one atomic `@Modifying` soft delete. Why is a derived `Page` query two SQL statements?
3. `CreateProductRequest` - Bean Validation incl. `@Pattern`. Why `Long`?
4. `CatalogProperties` - a validated `@ConfigurationProperties` record with defaults. Test: `CatalogPropertiesTest`.
5. `ProductService` - `@Service`, transactions, size clamping, duplicate handling (check AND constraint), idempotent deactivate.
6. `ProductController` - `201 + Location`, `@RequestParam` constraints WITHOUT `@Validated` on the class. Which exception does Spring 6.1 raise for them?
7. `ApiExceptionHandler` - `ProblemDetail` for body validation (override the base-class hook), 404 and 409.
8. `application.yml` - answer the questions on paper, then compare with `answers/`.

Say out loud: offset paging vs keyset paging (cost of `OFFSET 100000`, pages that shift while a row is inserted, why the sort needs a unique tie-breaker); why a client-controlled `size` must be clamped; `existsBy` then `save` is a race and the unique constraint is the real guard; what an interface projection selects vs an entity; why the controller never touches the repository.
