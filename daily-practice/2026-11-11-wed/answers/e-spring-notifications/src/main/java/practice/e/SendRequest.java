package practice.e;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record SendRequest(
        @NotNull @Positive Long userId,
        @NotNull Channel channel,
        @NotBlank @Size(max = 200) String target,
        @NotBlank @Size(max = 500) String message) {}
