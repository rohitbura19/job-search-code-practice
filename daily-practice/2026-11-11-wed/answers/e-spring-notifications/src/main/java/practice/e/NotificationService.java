package practice.e;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practice.e.Exceptions.InvalidTargetException;
import practice.e.Exceptions.NotFoundException;

@Service
@Transactional(readOnly = true)
public class NotificationService {
    private static final Pattern EMAIL = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    private static final Pattern PHONE = Pattern.compile("^\\+[1-9]\\d{7,14}$");

    private final NotificationRepository repo;
    private final Clock clock;
    private final int staleDays;

    public NotificationService(NotificationRepository repo, Clock clock, @Value("${inbox.stale-days:30}") int staleDays) {
        this.repo = repo; this.clock = clock; this.staleDays = staleDays;
    }

    @Transactional
    public Views.NotificationView send(SendRequest r) {
        Instant now = clock.instant();
        String target = r.target().strip();
        Notification n = switch (r.channel()) {
            case EMAIL -> {
                if (!EMAIL.matcher(target).matches()) throw new InvalidTargetException("Not an e-mail address: " + target);
                yield new EmailNotification(r.userId(), target.toLowerCase(), r.message(), now);
            }
            case SMS -> {
                if (!PHONE.matcher(target).matches()) throw new InvalidTargetException("Not an E.164 phone number: " + target);
                yield new SmsNotification(r.userId(), target, r.message(), now);
            }
        };
        return Views.NotificationView.from(repo.save(n));
    }

    public Views.PageView list(Long userId, boolean unreadOnly, int page, int size) {
        PageRequest pr = PageRequest.of(page, size);
        Page<Notification> p = unreadOnly
                ? repo.findByUserIdAndReadAtIsNullOrderByCreatedAtDescIdDesc(userId, pr)
                : repo.findByUserIdOrderByCreatedAtDescIdDesc(userId, pr);
        return new Views.PageView(p.getContent().stream().map(Views.NotificationView::from).toList(), page, size, p.getTotalElements());
    }

    public long unreadCount(Long userId) { return repo.countByUserIdAndReadAtIsNull(userId); }

    @Transactional
    public Views.NotificationView markRead(Long id, Long userId) {
        Notification n = repo.findByIdAndUserId(id, userId).orElseThrow(() -> new NotFoundException("Notification " + id + " not found"));
        n.markRead(clock.instant());
        return Views.NotificationView.from(n);
    }

    @Transactional
    public int markAllRead(Long userId) { return repo.markAllRead(userId, clock.instant()); }

    public List<Long> staleUnreadEmailIds() {
        return repo.staleUnreadEmails(clock.instant().minus(Duration.ofDays(staleDays))).stream().map(Notification::getId).toList();
    }
}
