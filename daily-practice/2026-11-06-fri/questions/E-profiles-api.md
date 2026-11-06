# E - Spring Boot: profiles API with optimistic locking (30 min)
Project: `e-spring-profiles/` (Spring Boot 3.3, JPA/H2, validation). Search `TODO(E` in `src/main` - seven gaps:
E1 `Profile` entity (`@Entity`, `@Version`, unique email, `@Embedded` address), E2 repository (+ `JpaSpecificationExecutor`, derived queries), E3 `ProfileSpecs`, E4 validation annotations on the two request records, E5 `ProfileService` (transactions, lost-update check, duplicate handling), E6 `ProfileController` (REST mapping, 201 + Location + ETag, `If-Match` -> 428/412, `Pageable`), E7 `application.yml` page-size properties.
`ApiExceptionHandler`, exceptions, view and application class are given. Tests: `mvn test` inside the folder (`@DataJpaTest`, Mockito service test, `@WebMvcTest`).
Be ready to explain: why `@Version` + `If-Match` prevents lost updates, why 412 vs 409, and why the service check AND the column are both needed.
