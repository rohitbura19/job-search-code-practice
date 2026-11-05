package practice.d20261105.g;

import java.time.Instant;

/**
 * Given. PERCENT: value is a whole percent 1..100 (discount = subtotal * value / 100, rounded down, then capped by maxDiscountCents when > 0).
 * FLAT: value is cents off, never more than the subtotal. Valid in [validFrom, validTo). minSubtotalCents 0 = no minimum.
 * maxRedemptions = total uses across all users (> 0); perUserLimit = uses per user (> 0).
 */
public record Coupon(String code, Type type, long value, long minSubtotalCents, long maxDiscountCents,
                     Instant validFrom, Instant validTo, int maxRedemptions, int perUserLimit) {
    public enum Type { PERCENT, FLAT }
}
