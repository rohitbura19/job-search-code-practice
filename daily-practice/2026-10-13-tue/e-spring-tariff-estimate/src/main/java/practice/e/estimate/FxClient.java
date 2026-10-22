package practice.e.estimate;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;

/**
 * TODO. The RestClient is injected (built in FxConfig); tests pass one wired to MockRestServiceServer.
 * rate(from, to): same currency -> BigDecimal.ONE WITHOUT any HTTP call; otherwise GET /rates?from={from}&to={to}
 * and return the "rate" of the FxRate body. HTTP errors surface as RestClientException.
 * rateFallback(from, to, t): what the circuit breaker calls when rate() fails. Same currency -> ONE, otherwise throw
 * FxUnavailableException(from, to, t). (The @CircuitBreaker(name = "fx", fallbackMethod = "rateFallback") annotation is paper:
 * resilience4j is not on this classpath.)
 */
@Component
public class FxClient {
    private final RestClient http;

    public FxClient(RestClient http) { this.http = http; }

    public BigDecimal rate(String from, String to) {
        throw new UnsupportedOperationException("TODO");
    }

    BigDecimal rateFallback(String from, String to, Throwable t) {
        throw new UnsupportedOperationException("TODO");
    }
}
