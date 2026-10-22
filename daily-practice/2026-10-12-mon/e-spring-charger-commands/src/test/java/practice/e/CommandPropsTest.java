package practice.e;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** E - binds commands.topic and commands.default-ttl into the immutable CommandProps record (no web server, no Kafka). */
class CommandPropsTest {

    @Configuration
    @EnableConfigurationProperties(CommandProps.class)
    static class Cfg { }

    @Test @DisplayName("commands.topic and commands.default-ttl (ISO-8601) bind into the record")
    void binds() {
        new ApplicationContextRunner()
                .withUserConfiguration(Cfg.class)
                .withPropertyValues("commands.topic=charger-commands", "commands.default-ttl=PT5M")
                .run(ctx -> {
                    CommandProps p = ctx.getBean(CommandProps.class);
                    assertEquals("charger-commands", p.topic());
                    assertEquals(Duration.ofMinutes(5), p.defaultTtl());
                });
    }

    @Test @DisplayName("a different duration binds too (relaxed binding of the kebab-case key)")
    void bindsOtherValues() {
        new ApplicationContextRunner()
                .withUserConfiguration(Cfg.class)
                .withPropertyValues("commands.topic=t2", "commands.default-ttl=PT90S")
                .run(ctx -> {
                    CommandProps p = ctx.getBean(CommandProps.class);
                    assertEquals("t2", p.topic());
                    assertEquals(Duration.ofSeconds(90), p.defaultTtl());
                });
    }
}
