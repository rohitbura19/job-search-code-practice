package practice.e;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateProfileRequest(
        @NotBlank @Email String email,
        @NotBlank @Size(max = 50) String displayName,
        @NotBlank String city,
        @NotBlank @Size(min = 2, max = 2) String country) {}
