package practice.e;

import java.time.Instant;

/**
 * TODO(E1b): make this an entity mapped to table "subscriptions" with a (non-unique) index on columns status, renews_at.
 *  - id: IDENTITY generated.
 *  - account: many subscriptions to one account; fetched LAZILY; never null; join column "account_id".
 *  - plan, status: stored as their NAMES, not null, length 10.
 *  - startedAt, renewsAt: not null Instant columns.
 */
public class Subscription {
    private Long id;
    private Account account;
    private Plan plan;
    private SubscriptionStatus status = SubscriptionStatus.ACTIVE;
    private Instant startedAt;
    private Instant renewsAt;

    protected Subscription() {}
    public Subscription(Account account, Plan plan, Instant startedAt, Instant renewsAt) {
        this.account = account; this.plan = plan; this.startedAt = startedAt; this.renewsAt = renewsAt;
    }

    public Long getId() { return id; }
    public Account getAccount() { return account; }
    public Plan getPlan() { return plan; }
    public SubscriptionStatus getStatus() { return status; }
    public Instant getStartedAt() { return startedAt; }
    public Instant getRenewsAt() { return renewsAt; }
    public void setPlan(Plan plan) { this.plan = plan; }
    public void setStatus(SubscriptionStatus status) { this.status = status; }
}
