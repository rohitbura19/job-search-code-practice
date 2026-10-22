package practice.e.estimate;

import org.springframework.stereotype.Component;

/**
 * TODO - on(msg): evict the cached tariff of msg.regionCode(). Let every failure propagate (no try/catch) so SQS redelivers.
 * (@SqsListener("tariff-updated") is paper: spring-cloud-aws is not on this classpath.)
 */
@Component
public class TariffUpdatedListener {
    private final TariffLookup tariffs;

    public TariffUpdatedListener(TariffLookup tariffs) { this.tariffs = tariffs; }

    public void on(TariffUpdated msg) {
        throw new UnsupportedOperationException("TODO");
    }
}
