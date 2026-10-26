package practice.e;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;

class RefundPropertiesTest {
    @Configuration
    @EnableConfigurationProperties(RefundProperties.class)
    static class Config {}

    final ApplicationContextRunner runner = new ApplicationContextRunner().withUserConfiguration(Config.class);

    @Test void defaultsApplyWhenNothingIsConfigured() {
        runner.run(ctx -> {
            RefundProperties p = ctx.getBean(RefundProperties.class);
            assertEquals(5_000_000, p.maxRefundPaise());
            assertEquals(100_000, p.autoApproveUpToPaise());
        });
    }

    @Test void relaxedBindingReadsKebabCase() {
        runner.withPropertyValues("refund.max-refund-paise=900", "refund.auto-approve-up-to-paise=0").run(ctx -> {
            RefundProperties p = ctx.getBean(RefundProperties.class);
            assertEquals(900, p.maxRefundPaise());
            assertEquals(0, p.autoApproveUpToPaise());
        });
    }

    @Test void invalidValuesFailTheStartup() {
        runner.withPropertyValues("refund.max-refund-paise=0").run(ctx -> assertNotNull(ctx.getStartupFailure()));
        runner.withPropertyValues("refund.auto-approve-up-to-paise=-1").run(ctx -> assertNotNull(ctx.getStartupFailure()));
    }

    @Test void thresholdAboveTheMaximumFailsTheStartup() {
        runner.withPropertyValues("refund.max-refund-paise=1000", "refund.auto-approve-up-to-paise=1001").run(ctx -> assertNotNull(ctx.getStartupFailure()));
        runner.withPropertyValues("refund.max-refund-paise=1000", "refund.auto-approve-up-to-paise=1000").run(ctx -> assertNull(ctx.getStartupFailure()));
    }
}
