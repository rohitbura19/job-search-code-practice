package practice.e;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;

/** `List<@Valid LineRequest>` cascades validation into each element; without @Valid the lines would never be checked. */
public record CreateOrderRequest(
        @NotBlank @Pattern(regexp = "ORD-\\d{6}") String orderNo,
        @NotBlank @Size(max = 40) String customerId,
        @NotEmpty @Size(max = 50) List<@Valid LineRequest> lines) {}
