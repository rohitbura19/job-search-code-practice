# 21. notifications - repository queries

_From daily-practice/2026-11-11-wed. E - Spring Boot: notifications inbox API (30 min) - project `e-spring-notifications/` (find `TODO(E`)_

Write the annotation + query for every TODO (JPQL unless it says native). Answers: `daily-practice/2026-11-11-wed/answers/`.

## Entity `Notification`

```java
    private Long id;
    private Long userId;
    private String message;
    private Instant createdAt;
    private Instant readAt;
```

## `NotificationRepository`

```java
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    // TODO(E2a): derived paged query: a user's notifications, newest first (createdAt desc, then id desc).
    //            Page<Notification> findByUserIdOrderByCreatedAtDescIdDesc(Long userId, Pageable pageable);   <- add it (the tests call this exact name)
    // TODO(E2b): the same but only UNREAD ones (readAt is null). Name: findByUserIdAndReadAtIsNullOrderByCreatedAtDescIdDesc(Long, Pageable) returning Page<Notification>.
    // TODO(E2c): derived count: long countByUserIdAndReadAtIsNull(Long userId);
    // TODO(E2d): scoped lookup so one user cannot read another's row: Optional<Notification> findByIdAndUserId(Long id, Long userId);
    /**
     * TODO(E2e): JPQL bulk update: set readAt = :now for the user's rows where readAt is null; return the number of rows changed.
     *  Flush pending changes before and clear the persistence context after (bulk updates bypass it). Bind parameters with @Param.
     */
    int markAllRead(Long userId, Instant now);
    /**
     * TODO(E2f): JPQL over the hierarchy: unread EMAIL notifications created strictly before :before, oldest first (then id). Use the type() function,
     *  not a native query and not a second repository.
     */
    List<Notification> staleUnreadEmails(Instant before);
}
```
