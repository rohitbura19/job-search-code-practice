package practice.e;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateSubscriptionRequest(@NotBlank @Email @Size(max = 120) String email, @NotNull Plan plan) {
    public record ChangePlanRequest(@NotNull Plan plan) {}
}
