package practice.e;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;

class BookingPropsTest {
    @Configuration
    @EnableConfigurationProperties(BookingProps.class)
    static class Cfg {}

    final ApplicationContextRunner runner = new ApplicationContextRunner().withUserConfiguration(Cfg.class);

    @Test void defaultsApplyWhenNothingIsConfigured() {
        runner.run(ctx -> {
            BookingProps p = ctx.getBean(BookingProps.class);
            assertThat(p.maxDuration()).isEqualTo(Duration.ofHours(4));
            assertThat(p.maxPageSize()).isEqualTo(50);
        });
    }

    @Test void valuesAreBoundFromProperties() {
        runner.withPropertyValues("booking.max-duration=PT90M", "booking.max-page-size=200").run(ctx -> {
            BookingProps p = ctx.getBean(BookingProps.class);
            assertThat(p.maxDuration()).isEqualTo(Duration.ofMinutes(90));
            assertThat(p.maxPageSize()).isEqualTo(200);
        });
    }

    @Test void outOfRangePageSizeFailsTheStartup() {
        runner.withPropertyValues("booking.max-page-size=0").run(ctx -> assertThat(ctx).hasFailed());
        runner.withPropertyValues("booking.max-page-size=201").run(ctx -> assertThat(ctx).hasFailed());
    }
}
