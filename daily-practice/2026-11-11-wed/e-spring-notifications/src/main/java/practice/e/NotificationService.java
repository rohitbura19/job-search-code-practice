package practice.e;

import java.time.Clock;

/**
 * TODO(E4): make this a Spring service; read-only transactional by default, write methods override. Constructor: (NotificationRepository, Clock,
 *  stale-days injected with @Value("${inbox.stale-days:30}") int).
 *  - send(r): target is stripped. EMAIL: must look like name@host.tld (no spaces, exactly one @, a dot after it) else InvalidTargetException("Not an e-mail address: <target>"),
 *      stored lower-cased -> EmailNotification. SMS: E.164 "+" followed by 8..15 digits, first digit non-zero, else InvalidTargetException("Not an E.164 phone number: <target>")
 *      -> SmsNotification. createdAt = clock.instant(). Return the view of the saved entity.
 *  - list(userId, unreadOnly, page, size): PageRequest.of(page, size) over the right repository method -> Views.PageView(items, page, size, total).
 *  - unreadCount(userId).
 *  - markRead(id, userId): row of that user or NotFoundException("Notification <id> not found") (also when it belongs to someone else); the first read time wins (entity.markRead);
 *      no explicit save call is needed - why?
 *  - markAllRead(userId): number of rows updated, using the bulk query with clock.instant().
 *  - staleUnreadEmailIds(): ids of unread e-mails older than now - staleDays days.
 */
public class NotificationService {
    public NotificationService(NotificationRepository repo, Clock clock, int staleDays) {}

    public Views.NotificationView send(SendRequest r) { throw new UnsupportedOperationException("TODO"); }
    public Views.PageView list(Long userId, boolean unreadOnly, int page, int size) { throw new UnsupportedOperationException("TODO"); }
    public long unreadCount(Long userId) { throw new UnsupportedOperationException("TODO"); }
    public Views.NotificationView markRead(Long id, Long userId) { throw new UnsupportedOperationException("TODO"); }
    public int markAllRead(Long userId) { throw new UnsupportedOperationException("TODO"); }
    public java.util.List<Long> staleUnreadEmailIds() { throw new UnsupportedOperationException("TODO"); }
}
