package practice.e.estimate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/** E - the FX client against a fake server (no network). */
class FxClientTest {

    final RestClient.Builder builder = RestClient.builder().baseUrl("http://fx.test");
    final MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
    final FxClient client = new FxClient(builder.build());

    @Test @DisplayName("GET /rates?from=USD&to=INR and read the rate from the JSON body")
    void fetchesRate() {
        server.expect(requestTo("http://fx.test/rates?from=USD&to=INR"))
              .andExpect(method(HttpMethod.GET))
              .andRespond(withSuccess("{\"rate\":83.25}", MediaType.APPLICATION_JSON));
        assertEquals(new BigDecimal("83.25"), client.rate("USD", "INR"));
        server.verify();
    }

    @Test @DisplayName("same currency: ONE and no HTTP call at all")
    void sameCurrency() {
        assertEquals(BigDecimal.ONE, client.rate("INR", "INR"));
        server.verify();   // no expectation was set: any request would already have failed
    }

    @Test @DisplayName("an HTTP 500 surfaces as a RestClientException (so the circuit breaker counts it as a failure)")
    void serverError() {
        server.expect(requestTo("http://fx.test/rates?from=USD&to=INR")).andRespond(withServerError());
        assertThrows(RestClientException.class, () -> client.rate("USD", "INR"));
    }

    @Test @DisplayName("fallback: same currency is still ONE")
    void fallbackSame() {
        assertEquals(BigDecimal.ONE, client.rateFallback("INR", "INR", new RuntimeException("circuit open")));
    }

    @Test @DisplayName("fallback: different currencies -> FxUnavailableException carrying the original cause")
    void fallbackDifferent() {
        RuntimeException cause = new RuntimeException("circuit open");
        FxUnavailableException e = assertThrows(FxUnavailableException.class, () -> client.rateFallback("USD", "INR", cause));
        assertSame(cause, e.getCause());
    }
}
