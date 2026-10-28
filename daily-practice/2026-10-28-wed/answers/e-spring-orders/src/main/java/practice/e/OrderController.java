package practice.e;

import java.net.URI;
import java.util.List;
import java.util.Set;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

/** Clamping (not rejecting) an oversized ?size keeps old clients working and still protects the database. */
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService service;
    private final OrderProps props;

    public OrderController(OrderService service, OrderProps props) {
        this.service = service;
        this.props = props;
    }

    @PostMapping
    public ResponseEntity<OrderView> create(@Valid @RequestBody CreateOrderRequest request) {
        OrderView view = service.create(request);
        return ResponseEntity.created(URI.create("/api/orders/" + view.orderNo())).body(view);
    }

    @GetMapping("/{orderNo}")
    public OrderView get(@PathVariable String orderNo) { return service.get(orderNo); }

    @PostMapping("/{orderNo}/cancel")
    public OrderView cancel(@PathVariable String orderNo) { return service.cancel(orderNo); }

    @GetMapping
    public PagedResponse<OrderSummary> list(@RequestParam String customerId,
                                            @RequestParam(defaultValue = "") Set<OrderStatus> status,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "20") int size) {
        int clamped = Math.min(Math.max(size, 1), props.maxPageSize());
        return PagedResponse.of(service.list(customerId, status, Math.max(page, 0), clamped));
    }

    @GetMapping("/top-customers")
    public List<CustomerTotal> top(@RequestParam(defaultValue = "5") int n) {
        return service.topCustomers(Math.min(Math.max(n, 1), 20));
    }
}
