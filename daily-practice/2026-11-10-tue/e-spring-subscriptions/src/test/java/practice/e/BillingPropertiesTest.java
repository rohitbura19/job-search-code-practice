package practice.e;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;

class BillingPropertiesTest {
    @Configuration
    @EnableConfigurationProperties(BillingProperties.class)
    static class Cfg {}

    private final ApplicationContextRunner runner = new ApplicationContextRunner().withUserConfiguration(Cfg.class);

    @Test void defaultsTo30Days() {
        runner.run(ctx -> assertThat(ctx.getBean(BillingProperties.class).periodDays()).isEqualTo(30));
    }

    @Test void bindsKebabCaseProperty() {
        runner.withPropertyValues("billing.period-days=7").run(ctx -> assertThat(ctx.getBean(BillingProperties.class).periodDays()).isEqualTo(7));
    }

    @Test void rejectsOutOfRangeValuesAtStartup() {
        runner.withPropertyValues("billing.period-days=0").run(ctx -> assertThat(ctx).hasFailed());
        runner.withPropertyValues("billing.period-days=367").run(ctx -> assertThat(ctx).hasFailed());
        runner.withPropertyValues("billing.period-days=366").run(ctx -> assertThat(ctx).hasNotFailed());
    }
}
