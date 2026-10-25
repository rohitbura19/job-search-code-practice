package practice.e;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;

class CouponPropertiesTest {
    @Configuration
    @EnableConfigurationProperties(CouponProperties.class)
    static class Config {}

    final ApplicationContextRunner runner = new ApplicationContextRunner().withUserConfiguration(Config.class);

    @Test void defaultsApplyWhenNothingIsConfigured() {
        runner.run(ctx -> {
            CouponProperties p = ctx.getBean(CouponProperties.class);
            assertEquals(50, p.maxPercentOff());
            assertEquals(10_000, p.maxRedemptionsCap());
        });
    }

    @Test void relaxedBindingReadsKebabCase() {
        runner.withPropertyValues("coupon.max-percent-off=30", "coupon.max-redemptions-cap=5").run(ctx -> {
            CouponProperties p = ctx.getBean(CouponProperties.class);
            assertEquals(30, p.maxPercentOff());
            assertEquals(5, p.maxRedemptionsCap());
        });
    }

    @Test void invalidValuesFailTheStartup() {
        runner.withPropertyValues("coupon.max-percent-off=0").run(ctx -> assertNotNull(ctx.getStartupFailure()));
        runner.withPropertyValues("coupon.max-percent-off=101").run(ctx -> assertNotNull(ctx.getStartupFailure()));
        runner.withPropertyValues("coupon.max-redemptions-cap=0").run(ctx -> assertNotNull(ctx.getStartupFailure()));
    }
}
