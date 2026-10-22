package practice.d20261011.g;

import java.time.Clock;

/** The orchestrator; depends only on interfaces. See questions/G-notification-lld.md. Tests: NotificationServiceTest. */
public class NotificationService {

    public NotificationService(DedupeStore dedupe, PreferenceService prefs, TemplateEngine templates, SenderRegistry registry,
                               RecipientRepository recipients, Dispatcher dispatcher, Scheduler scheduler, Clock clock) {
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Skip a duplicate (key eventId + ":" + userId). For each allowed channel: a non-URGENT request while the user is in quiet hours
     * is deferred via scheduler.defer(req, channel) and NOT sent; otherwise render, load the recipient, dispatchWithRetry through registry.get(channel).
     */
    public void handle(NotificationRequest req) {
        throw new UnsupportedOperationException("TODO");
    }
}
