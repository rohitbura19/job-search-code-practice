# 10. notes - repository queries

_From daily-practice/2026-10-31-sat. E. Spring Boot: notes API with soft delete (30 min)_

Write the annotation + query for every TODO (JPQL unless it says native). Answers: `daily-practice/2026-10-31-sat/answers/`.

## Entity `Note`

```java
```

## `NoteRepository`

```java
/**
 * TODO: the derived queries are declared; finish the other two (add annotations/parameter names):
 *   search:  @Query JPQL: owner matches AND (title OR body contains q, case-insensitive)
 *   restore: NATIVE @Modifying update that sets deleted = false (bypasses the soft-delete filter);
 *            the persistence context must be flushed before and cleared after; returns rows changed
 */
public interface NoteRepository extends JpaRepository<Note, Long> {
    long countByOwner(String owner);
    Page<Note> findByOwner(String owner, Pageable pageable);
    Page<Note> search(String owner, String q, Pageable pageable);
    int restore(long id);
}
```
