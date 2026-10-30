package practice.e;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ReserveRequest(
        @NotBlank @Pattern(regexp = "[A-Z0-9-]{3,32}", message = "must be 3-32 upper-case letters, digits or dashes") String sku,
        @Min(1) @Max(100) int qty) {}
