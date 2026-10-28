package practice.e;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

class OrderPropsTest {
    @EnableConfigurationProperties(OrderProps.class)
    static class Cfg {}

    ApplicationContextRunner runner = new ApplicationContextRunner().withUserConfiguration(Cfg.class);

    @Test void bindsKebabCaseKey() {
        runner.withPropertyValues("orders.max-page-size=25").run(ctx -> assertEquals(25, ctx.getBean(OrderProps.class).maxPageSize()));
    }

    @Test void bindsCamelAndUnderscoreKeys() {
        runner.withPropertyValues("orders.maxPageSize=7").run(ctx -> assertEquals(7, ctx.getBean(OrderProps.class).maxPageSize()));
        runner.withPropertyValues("orders.max_page_size=9").run(ctx -> assertEquals(9, ctx.getBean(OrderProps.class).maxPageSize()));
    }
}
