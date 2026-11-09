package practice.e;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;

public record CreateInvoiceRequest(
        @NotBlank @Size(max = 40) String customer,
        @NotEmpty @Size(max = 50) List<@Valid Item> items) {
    public record Item(
            @NotBlank @Size(max = 100) String description,
            @Min(1) @Max(1000) int quantity,
            @Positive long unitPriceCents) {
        public LineItem toLineItem() { return new LineItem(description, quantity, unitPriceCents); }
    }
}
