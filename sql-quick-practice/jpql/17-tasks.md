# 17. tasks - repository queries

_From daily-practice/2026-11-07-sat. E - Spring Boot: tasks API (30 min) - project `e-spring-tasks/` (find `TODO(E`)_

Write the annotation + query for every TODO (JPQL unless it says native). Answers: `daily-practice/2026-11-07-sat/answers/`.

## Entity `Task`

```java
@Entity
    @Id
    private Long id; // TODO(E1): generation strategy
    private String title;
    private TaskStatus status = TaskStatus.TODO;
    private Instant createdAt;
    private Instant updatedAt;
    private List<Comment> comments = new ArrayList<>();
```

## `TaskRepository`

```java
public interface TaskRepository extends JpaRepository<Task, Long> {
    /** TODO(E3): ONE query that loads the task together with its comments (JPQL with a fetch join; keep tasks without comments). */
    Optional<Task> findWithComments(Long id);
    /** TODO(E3): derived query - tasks with that status, paged and sorted by the Pageable. */
    Page<Task> findByStatus(TaskStatus status, Pageable pageable);
    /** TODO(E3): JPQL group by status; the aliases must be "status" and "total" so the StatusCount projection works. */
    List<StatusCount> countByStatus();
}
```
