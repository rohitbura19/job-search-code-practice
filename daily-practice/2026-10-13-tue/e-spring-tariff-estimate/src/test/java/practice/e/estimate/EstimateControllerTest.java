package practice.e.estimate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** E - web layer: routing, request validation, ProblemDetail. Service and lookup are mocked. Run: mvn test (inside this folder). */
@WebMvcTest(EstimateController.class)
@Import(ApiErrors.class)
class EstimateControllerTest {

    @Autowired MockMvc mvc;
    @MockBean EstimateService service;        // Spring Boot 3.4+: @MockitoBean
    @MockBean TariffLookup tariffs;

    static String body(String siteId, String kwh, String region, String currency) {
        return "{\"siteId\":" + siteId + ",\"kwh\":" + kwh + ",\"regionCode\":" + region + ",\"currency\":" + currency + "}";
    }
    static final String VALID = body("\"s1\"", "12.5", "\"IN-KA\"", "\"INR\"");

    private void expectBadRequest(String json) throws Exception {
        mvc.perform(post("/api/v1/estimates").contentType(MediaType.APPLICATION_JSON).content(json))
           .andExpect(status().isBadRequest())
           .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON));
        verifyNoInteractions(service);
    }

    // ---------- POST /estimates ----------

    @Test @DisplayName("valid request -> 200 with the service's response; the request reaches the service intact")
    void ok() throws Exception {
        when(service.estimate(any())).thenReturn(new EstimateResponse("s1", new BigDecimal("106.25"), "INR", false));
        mvc.perform(post("/api/v1/estimates").contentType(MediaType.APPLICATION_JSON).content(VALID))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.siteId").value("s1"))
           .andExpect(jsonPath("$.cost").value(106.25))
           .andExpect(jsonPath("$.currency").value("INR"))
           .andExpect(jsonPath("$.fxFallbackUsed").value(false));
        verify(service).estimate(new EstimateRequest("s1", new BigDecimal("12.5"), "IN-KA", "INR"));
    }

    @Test @DisplayName("kwh = -1 -> 400 problem+json, service never called")
    void negativeKwh() throws Exception { expectBadRequest(body("\"s1\"", "-1", "\"IN-KA\"", "\"INR\"")); }

    @Test @DisplayName("kwh = 0 -> 400 (strictly greater than 0)")
    void zeroKwh() throws Exception { expectBadRequest(body("\"s1\"", "0", "\"IN-KA\"", "\"INR\"")); }

    @Test @DisplayName("kwh missing -> 400")
    void missingKwh() throws Exception { expectBadRequest("{\"siteId\":\"s1\",\"regionCode\":\"IN-KA\",\"currency\":\"INR\"}"); }

    @Test @DisplayName("blank siteId -> 400")
    void blankSite() throws Exception { expectBadRequest(body("\" \"", "1", "\"IN-KA\"", "\"INR\"")); }

    @Test @DisplayName("regionCode: lower case, wrong shape or too long suffix -> 400")
    void badRegion() throws Exception {
        expectBadRequest(body("\"s1\"", "1", "\"in-ka\"", "\"INR\""));
        expectBadRequest(body("\"s1\"", "1", "\"INDIA\"", "\"INR\""));
        expectBadRequest(body("\"s1\"", "1", "\"IN-KARN\"", "\"INR\""));
        expectBadRequest(body("\"s1\"", "1", "\"I\"", "\"INR\""));
    }

    @Test @DisplayName("currency must be exactly 3 characters")
    void badCurrency() throws Exception {
        expectBadRequest(body("\"s1\"", "1", "\"IN-KA\"", "\"IN\""));
        expectBadRequest(body("\"s1\"", "1", "\"IN-KA\"", "\"INRR\""));
        expectBadRequest(body("\"s1\"", "1", "\"IN-KA\"", "null"));
    }

    @Test @DisplayName("region shapes that ARE allowed: IN, IN-KA, US-CA1")
    void goodRegions() throws Exception {
        when(service.estimate(any())).thenReturn(new EstimateResponse("s1", BigDecimal.ONE, "INR", false));
        for (String region : new String[]{"IN", "IN-KA", "US-CA1"})
            mvc.perform(post("/api/v1/estimates").contentType(MediaType.APPLICATION_JSON).content(body("\"s1\"", "1", "\"" + region + "\"", "\"INR\"")))
               .andExpect(status().isOk());
    }

    @Test @DisplayName("malformed JSON -> 400 problem+json")
    void malformed() throws Exception { expectBadRequest("{not json"); }

    @Test @DisplayName("unknown region (service throws NotFoundException) -> 404 problem+json with the message as detail")
    void estimateNotFound() throws Exception {
        when(service.estimate(any())).thenThrow(new NotFoundException("IN-KA"));
        mvc.perform(post("/api/v1/estimates").contentType(MediaType.APPLICATION_JSON).content(VALID))
           .andExpect(status().isNotFound())
           .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
           .andExpect(jsonPath("$.status").value(404))
           .andExpect(jsonPath("$.detail").value(containsString("IN-KA")));
    }

    // ---------- GET /tariffs/{regionCode} ----------

    @Test @DisplayName("GET tariff -> 200 with the tariff")
    void getTariff() throws Exception {
        when(tariffs.tariff("IN-KA")).thenReturn(new Tariff(1L, "IN-KA", new BigDecimal("8.5000"), "INR", Instant.parse("2026-10-13T00:00:00Z")));
        mvc.perform(get("/api/v1/tariffs/{r}", "IN-KA"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.regionCode").value("IN-KA"))
           .andExpect(jsonPath("$.ratePerKwh").value(8.5))
           .andExpect(jsonPath("$.currency").value("INR"));
    }

    @Test @DisplayName("GET unknown tariff -> 404 problem+json")
    void getTariffMissing() throws Exception {
        when(tariffs.tariff("ZZ")).thenThrow(new NotFoundException("ZZ"));
        mvc.perform(get("/api/v1/tariffs/{r}", "ZZ"))
           .andExpect(status().isNotFound())
           .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
           .andExpect(jsonPath("$.detail").value(containsString("ZZ")));
    }
}
