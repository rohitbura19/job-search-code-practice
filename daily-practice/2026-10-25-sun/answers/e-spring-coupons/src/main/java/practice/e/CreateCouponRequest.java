package practice.e;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record CreateCouponRequest(@NotBlank @Pattern(regexp = "[A-Z0-9]{4,30}") String code,
                                  @NotNull @Min(1) @Max(100) Integer percentOff,
                                  @NotNull @Future LocalDate validUntil,
                                  @NotNull @Positive Integer maxRedemptions) {}   // Integer: a missing field is null -> @NotNull, not a silent 0
