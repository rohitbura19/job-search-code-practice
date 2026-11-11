package practice.e;

import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

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
