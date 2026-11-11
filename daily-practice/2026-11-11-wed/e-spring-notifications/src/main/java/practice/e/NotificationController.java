package practice.e;

import java.util.Map;
import org.springframework.http.ResponseEntity;

/**
 * TODO(E5): REST controller under /api/notifications. Do NOT put @Validated on the class (Spring 6.1 method validation is built in).
 *  POST   /api/notifications                @Valid body SendRequest -> 201 + Location /api/notifications/{id} + view
 *  GET    /api/notifications                required header X-User-Id (positive); params unreadOnly (default false), page (default 0, >= 0), size (default 20, 1..100) -> PageView
 *  GET    /api/notifications/unread-count   header X-User-Id -> {"unread": n}
 *  PATCH  /api/notifications/{id}/read      id positive, header X-User-Id -> 200 + view
 *  POST   /api/notifications/read-all       header X-User-Id -> {"updated": n}
 *  A missing or non-positive X-User-Id must give 400 (the exception handler of E6 does that for free).
 */
public class NotificationController {
    public NotificationController(NotificationService service) {}

    public ResponseEntity<Views.NotificationView> send(SendRequest r) { throw new UnsupportedOperationException("TODO"); }
    public Views.PageView list(Long userId, boolean unreadOnly, int page, int size) { throw new UnsupportedOperationException("TODO"); }
    public Map<String, Long> unreadCount(Long userId) { throw new UnsupportedOperationException("TODO"); }
    public Views.NotificationView markRead(Long id, Long userId) { throw new UnsupportedOperationException("TODO"); }
    public Map<String, Integer> readAll(Long userId) { throw new UnsupportedOperationException("TODO"); }
}
