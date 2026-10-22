package practice.e.estimate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/** E - plain unit test with Mockito, no Spring. */
class EstimateServiceTest {

    final TariffLookup tariffs = mock(TariffLookup.class);
    final FxClient fx = mock(FxClient.class);
    final EstimateService service = new EstimateService(tariffs, fx);

    static Tariff tariff(String rate, String currency) {
        return new Tariff(1L, "IN-KA", new BigDecimal(rate), currency, Instant.parse("2026-10-13T00:00:00Z"));
    }
    static EstimateRequest req(String kwh, String currency) { return new EstimateRequest("s1", new BigDecimal(kwh), "IN-KA", currency); }

    @Test @DisplayName("same currency: kwh * rate, scale 2, FX never called")
    void sameCurrency() {
        when(tariffs.tariff("IN-KA")).thenReturn(tariff("8.5000", "INR"));
        EstimateResponse r = service.estimate(req("10", "INR"));
        assertEquals("s1", r.siteId());
        assertEquals(new BigDecimal("85.00"), r.cost());
        assertEquals("INR", r.currency());
        assertFalse(r.fxFallbackUsed());
        verifyNoInteractions(fx);
    }

    @Test @DisplayName("different currency: converted with the FX rate, currency is the requested one")
    void converted() {
        when(tariffs.tariff("IN-KA")).thenReturn(tariff("8.5000", "INR"));
        when(fx.rate("INR", "USD")).thenReturn(new BigDecimal("0.012"));
        EstimateResponse r = service.estimate(req("10", "USD"));
        assertEquals(new BigDecimal("1.02"), r.cost());
        assertEquals("USD", r.currency());
        verify(fx).rate("INR", "USD");
    }

    @Test @DisplayName("BigDecimal, not double: 1.005 kWh * 1.0000 rounds HALF_UP to 1.01")
    void notDouble() {
        when(tariffs.tariff("IN-KA")).thenReturn(tariff("1.0000", "INR"));
        assertEquals(new BigDecimal("1.01"), service.estimate(req("1.005", "INR")).cost());
    }

    @Test @DisplayName("HALF_UP, not HALF_EVEN: 0.125 rounds to 0.13")
    void halfUp() {
        when(tariffs.tariff("IN-KA")).thenReturn(tariff("1.0000", "INR"));
        assertEquals(new BigDecimal("0.13"), service.estimate(req("0.125", "INR")).cost());
    }

    @Test @DisplayName("the result always has scale 2")
    void scale() {
        when(tariffs.tariff("IN-KA")).thenReturn(tariff("3.0000", "INR"));
        assertEquals(new BigDecimal("3.00"), service.estimate(req("1", "INR")).cost());
        assertEquals(new BigDecimal("30000.00"), service.estimate(req("10000", "INR")).cost());
    }

    @Test @DisplayName("unknown region: NotFoundException propagates and FX is not touched")
    void unknownRegion() {
        when(tariffs.tariff("IN-KA")).thenThrow(new NotFoundException("IN-KA"));
        assertThrows(NotFoundException.class, () -> service.estimate(req("1", "INR")));
        verifyNoInteractions(fx);
    }

    @Test @DisplayName("FX failure propagates (the circuit breaker fallback decides, not the service)")
    void fxFails() {
        when(tariffs.tariff("IN-KA")).thenReturn(tariff("8.5000", "INR"));
        when(fx.rate("INR", "USD")).thenThrow(new FxUnavailableException("INR", "USD", new RuntimeException("slow")));
        assertThrows(FxUnavailableException.class, () -> service.estimate(req("1", "USD")));
    }
}
