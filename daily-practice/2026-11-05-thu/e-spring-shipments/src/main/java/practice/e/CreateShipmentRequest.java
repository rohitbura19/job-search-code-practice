package practice.e;

// TODO: validation annotations on the components:
//  orderId: @NotBlank, @Size(max = 40)      address: @NotBlank, @Size(max = 200)
public record CreateShipmentRequest(String orderId, String address) {}
