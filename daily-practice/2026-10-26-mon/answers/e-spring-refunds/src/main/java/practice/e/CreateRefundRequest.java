package practice.e;

import jakarta.validation.constraints.*;

public record CreateRefundRequest(@NotBlank @Size(max = 40) String paymentId,
                                  @NotNull @Positive Long amountPaise,
                                  @Size(max = 200) String reason) {}   // Long: a missing field is null -> @NotNull, not a silent 0
