package practice.e;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record LineRequest(@NotBlank @Size(max = 32) String sku, @Min(1) @Max(1000) int quantity, @Positive long unitPricePaise) {}
