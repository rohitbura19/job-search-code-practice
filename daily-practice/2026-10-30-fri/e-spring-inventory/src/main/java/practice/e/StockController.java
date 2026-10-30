package practice.e;

/**
 * TODO: REST controller under /api/stock.
 *   POST /reservations   body ReserveRequest (validated) -> 200 StockView
 *   GET  /low            optional threshold (>= 0, default props.lowThreshold()), page (>= 0, default 0), size (>= 1, default 20, capped at props.maxPageSize())
 *                        -> PagedResponse<StockView>; invalid parameters -> 400
 *   GET  /{sku}          -> StockView
 */
public class StockController {
    private final StockService service;
    private final StockProps props;

    public StockController(StockService service, StockProps props) { this.service = service; this.props = props; }

    public StockView reserve(ReserveRequest req) { throw new UnsupportedOperationException("TODO"); }

    public PagedResponse<StockView> low(Integer threshold, int page, int size) { throw new UnsupportedOperationException("TODO"); }

    public StockView get(String sku) { throw new UnsupportedOperationException("TODO"); }
}
