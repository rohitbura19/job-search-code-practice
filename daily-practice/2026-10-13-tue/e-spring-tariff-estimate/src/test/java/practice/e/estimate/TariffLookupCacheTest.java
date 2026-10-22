package practice.e.estimate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/** E - the real Spring caching proxy around TariffLookup, with a mocked repository. Each test uses its own region code. */
@SpringJUnitConfig(TariffLookupCacheTest.Config.class)
class TariffLookupCacheTest {

    @Configuration
    @EnableCaching
    static class Config {
        @Bean TariffRepository tariffRepository() { return Mockito.mock(TariffRepository.class); }
        @Bean CacheManager cacheManager() { return new ConcurrentMapCacheManager("tariffs"); }
        @Bean TariffLookup tariffLookup(TariffRepository repo) { return new TariffLookup(repo); }
    }

    @Autowired TariffLookup lookup;
    @Autowired TariffRepository repo;
    @Autowired CacheManager cacheManager;

    static Tariff tariff(String region) { return new Tariff(1L, region, new BigDecimal("8.5000"), "INR", Instant.parse("2026-10-13T00:00:00Z")); }

    @Test @DisplayName("the second call is served from the cache: the repository is hit once")
    void cachesHit() {
        Tariff t = tariff("C1");
        when(repo.findByRegionCode("C1")).thenReturn(Optional.of(t));
        assertSame(t, lookup.tariff("C1"));
        assertSame(t, lookup.tariff("C1"));
        verify(repo, times(1)).findByRegionCode("C1");
    }

    @Test @DisplayName("the cache key is the region code itself; regions do not share entries")
    void keyedByRegion() {
        Tariff a = tariff("K1"), b = tariff("K2");
        when(repo.findByRegionCode("K1")).thenReturn(Optional.of(a));
        when(repo.findByRegionCode("K2")).thenReturn(Optional.of(b));
        assertSame(a, lookup.tariff("K1"));
        assertSame(b, lookup.tariff("K2"));
        assertSame(a, cacheManager.getCache("tariffs").get("K1", Tariff.class));
        assertSame(b, cacheManager.getCache("tariffs").get("K2", Tariff.class));
    }

    @Test @DisplayName("a missing region throws NotFoundException every time and is never cached")
    void missingNotCached() {
        when(repo.findByRegionCode("M1")).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> lookup.tariff("M1"));
        assertThrows(NotFoundException.class, () -> lookup.tariff("M1"));
        verify(repo, times(2)).findByRegionCode("M1");
        assertNull(cacheManager.getCache("tariffs").get("M1"));
    }

    @Test @DisplayName("evict removes one entry: the next call reloads from the repository")
    void evictOne() {
        when(repo.findByRegionCode("E1")).thenReturn(Optional.of(tariff("E1")));
        when(repo.findByRegionCode("E2")).thenReturn(Optional.of(tariff("E2")));
        lookup.tariff("E1"); lookup.tariff("E2");
        lookup.evict("E1");
        assertNull(cacheManager.getCache("tariffs").get("E1"));
        assertNotNull(cacheManager.getCache("tariffs").get("E2"), "other regions stay cached");
        lookup.tariff("E1"); lookup.tariff("E2");
        verify(repo, times(2)).findByRegionCode("E1");
        verify(repo, times(1)).findByRegionCode("E2");
    }

    @Test @DisplayName("after the database value changes and the entry is evicted, the new value is served")
    void freshValueAfterEvict() {
        Tariff v1 = tariff("F1");
        Tariff v2 = new Tariff(1L, "F1", new BigDecimal("9.0000"), "INR", Instant.parse("2026-10-13T01:00:00Z"));
        when(repo.findByRegionCode("F1")).thenReturn(Optional.of(v1)).thenReturn(Optional.of(v2));
        assertEquals(new BigDecimal("8.5000"), lookup.tariff("F1").ratePerKwh());
        assertEquals(new BigDecimal("8.5000"), lookup.tariff("F1").ratePerKwh(), "stale until evicted");
        lookup.evict("F1");
        assertEquals(new BigDecimal("9.0000"), lookup.tariff("F1").ratePerKwh());
    }
}
