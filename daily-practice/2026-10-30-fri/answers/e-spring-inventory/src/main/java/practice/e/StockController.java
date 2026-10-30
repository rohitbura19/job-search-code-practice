package practice.e;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stock")
public class StockController {
    private final StockService service;
    private final StockProps props;

    public StockController(StockService service, StockProps props) { this.service = service; this.props = props; }

    @PostMapping("/reservations")
    public StockView reserve(@Valid @RequestBody ReserveRequest req) { return service.reserve(req.sku(), req.qty()); }

    @GetMapping("/low")
    public PagedResponse<StockView> low(@RequestParam(required = false) @Min(0) Integer threshold,
                                        @RequestParam(defaultValue = "0") @Min(0) int page,
                                        @RequestParam(defaultValue = "20") @Min(1) int size) {
        int t = threshold != null ? threshold : props.lowThreshold();
        return PagedResponse.of(service.lowStock(t, PageRequest.of(page, Math.min(size, props.maxPageSize()))));
    }

    @GetMapping("/{sku}")
    public StockView get(@PathVariable String sku) { return service.get(sku); }
}
