package practice.e;

import java.util.List;
import java.util.Set;
import org.springframework.http.ResponseEntity;

// TODO: @RestController under /api/orders.
public class OrderController {
    private final OrderService service;
    private final OrderProps props;

    public OrderController(OrderService service, OrderProps props) {
        this.service = service;
        this.props = props;
    }

    // TODO POST /api/orders: validate the body; 201 with Location: /api/orders/{orderNo} and the view as body.
    public ResponseEntity<OrderView> create(CreateOrderRequest request) { throw new UnsupportedOperationException("TODO"); }

    // TODO GET /api/orders/{orderNo}
    public OrderView get(String orderNo) { throw new UnsupportedOperationException("TODO"); }

    // TODO POST /api/orders/{orderNo}/cancel -> 200 with the updated view.
    public OrderView cancel(String orderNo) { throw new UnsupportedOperationException("TODO"); }

    // TODO GET /api/orders?customerId=&status=&page=&size=  customerId required; status optional, repeatable, default none;
    //      page default 0; size default 20 and CLAMPED to 1..props.maxPageSize() (never an error).
    public PagedResponse<OrderSummary> list(String customerId, Set<OrderStatus> status, int page, int size) { throw new UnsupportedOperationException("TODO"); }

    // TODO GET /api/orders/top-customers?n=  n default 5, clamped to 1..20.
    public List<CustomerTotal> top(int n) { throw new UnsupportedOperationException("TODO"); }
}
