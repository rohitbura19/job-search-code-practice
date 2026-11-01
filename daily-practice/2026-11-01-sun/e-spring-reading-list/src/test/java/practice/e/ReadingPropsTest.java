package practice.e;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

class ReadingPropsTest {
    @Configuration @EnableConfigurationProperties(ReadingProps.class)
    static class Cfg {}

    final ApplicationContextRunner runner = new ApplicationContextRunner().withUserConfiguration(Cfg.class);

    @Test void defaultsApplyWhenNothingIsConfigured() {
        runner.run(ctx -> {
            ReadingProps p = ctx.getBean(ReadingProps.class);
            assertThat(p.maxTags()).isEqualTo(5);
            assertThat(p.maxPageSize()).isEqualTo(50);
        });
    }

    @Test void valuesCanBeOverridden() {
        runner.withPropertyValues("reading.max-tags=3", "reading.max-page-size=10").run(ctx -> {
            assertThat(ctx.getBean(ReadingProps.class)).isEqualTo(new ReadingProps(3, 10));
        });
    }

    @Test void invalidValuesStopTheContext() {
        runner.withPropertyValues("reading.max-tags=0").run(ctx -> assertThat(ctx).hasFailed());
        runner.withPropertyValues("reading.max-page-size=-1").run(ctx -> assertThat(ctx).hasFailed());
    }

    @Test void applicationYmlTurnsOpenInViewOffAndSetsTheLimits() {
        YamlPropertiesFactoryBean y = new YamlPropertiesFactoryBean();
        y.setResources(new ClassPathResource("application.yml"));
        var props = y.getObject();
        assertThat(props.getProperty("spring.jpa.open-in-view")).isEqualTo("false");
        assertThat(props.getProperty("reading.max-tags")).isEqualTo("5");
        assertThat(props.getProperty("reading.max-page-size")).isEqualTo("50");
    }
}
