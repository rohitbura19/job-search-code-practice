package practice.e;

import java.time.Clock;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;

// TODO: make it a Spring bean with read-only transactions by default; writes override that.
public class OrderService {
    private final PurchaseOrderRepository repo;
    private final Clock clock;

    public OrderService(PurchaseOrderRepository repo, Clock clock) {
        this.repo = repo;
        this.clock = clock;
    }

    /** Duplicate orderNo -> DuplicateOrderException. Builds the order with its lines, saves, returns the view. */
    public OrderView create(CreateOrderRequest r) { throw new UnsupportedOperationException("TODO"); }

    /** Unknown orderNo -> OrderNotFoundException. */
    public OrderView get(String orderNo) { throw new UnsupportedOperationException("TODO"); }

    /** Unknown -> OrderNotFoundException; illegal state -> IllegalStateTransitionException. Do NOT call save(): the entity is managed. */
    public OrderView cancel(String orderNo) { throw new UnsupportedOperationException("TODO"); }

    /** Newest first (createdAt desc, id desc). An empty `statuses` set means all statuses. */
    public Page<OrderSummary> list(String customerId, Set<OrderStatus> statuses, int page, int size) { throw new UnsupportedOperationException("TODO"); }

    /** The n biggest customers by non-cancelled spend. */
    public List<CustomerTotal> topCustomers(int n) { throw new UnsupportedOperationException("TODO"); }
}
