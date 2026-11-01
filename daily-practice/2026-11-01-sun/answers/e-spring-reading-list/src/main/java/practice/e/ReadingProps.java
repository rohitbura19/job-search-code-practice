package practice.e;

import jakarta.validation.constraints.Min;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties("reading")
@Validated
public record ReadingProps(@DefaultValue("5") @Min(1) int maxTags, @DefaultValue("50") @Min(1) int maxPageSize) {}
