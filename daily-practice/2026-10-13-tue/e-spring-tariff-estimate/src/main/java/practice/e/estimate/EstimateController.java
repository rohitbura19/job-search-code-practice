package practice.e.estimate;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO - base path /api/v1
 *   POST /api/v1/estimates               @Valid @RequestBody EstimateRequest -> 200, body EstimateResponse (from EstimateService)
 *   GET  /api/v1/tariffs/{regionCode}    -> 200, body Tariff (from TariffLookup); an unknown region becomes 404 through ApiErrors
 */
@RestController
@RequestMapping("/api/v1")
public class EstimateController {
    private final EstimateService service;
    private final TariffLookup tariffs;

    public EstimateController(EstimateService service, TariffLookup tariffs) { this.service = service; this.tariffs = tariffs; }

    // TODO: endpoints here
}
