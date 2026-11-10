package practice.e;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "subscriptions", indexes = @Index(name = "ix_sub_status_renews", columnList = "status, renews_at"))
public class Subscription {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 10)
    private Plan plan;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 10)
    private SubscriptionStatus status = SubscriptionStatus.ACTIVE;
    @Column(nullable = false)
    private Instant startedAt;
    @Column(nullable = false)
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
