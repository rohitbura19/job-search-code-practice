# E. Spring Boot: notes API with soft delete (30 min)

Project: `e-spring-notes/` (own `pom.xml`, Spring Boot 3.3, Java 21, H2 for tests; no Kafka this time). Files with TODOs: `Note`, `NoteRepository`, `NoteRequest`, `NoteUpdate`, `NotesProps`, `NoteService`, `NoteController`, `ApiExceptionHandler`, `application.yml`. Given: exceptions, `NoteView`, `PagedResponse`, `NotesApplication`.

Behaviour:
- Entity `Note`: generated id, unique `(owner, title)`, `@Version`, `createdAt` set by Hibernate, **soft delete** with `@SQLDelete` + `@SQLRestriction` (no hand-written `where deleted = false`).
- Repository: derived count and page queries, a JPQL case-insensitive `search` over title or body, and a native `restore` that bypasses the filter.
- Service: per-owner quota (409), optimistic-lock token in the PUT body compared to the entity version (409), size capped by `notes.max-page-size`, transactions (writes read-write, reads read-only).
- Controller: `POST` 201 + `Location`, `GET /{id}`, `GET ?owner&q&page&size`, `PUT /{id}`, `DELETE /{id}` 204, `POST /{id}/restore`. Bad input and malformed JSON are `application/problem+json` 400s; unknown JSON properties are rejected.
- `NotesProps`: immutable validated `@ConfigurationProperties` record with defaults.

Tests: `NoteRepositoryTest` (`@DataJpaTest`), `NoteControllerTest` (`@WebMvcTest`), `NoteServiceTest` (Mockito), `NotesPropsTest` (`ApplicationContextRunner` + yml check).
Run: `cd e-spring-notes && mvn test`. Reference: `answers/e-spring-notes/src/`.
Paper: why does a soft-deleted title still block reuse, and how do you allow it? (partial unique index, or a `deleted_at` column in the key) What does `@SQLRestriction` NOT cover?
