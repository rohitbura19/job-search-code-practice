package practice.e;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** GIVEN. */
public record AlertRequest(@NotBlank String siteId,
                           @NotNull Alert.Severity severity,
                           @NotBlank @Size(max = 500) String message) {}
