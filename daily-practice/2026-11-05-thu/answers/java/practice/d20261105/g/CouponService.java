package practice.d20261105.g;

import java.time.Clock;
import java.time.Instant;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Design: a registry of per-coupon state objects. Each State owns its counters and is the lock granularity (synchronized), so different coupons
 * never contend and the check-then-consume sequence is atomic per coupon. orderId -> Redemption is kept in a ConcurrentHashMap (computeIfAbsent
 * would run the consume under the map's bin lock, so the idempotency check happens inside the State lock instead: the order map is only read
 * and written while holding that coupon's lock; a given orderId is expected to be used with one coupon).
 * Money math in long; the discount is bounded by the subtotal. Preview shares the same validation as redeem, so they can never disagree.
 */
public class CouponService {
    private static final class State {
        final Coupon coupon;
        int total;
        final Map<String, Integer> perUser = new HashMap<>();
        State(Coupon c) { coupon = c; }
    }

    private record Redemption(State state, String userId, Discount discount) {}

    private final Clock clock;
    private final Map<String, State> coupons = new ConcurrentHashMap<>();
    private final Map<String, Redemption> orders = new ConcurrentHashMap<>();

    public CouponService(Clock clock) { this.clock = clock; }

    private static String key(String code) { return code.toUpperCase(Locale.ROOT); }

    public void register(Coupon c) {
        if (c.maxRedemptions() <= 0 || c.perUserLimit() <= 0) throw new IllegalArgumentException("limits must be positive");
        if (coupons.putIfAbsent(key(c.code()), new State(c)) != null) throw new IllegalArgumentException("duplicate code " + c.code());
    }

    public Discount preview(String code, String userId, Cart cart) {
        State s = state(code);
        synchronized (s) { return validate(s, userId, cart); }
    }

    public Discount redeem(String code, String userId, String orderId, Cart cart) {
        State s = state(code);
        synchronized (s) {
            Redemption existing = orders.get(orderId);
            if (existing != null) return existing.discount();
            Discount d = validate(s, userId, cart);
            s.total++;
            s.perUser.merge(userId, 1, Integer::sum);
            orders.put(orderId, new Redemption(s, userId, d));
            return d;
        }
    }

    public void release(String orderId) {
        Redemption r = orders.get(orderId);
        if (r == null) return;
        synchronized (r.state()) {
            if (orders.remove(orderId, r)) {
                r.state().total--;
                r.state().perUser.merge(r.userId(), -1, Integer::sum);
            }
        }
    }

    private State state(String code) {
        State s = coupons.get(key(code));
        if (s == null) throw new CouponException(CouponException.Reason.UNKNOWN);
        return s;
    }

    private Discount validate(State s, String userId, Cart cart) {
        Coupon c = s.coupon;
        Instant now = clock.instant();
        long subtotal = cart.subtotalCents();
        if (now.isBefore(c.validFrom())) throw new CouponException(CouponException.Reason.NOT_STARTED);
        if (!now.isBefore(c.validTo())) throw new CouponException(CouponException.Reason.EXPIRED);
        if (subtotal < c.minSubtotalCents()) throw new CouponException(CouponException.Reason.MIN_NOT_MET);
        if (s.total >= c.maxRedemptions()) throw new CouponException(CouponException.Reason.EXHAUSTED);
        if (s.perUser.getOrDefault(userId, 0) >= c.perUserLimit()) throw new CouponException(CouponException.Reason.USER_LIMIT);
        long amount = c.type() == Coupon.Type.PERCENT ? subtotal * c.value() / 100 : Math.min(c.value(), subtotal);
        if (c.type() == Coupon.Type.PERCENT && c.maxDiscountCents() > 0) amount = Math.min(amount, c.maxDiscountCents());
        return new Discount(c.code(), Math.min(amount, subtotal));
    }
}
