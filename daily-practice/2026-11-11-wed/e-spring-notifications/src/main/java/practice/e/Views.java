package practice.e;

import java.time.Instant;
import java.util.List;

/** Given. Uses Notification.channel(), target() and isRead() from your entity. */
public final class Views {
    private Views() {}

    public record NotificationView(Long id, Long userId, Channel channel, String target, String message, Instant createdAt, boolean read) {
        public static NotificationView from(Notification n) {
            return new NotificationView(n.getId(), n.getUserId(), n.channel(), n.target(), n.getMessage(), n.getCreatedAt(), n.isRead());
        }
    }

    public record PageView(List<NotificationView> items, int page, int size, long total) {}
}
