package practice.e;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "reviews")
@Validated
public record ReviewProperties(@NotNull List<String> bannedWords, @DefaultValue("20") @Min(1) @Max(100) int maxPageSize) {}
