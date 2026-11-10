package practice.e;

import java.time.Instant;

public final class Views {
    private Views() {}

    public record SubscriptionView(Long id, String email, Plan plan, SubscriptionStatus status, Instant startedAt, Instant renewsAt, int priceCents) {
        public static SubscriptionView of(Subscription s) {
            return new SubscriptionView(s.getId(), s.getAccount().getEmail(), s.getPlan(), s.getStatus(), s.getStartedAt(), s.getRenewsAt(), s.getPlan().priceCents());
        }
    }
}
