package practice.e;

import jakarta.validation.constraints.*;

public record CreateProductRequest(@NotBlank @Pattern(regexp = "[A-Z0-9-]{3,20}") String sku,
                                   @NotBlank @Size(max = 80) String name,
                                   @NotBlank @Size(max = 40) String category,
                                   @NotNull @Positive Long priceCents) {}   // Long: a missing price is null -> @NotNull, not a silent 0
