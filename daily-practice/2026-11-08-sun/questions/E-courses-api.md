# E - Spring Boot: courses API (30 min) - project `e-spring-courses/` (find `TODO(E`)

Boot 3.3, Java 21, H2. Read the Javadoc above each gap. `application.yml` (given): `open-in-view: false`. A `Clock` bean is injected so tests freeze "today" (2026-11-08).

| Gap | File | Topic |
|---|---|---|
| E1 | `Schedule.java`, `Course.java` | `@Embeddable` record + `@Embedded`; IDENTITY id, unique title; unidirectional `@ManyToMany` with an explicit `@JoinTable` on a `Set` |
| E2 | `Student.java` | entity, unique email |
| E3 | `CourseRepository.java` | fetch-join `@Query`, interface projection with `size()`, join query, derived paged query through an embedded property, derived `existsBy...IgnoreCase` |
| E4 | `CreateCourseRequest.java` | Bean Validation incl. a cross-field `@AssertTrue` |
| E5 | `CourseService.java` | `@Service`, class-level `@Transactional(readOnly)` + write overrides, idempotent enroll, capacity/ended rules, PATCH semantics, `Clock` |
| E6 | `CourseController.java` | mappings, `@Valid`, `@Positive/@Min/@Max` on parameters, 201 + Location, 204, `X-Total-Count`, literal path vs `{id}` |
| E7 | `ApiExceptionHandler.java` | `ResponseEntityExceptionHandler` + `ProblemDetail` for 404 / 409 |

Tests: `CourseRepositoryTest` (`@DataJpaTest`), `CourseServiceTest` (`@DataJpaTest` + `@Import` service + fixed clock), `CourseControllerTest` (`@WebMvcTest`, `@MockBean` service). Run: `cd e-spring-courses && mvn test`. Entities and repository first; the service tests need them.
