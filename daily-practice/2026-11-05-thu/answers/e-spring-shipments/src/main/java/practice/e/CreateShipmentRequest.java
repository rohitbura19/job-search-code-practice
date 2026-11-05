package practice.e;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateShipmentRequest(@NotBlank @Size(max = 40) String orderId, @NotBlank @Size(max = 200) String address) {}
