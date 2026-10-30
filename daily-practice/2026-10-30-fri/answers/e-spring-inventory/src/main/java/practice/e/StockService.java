package practice.e;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StockService {
    private final StockRepository repo;
    private final StockEventPublisher events;
    private final StockProps props;

    public StockService(StockRepository repo, StockEventPublisher events, StockProps props) {
        this.repo = repo; this.events = events; this.props = props;
    }

    @Transactional
    public StockView reserve(String sku, int qty) {
        if (repo.reserve(sku, qty) == 0) {
            throw repo.findBySku(sku).isPresent() ? new OutOfStockException(sku, qty) : new StockNotFoundException(sku);
        }
        StockItem item = repo.findBySku(sku).orElseThrow();
        events.reserved(sku, qty);
        if (item.available() <= props.lowThreshold()) events.lowStock(sku, item.available());
        return StockView.from(item);
    }

    @Transactional
    public void restock(String sku, int qty) {
        if (repo.restock(sku, qty) == 0) throw new StockNotFoundException(sku);
    }

    @Transactional(readOnly = true)
    public StockView get(String sku) {
        return repo.findBySku(sku).map(StockView::from).orElseThrow(() -> new StockNotFoundException(sku));
    }

    @Transactional(readOnly = true)
    public Page<StockView> lowStock(int threshold, Pageable pageable) {
        return repo.findLowStock(threshold, pageable).map(StockView::from);
    }
}
