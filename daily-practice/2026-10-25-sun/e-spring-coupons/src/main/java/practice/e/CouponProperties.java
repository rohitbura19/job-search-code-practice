package practice.e;

// TODO: bind `coupon.*` to this record: a @ConfigurationProperties(prefix = "coupon") that is validated at startup.
//       maxPercentOff defaults to 50 and must be in 1..100; codeLength is NOT part of this record (do not add it).
//       maxRedemptionsCap defaults to 10000 and must be >= 1 (an upper bound for CreateCouponRequest.maxRedemptions enforced in the service).
public record CouponProperties(int maxPercentOff, int maxRedemptionsCap) {}
