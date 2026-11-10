package practice.e;

import java.time.Clock;
import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * TODO(E4): make this a Spring service; read-only transactional by default, write methods override.
 *  - subscribe(email, plan): normalise the e-mail (trim + lower-case); reuse the account or create it (createdAt = clock.instant());
 *      if the account already has an ACTIVE subscription -> ConflictException("Account <email> already has an active subscription");
 *      else save ACTIVE, startedAt = now, renewsAt = now + billing.periodDays() days; return the view.
 *  - get(id): view or NotFoundException("Subscription <id> not found").
 *  - forEmail(email): views of that (normalised) e-mail, newest first, empty list if unknown.
 *  - changePlan(id, plan): only ACTIVE (else ConflictException("Subscription <id> is not active")); same plan = no-op returning the view.
 *  - cancel(id): ACTIVE -> CANCELLED; already CANCELLED = no-op (idempotent DELETE); unknown -> NotFoundException.
 *  - renewalsDue(within): ACTIVE subscriptions renewing between now and now + within (inclusive), earliest first.
 *  - activeByPlan(): Map with EVERY Plan as key (EnumMap), count of ACTIVE subscriptions, 0 when none.
 */
public class SubscriptionService {
    public SubscriptionService(SubscriptionRepository subs, AccountRepository accounts, BillingProperties billing, Clock clock) {}

    public Views.SubscriptionView subscribe(String email, Plan plan) { throw new UnsupportedOperationException("TODO"); }
    public Views.SubscriptionView get(Long id) { throw new UnsupportedOperationException("TODO"); }
    public List<Views.SubscriptionView> forEmail(String email) { throw new UnsupportedOperationException("TODO"); }
    public Views.SubscriptionView changePlan(Long id, Plan plan) { throw new UnsupportedOperationException("TODO"); }
    public void cancel(Long id) { throw new UnsupportedOperationException("TODO"); }
    public List<Views.SubscriptionView> renewalsDue(Duration within) { throw new UnsupportedOperationException("TODO"); }
    public Map<Plan, Long> activeByPlan() { throw new UnsupportedOperationException("TODO"); }
}
