package practice.e;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.Instant;

public record ReadingRequest(
        @NotBlank @Size(max = 40) String meterId,
        @NotNull @DecimalMin("0") @Digits(integer = 9, fraction = 3) BigDecimal kwh,
        @NotNull @PastOrPresent Instant readAt) {}
