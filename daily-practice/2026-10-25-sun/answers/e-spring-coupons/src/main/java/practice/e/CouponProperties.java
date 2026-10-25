package practice.e;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "coupon")
@Validated
public record CouponProperties(@DefaultValue("50") @Min(1) @Max(100) int maxPercentOff, @DefaultValue("10000") @Min(1) int maxRedemptionsCap) {}
