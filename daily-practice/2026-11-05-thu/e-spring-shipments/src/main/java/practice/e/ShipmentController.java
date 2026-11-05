package practice.e;

// TODO: @RestController @RequestMapping("/api/shipments").
//  POST   ""      : @Valid @RequestBody CreateShipmentRequest -> 201 Created with Location /api/shipments/{id} and the ShipmentView body.
//  GET    "/{id}" : ShipmentView (404 comes from the exception handler).
//  GET    ""      : @RequestParam(required = false) ShipmentStatus status, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size -> ShipmentPage.
public class ShipmentController {
    public ShipmentController(ShipmentService service) { throw new UnsupportedOperationException("TODO"); }

    public org.springframework.http.ResponseEntity<ShipmentView> create(CreateShipmentRequest request) { throw new UnsupportedOperationException("TODO"); }

    public ShipmentView get(Long id) { throw new UnsupportedOperationException("TODO"); }

    public ShipmentPage search(ShipmentStatus status, int page, int size) { throw new UnsupportedOperationException("TODO"); }
}
