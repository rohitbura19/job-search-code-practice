package practice.e;

import jakarta.validation.constraints.Min;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "notes")
public record NotesProps(@DefaultValue("100") @Min(1) int maxPerOwner, @DefaultValue("50") @Min(1) int maxPageSize) {}
