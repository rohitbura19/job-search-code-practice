package practice.e.estimate;

import org.springframework.stereotype.Service;

/**
 * TODO - the cached lookup lives in its OWN bean so the proxy is hit (no self-invocation).
 * tariff(regionCode): cache "tariffs", key = the region code; load from the repository, throw NotFoundException if missing
 * (an exception is never cached, so a missing region is never cached as null).
 * evict(regionCode): remove that one entry from the "tariffs" cache.
 * Add @Cacheable and @CacheEvict.
 */
@Service
public class TariffLookup {
    private final TariffRepository repo;

    public TariffLookup(TariffRepository repo) { this.repo = repo; }

    public Tariff tariff(String regionCode) {
        throw new UnsupportedOperationException("TODO");
    }

    public void evict(String regionCode) {
        throw new UnsupportedOperationException("TODO");
    }
}
