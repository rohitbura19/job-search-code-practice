package practice.e;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.origin.OriginTrackedValue;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;

class StockPropsTest {
    @EnableConfigurationProperties(StockProps.class)
    static class Cfg {}

    ApplicationContextRunner runner = new ApplicationContextRunner().withUserConfiguration(Cfg.class);

    @Test void defaultsApplyWhenNothingIsConfigured() {
        runner.run(ctx -> {
            StockProps p = ctx.getBean(StockProps.class);
            assertEquals(5, p.lowThreshold());
            assertEquals(50, p.maxPageSize());
            assertEquals("stock.reserved", p.topics().reserved());
            assertEquals("stock.low", p.topics().lowStock());
            assertEquals("stock.restock", p.topics().restock());
        });
    }

    @Test void relaxedBindingForNestedKeys() {
        runner.withPropertyValues("stock.topics.low-stock=alerts", "stock.low_threshold=9", "stock.maxPageSize=12").run(ctx -> {
            StockProps p = ctx.getBean(StockProps.class);
            assertEquals("alerts", p.topics().lowStock());
            assertEquals(9, p.lowThreshold());
            assertEquals(12, p.maxPageSize());
        });
    }

    @Test void invalidValuesStopTheContext() {
        runner.withPropertyValues("stock.max-page-size=0").run(ctx -> assertNotNull(ctx.getStartupFailure()));
        runner.withPropertyValues("stock.low-threshold=-1").run(ctx -> assertNotNull(ctx.getStartupFailure()));
        runner.withPropertyValues("stock.topics.restock= ").run(ctx -> assertNotNull(ctx.getStartupFailure()));
    }

    @Test void applicationYmlHasTheRequiredKeys() throws Exception {
        List<PropertySource<?>> sources = new YamlPropertySourceLoader().load("application", new ClassPathResource("application.yml"));
        assertTrue(!sources.isEmpty(), "application.yml is empty");
        @SuppressWarnings("unchecked") Map<String, Object> m = (Map<String, Object>) sources.get(0).getSource();
        assertEquals(false, val(m, "spring.jpa.open-in-view"));
        assertEquals("org.apache.kafka.common.serialization.StringSerializer", val(m, "spring.kafka.producer.value-serializer"));
        assertEquals("org.apache.kafka.common.serialization.StringDeserializer", val(m, "spring.kafka.consumer.value-deserializer"));
        assertEquals("all", val(m, "spring.kafka.producer.acks"));
        assertEquals("earliest", val(m, "spring.kafka.consumer.auto-offset-reset"));
        assertEquals("stock.restock", val(m, "stock.topics.restock"));
        assertEquals(50, val(m, "stock.max-page-size"));
    }

    private static Object val(Map<String, Object> m, String key) {
        Object v = m.get(key);
        return v instanceof OriginTrackedValue o ? o.getValue() : v;
    }
}
