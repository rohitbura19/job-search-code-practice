package practice.e;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;

class ReviewPropertiesTest {
    @Configuration
    @EnableConfigurationProperties(ReviewProperties.class)
    static class Cfg {}

    private final ApplicationContextRunner runner = new ApplicationContextRunner().withUserConfiguration(Cfg.class);

    @Test void bindsListAndAppliesDefault() {
        runner.withPropertyValues("reviews.banned-words=a,b").run(ctx -> {
            ReviewProperties p = ctx.getBean(ReviewProperties.class);
            assertEquals(java.util.List.of("a", "b"), p.bannedWords());
            assertEquals(20, p.maxPageSize());
        });
    }
    @Test void bindsExplicitSize() {
        runner.withPropertyValues("reviews.banned-words=a", "reviews.max-page-size=50").run(ctx ->
                assertEquals(50, ctx.getBean(ReviewProperties.class).maxPageSize()));
    }
    @Test void outOfRangeSizeFailsStartup() {
        runner.withPropertyValues("reviews.banned-words=a", "reviews.max-page-size=0").run(ctx -> assertNotNull(ctx.getStartupFailure()));
        runner.withPropertyValues("reviews.banned-words=a", "reviews.max-page-size=101").run(ctx -> assertNotNull(ctx.getStartupFailure()));
    }
}
