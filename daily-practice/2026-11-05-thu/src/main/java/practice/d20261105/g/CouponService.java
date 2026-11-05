package practice.d20261105.g;

import java.time.Clock;

/**
 * Coupon engine. Codes are case-insensitive ("save10" == "SAVE10"). Implement thread-safely (many checkouts in parallel).
 * Check order for preview and redeem: UNKNOWN, NOT_STARTED (now < validFrom), EXPIRED (now >= validTo), MIN_NOT_MET (subtotal < minSubtotal),
 * EXHAUSTED (total redemptions == max), USER_LIMIT (this user's redemptions == perUserLimit).
 */
public class CouponService {
    public CouponService(Clock clock) { throw new UnsupportedOperationException("TODO"); }

    /** IllegalArgumentException if the code (case-insensitive) is already registered or maxRedemptions/perUserLimit <= 0. */
    public void register(Coupon coupon) { throw new UnsupportedOperationException("TODO"); }

    /** What the coupon would give now. Consumes nothing. Throws CouponException with the first failing reason. */
    public Discount preview(String code, String userId, Cart cart) { throw new UnsupportedOperationException("TODO"); }

    /**
     * Applies the coupon and consumes one redemption for the user. IDEMPOTENT per orderId: a repeated call with an orderId that already
     * holds a redemption returns the SAME stored Discount and consumes nothing more (even if the coupon is exhausted or expired by now).
     * Under concurrency never more than maxRedemptions / perUserLimit succeed.
     */
    public Discount redeem(String code, String userId, String orderId, Cart cart) { throw new UnsupportedOperationException("TODO"); }

    /** Order cancelled or refunded: gives the redemption back (total and per user). Unknown or already released orderId: no-op. */
    public void release(String orderId) { throw new UnsupportedOperationException("TODO"); }
}
