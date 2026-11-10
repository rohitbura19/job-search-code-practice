package practice.e;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties("billing")
@Validated
public record BillingProperties(@DefaultValue("30") @Min(1) @Max(366) int periodDays) {}
