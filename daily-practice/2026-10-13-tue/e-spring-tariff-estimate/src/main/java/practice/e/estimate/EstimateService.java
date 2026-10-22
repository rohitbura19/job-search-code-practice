package practice.e.estimate;

import org.springframework.stereotype.Service;

/**
 * TODO - estimate(r): tariff = tariffs.tariff(r.regionCode()); cost = kwh * ratePerKwh; if the tariff currency differs from
 * the requested one, multiply by fx.rate(tariffCurrency, requestedCurrency); scale 2, RoundingMode.HALF_UP.
 * Response: siteId, cost, the REQUESTED currency, fxFallbackUsed = false. Same-currency requests must not call the FX client.
 * (@Transactional(readOnly = true) is paper: spring-tx is not on this classpath.)
 */
@Service
public class EstimateService {
    private final TariffLookup tariffs;
    private final FxClient fx;

    public EstimateService(TariffLookup tariffs, FxClient fx) { this.tariffs = tariffs; this.fx = fx; }

    public EstimateResponse estimate(EstimateRequest r) {
        throw new UnsupportedOperationException("TODO");
    }
}
