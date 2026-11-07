# E - Spring Boot: tasks API (30 min) - project `e-spring-tasks/` (find `TODO(E`)

Complete the gaps (Boot 3.3, Java 21, H2). Read the Javadoc above each gap.

| Gap | File | Topic |
|---|---|---|
| E1 | `Task.java` | IDENTITY id, `@Enumerated(STRING)`, JPA auditing (`@CreatedDate`, `@LastModifiedDate`, `@EntityListeners`), `@OneToMany(mappedBy, cascade, orphanRemoval)` + `@OrderBy`, helper methods keeping both sides in sync |
| E2 | `Comment.java` | `@ManyToOne(LAZY, optional=false)` + `@JoinColumn`, column constraints |
| E3 | `TaskRepository.java` | fetch-join `@Query`, derived paged query, JPQL group-by with an interface projection |
| E4 | `TaskService.java` | `@Transactional` (class + `readOnly`), sort whitelist, business rules, `@ConfigurationProperties` record (`TaskProperties`, given) |
| E5 | `TaskController.java` | mappings, `@Valid`, `@Positive/@Min/@Max` on parameters, 201 + Location, `X-Total-Count`, 204 |
| E6 | `ApiExceptionHandler.java` | `ResponseEntityExceptionHandler` + `ProblemDetail` for 404 / 409 / 400 |

`application.yml` (given): `open-in-view: false` (so lazy loading outside the service fails loudly) and `app.tasks.max-comments-per-task: 3`.
Tests: `TaskRepositoryTest` (`@DataJpaTest`), `TaskServiceTest` (`@DataJpaTest` + imported service), `TaskControllerTest` (`@WebMvcTest`, mocked service). Run: `cd e-spring-tasks && mvn test`. Do the entities and repository first; the service tests need them.
