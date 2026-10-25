package practice.e;

import java.time.LocalDate;

public record CouponView(String code, int percentOff, LocalDate validUntil, int redemptionsLeft) {
    static CouponView of(Coupon c) { return new CouponView(c.getCode(), c.getPercentOff(), c.getValidUntil(), c.getMaxRedemptions() - c.getRedemptions()); }
}
