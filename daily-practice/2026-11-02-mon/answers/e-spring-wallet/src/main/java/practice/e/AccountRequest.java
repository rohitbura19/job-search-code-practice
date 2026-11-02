package practice.e;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

/** openingBalance may be omitted (treated as 0). */
public record AccountRequest(
        @NotBlank @Size(max = 40) String owner,
        @PositiveOrZero @Digits(integer = 12, fraction = 2) BigDecimal openingBalance) {}
