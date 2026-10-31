package practice.e;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.boot.origin.OriginTrackedValue;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;

class NotesPropsTest {
    @EnableConfigurationProperties(NotesProps.class)
    static class Cfg {}

    ApplicationContextRunner runner = new ApplicationContextRunner().withUserConfiguration(Cfg.class);

    @Test void defaultsApplyWhenNothingIsConfigured() {
        runner.run(ctx -> {
            NotesProps p = ctx.getBean(NotesProps.class);
            assertEquals(100, p.maxPerOwner());
            assertEquals(50, p.maxPageSize());
        });
    }

    @Test void relaxedBinding() {
        runner.withPropertyValues("notes.max_per_owner=3", "notes.maxPageSize=7").run(ctx -> {
            assertEquals(3, ctx.getBean(NotesProps.class).maxPerOwner());
            assertEquals(7, ctx.getBean(NotesProps.class).maxPageSize());
        });
    }

    @Test void invalidValuesStopTheContext() {
        runner.withPropertyValues("notes.max-per-owner=0").run(ctx -> assertNotNull(ctx.getStartupFailure()));
        runner.withPropertyValues("notes.max-page-size=-5").run(ctx -> assertNotNull(ctx.getStartupFailure()));
    }

    @Test void applicationYmlHasTheRequiredKeys() throws Exception {
        List<PropertySource<?>> sources = new YamlPropertySourceLoader().load("application", new ClassPathResource("application.yml"));
        assertTrue(!sources.isEmpty(), "application.yml is empty");
        @SuppressWarnings("unchecked") Map<String, Object> m = (Map<String, Object>) sources.get(0).getSource();
        assertEquals(false, val(m, "spring.jpa.open-in-view"));
        assertEquals(true, val(m, "spring.mvc.problemdetails.enabled"));
        assertEquals(true, val(m, "spring.jackson.deserialization.fail-on-unknown-properties"));
        assertEquals(100, val(m, "notes.max-per-owner"));
        assertEquals(50, val(m, "notes.max-page-size"));
    }

    private static Object val(Map<String, Object> m, String key) {
        Object v = m.get(key);
        return v instanceof OriginTrackedValue o ? o.getValue() : v;
    }
}
