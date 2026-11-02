package practice.e;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;

class WalletPropsTest {
    @Configuration
    @EnableConfigurationProperties(WalletProps.class)
    static class Cfg {}

    final ApplicationContextRunner runner = new ApplicationContextRunner().withUserConfiguration(Cfg.class);

    @Test void defaultsToTenThousand() {
        runner.run(ctx -> assertThat(ctx.getBean(WalletProps.class).maxTransfer()).isEqualByComparingTo("10000.00"));
    }

    @Test void bindsKebabCaseProperty() {
        runner.withPropertyValues("wallet.max-transfer=250.50")
                .run(ctx -> assertThat(ctx.getBean(WalletProps.class).maxTransfer()).isEqualByComparingTo("250.50"));
    }

    @Test void rejectsNonPositiveLimit() {
        runner.withPropertyValues("wallet.max-transfer=0").run(ctx -> assertThat(ctx).hasFailed());
    }
}
