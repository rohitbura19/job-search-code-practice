package practice.e;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/** TODO: @Service; @Transactional on the writes, readOnly on the reads. */
public class StockService {
    private final StockRepository repo;
    private final StockEventPublisher events;
    private final StockProps props;

    public StockService(StockRepository repo, StockEventPublisher events, StockProps props) {
        this.repo = repo; this.events = events; this.props = props;
    }

    /**
     * repo.reserve(...) == 0 means "rejected": OutOfStockException if the sku exists (findBySku), else StockNotFoundException.
     * On success: events.reserved(sku, qty); then, if the item's available() <= props.lowThreshold(), events.lowStock(sku, available). Return the view.
     */
    public StockView reserve(String sku, int qty) { throw new UnsupportedOperationException("TODO"); }

    /** repo.restock(...) == 0 -> StockNotFoundException. */
    public void restock(String sku, int qty) { throw new UnsupportedOperationException("TODO"); }

    /** Unknown sku -> StockNotFoundException. */
    public StockView get(String sku) { throw new UnsupportedOperationException("TODO"); }

    public Page<StockView> lowStock(int threshold, Pageable pageable) { throw new UnsupportedOperationException("TODO"); }
}
