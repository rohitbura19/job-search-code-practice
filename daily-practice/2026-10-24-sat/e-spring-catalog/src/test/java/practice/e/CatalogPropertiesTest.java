package practice.e;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;

class CatalogPropertiesTest {
    @Configuration
    @EnableConfigurationProperties(CatalogProperties.class)
    static class Config {}

    final ApplicationContextRunner runner = new ApplicationContextRunner().withUserConfiguration(Config.class);

    @Test void defaultsApplyWhenNothingIsConfigured() {
        runner.run(ctx -> {
            CatalogProperties p = ctx.getBean(CatalogProperties.class);
            assertEquals(20, p.defaultPageSize());
            assertEquals(100, p.maxPageSize());
        });
    }

    @Test void relaxedBindingReadsKebabCase() {
        runner.withPropertyValues("catalog.default-page-size=5", "catalog.max-page-size=50").run(ctx -> {
            CatalogProperties p = ctx.getBean(CatalogProperties.class);
            assertEquals(5, p.defaultPageSize());
            assertEquals(50, p.maxPageSize());
        });
    }

    @Test void invalidValuesFailTheStartup() {
        runner.withPropertyValues("catalog.max-page-size=0").run(ctx -> assertNotNull(ctx.getStartupFailure()));
    }
}
