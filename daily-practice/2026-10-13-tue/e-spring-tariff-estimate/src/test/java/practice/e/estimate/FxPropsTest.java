package practice.e.estimate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** E - immutable, validated configuration properties. */
class FxPropsTest {

    @Configuration
    @EnableConfigurationProperties(FxProps.class)
    static class Cfg {}

    final ApplicationContextRunner runner = new ApplicationContextRunner().withUserConfiguration(Cfg.class);

    @Test @DisplayName("annotated with prefix \"fx\" and @Validated")
    void annotations() {
        ConfigurationProperties cp = FxProps.class.getAnnotation(ConfigurationProperties.class);
        assertNotNull(cp, "@ConfigurationProperties missing");
        assertEquals("fx", cp.prefix().isEmpty() ? cp.value() : cp.prefix());
        assertTrue(FxProps.class.isAnnotationPresent(Validated.class), "@Validated missing");
    }

    @Test @DisplayName("binds fx.base-url and fx.timeout (2s) through the record constructor")
    void binds() {
        runner.withPropertyValues("fx.base-url=https://fx.example.com", "fx.timeout=2s").run(ctx -> {
            assertNull(ctx.getStartupFailure());
            FxProps p = ctx.getBean(FxProps.class);
            assertEquals("https://fx.example.com", p.baseUrl());
            assertEquals(Duration.ofSeconds(2), p.timeout());
        });
    }

    @Test @DisplayName("a missing timeout stops the application at startup")
    void missingTimeout() {
        runner.withPropertyValues("fx.base-url=https://fx.example.com").run(ctx -> assertNotNull(ctx.getStartupFailure()));
    }

    @Test @DisplayName("a blank base url stops the application at startup")
    void blankUrl() {
        runner.withPropertyValues("fx.base-url=", "fx.timeout=2s").run(ctx -> assertNotNull(ctx.getStartupFailure()));
    }

    @Test @DisplayName("constraints seen by the validator: baseUrl not blank, timeout not null")
    void constraints() {
        Validator v = Validation.buildDefaultValidatorFactory().getValidator();
        assertEquals(0, v.validate(new FxProps("https://fx.example.com", Duration.ofSeconds(2))).size());
        Set<ConstraintViolation<FxProps>> blank = v.validate(new FxProps(" ", Duration.ofSeconds(2)));
        assertEquals(1, blank.size());
        assertEquals("baseUrl", blank.iterator().next().getPropertyPath().toString());
        Set<ConstraintViolation<FxProps>> noTimeout = v.validate(new FxProps("https://fx.example.com", null));
        assertEquals(1, noTimeout.size());
        assertEquals("timeout", noTimeout.iterator().next().getPropertyPath().toString());
    }
}
