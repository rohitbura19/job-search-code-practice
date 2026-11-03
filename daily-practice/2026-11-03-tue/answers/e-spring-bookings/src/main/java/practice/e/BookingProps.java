package practice.e;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

/** Constructor binding is implicit for a single-constructor record; @Validated makes a bad value fail the startup. */
@ConfigurationProperties("booking")
@Validated
public record BookingProps(
        @DefaultValue("PT4H") @NotNull Duration maxDuration,
        @DefaultValue("50") @Min(1) @Max(200) int maxPageSize) {
}
