package practice.e;

import java.time.Clock;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Class-level readOnly=true (no dirty checking, may route to a replica); writes opt in. cancel() has no save(): the entity is managed
 * and dirty-checked at commit, and findByOrderNo already loaded the lines for the view. The existsByOrderNo pre-check gives the
 * friendly 409; the unique constraint is the real guard (the handler maps its violation to 409 too).
 */
@Service
@Transactional(readOnly = true)
public class OrderService {
    private final PurchaseOrderRepository repo;
    private final Clock clock;

    public OrderService(PurchaseOrderRepository repo, Clock clock) {
        this.repo = repo;
        this.clock = clock;
    }

    @Transactional
    public OrderView create(CreateOrderRequest r) {
        if (repo.existsByOrderNo(r.orderNo())) throw new DuplicateOrderException(r.orderNo());
        PurchaseOrder o = new PurchaseOrder(r.orderNo(), r.customerId(), clock.instant());
        r.lines().forEach(l -> o.addLine(new OrderLine(l.sku(), l.quantity(), l.unitPricePaise())));
        return OrderView.from(repo.save(o));
    }

    public OrderView get(String orderNo) {
        return repo.findByOrderNo(orderNo).map(OrderView::from).orElseThrow(() -> new OrderNotFoundException(orderNo));
    }

    @Transactional
    public OrderView cancel(String orderNo) {
        PurchaseOrder o = repo.findByOrderNo(orderNo).orElseThrow(() -> new OrderNotFoundException(orderNo));
        o.cancel();
        return OrderView.from(o);
    }

    public Page<OrderSummary> list(String customerId, Set<OrderStatus> statuses, int page, int size) {
        Set<OrderStatus> wanted = statuses.isEmpty() ? EnumSet.allOf(OrderStatus.class) : statuses;
        Pageable p = PageRequest.of(page, size, Sort.by(Sort.Order.desc("createdAt"), Sort.Order.desc("id")));
        return repo.findByCustomerIdAndStatusIn(customerId, wanted, p).map(OrderSummary::from);
    }

    public List<CustomerTotal> topCustomers(int n) {
        return repo.topCustomers(PageRequest.of(0, n));
    }
}
