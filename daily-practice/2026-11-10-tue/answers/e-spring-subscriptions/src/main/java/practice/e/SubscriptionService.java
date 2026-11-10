package practice.e;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practice.e.Exceptions.ConflictException;
import practice.e.Exceptions.NotFoundException;

@Service
@Transactional(readOnly = true)
public class SubscriptionService {
    private final SubscriptionRepository subs;
    private final AccountRepository accounts;
    private final BillingProperties billing;
    private final Clock clock;

    public SubscriptionService(SubscriptionRepository subs, AccountRepository accounts, BillingProperties billing, Clock clock) {
        this.subs = subs; this.accounts = accounts; this.billing = billing; this.clock = clock;
    }

    private static String norm(String email) { return email.trim().toLowerCase(Locale.ROOT); }

    private Subscription load(Long id) {
        return subs.findById(id).orElseThrow(() -> new NotFoundException("Subscription " + id + " not found"));
    }

    @Transactional
    public Views.SubscriptionView subscribe(String email, Plan plan) {
        String e = norm(email);
        Instant now = clock.instant();
        Account account = accounts.findByEmail(e).orElseGet(() -> accounts.save(new Account(e, now)));
        if (subs.existsByAccountIdAndStatus(account.getId(), SubscriptionStatus.ACTIVE))
            throw new ConflictException("Account " + e + " already has an active subscription");
        Subscription s = subs.save(new Subscription(account, plan, now, now.plus(billing.periodDays(), ChronoUnit.DAYS)));
        return Views.SubscriptionView.of(s);
    }

    public Views.SubscriptionView get(Long id) { return Views.SubscriptionView.of(load(id)); }

    public List<Views.SubscriptionView> forEmail(String email) {
        return subs.findByAccountEmailOrderByStartedAtDesc(norm(email)).stream().map(Views.SubscriptionView::of).toList();
    }

    @Transactional
    public Views.SubscriptionView changePlan(Long id, Plan plan) {
        Subscription s = load(id);
        if (s.getStatus() != SubscriptionStatus.ACTIVE) throw new ConflictException("Subscription " + id + " is not active");
        if (s.getPlan() != plan) s.setPlan(plan);
        return Views.SubscriptionView.of(s);
    }

    @Transactional
    public void cancel(Long id) {
        Subscription s = load(id);
        if (s.getStatus() == SubscriptionStatus.ACTIVE) s.setStatus(SubscriptionStatus.CANCELLED);
    }

    public List<Views.SubscriptionView> renewalsDue(Duration within) {
        Instant now = clock.instant();
        return subs.findByStatusAndRenewsAtBetweenOrderByRenewsAtAsc(SubscriptionStatus.ACTIVE, now, now.plus(within))
                .stream().map(Views.SubscriptionView::of).toList();
    }

    public Map<Plan, Long> activeByPlan() {
        Map<Plan, Long> m = new EnumMap<>(Plan.class);
        for (Plan p : Plan.values()) m.put(p, 0L);
        for (PlanCount c : subs.countByPlan(SubscriptionStatus.ACTIVE)) m.put(c.getPlan(), c.getTotal());
        return m;
    }
}
