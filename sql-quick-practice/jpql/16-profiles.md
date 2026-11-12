# 16. profiles - repository queries

_From daily-practice/2026-11-06-fri. E - Spring Boot: profiles API with optimistic locking (30 min)_

Write the annotation + query for every TODO (JPQL unless it says native). Answers: `daily-practice/2026-11-06-fri/answers/`.

## `ProfileRepository`

```java
// TODO(E2): make this a Spring Data repository of Profile (Long ids) that ALSO supports Specification queries.
//  Then DELETE the placeholder defaults below (findAll, saveAndFlush, findById) (the Specification-capable interface provides it) and turn the two methods into
//  derived queries by their names: find by email ignoring case (Optional), exists by email ignoring case.
public interface ProfileRepository {
    default Page<Profile> findAll(Specification<Profile> spec, Pageable pageable) { throw new UnsupportedOperationException("TODO"); }
    default <S extends Profile> S saveAndFlush(S entity) { throw new UnsupportedOperationException("TODO"); }
    default Optional<Profile> findById(Long id) { throw new UnsupportedOperationException("TODO"); }
    Optional<Profile> findByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCase(String email);
}
```
