package practice.e;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.UUID;

import static org.hamcrest.Matchers.endsWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/** E - POST /api/v1/alerts: status, Location and validation. The service is mocked. Run: mvn test */
@WebMvcTest(AlertController.class)
class AlertControllerTest {

    @Autowired MockMvc mvc;
    @MockBean AlertService service;            // Spring Boot 3.4+: @MockitoBean

    static final UUID ID = UUID.fromString("11111111-2222-3333-4444-555555555555");

    static Alert saved() {
        Alert a = new Alert();
        a.setId(ID); a.setSiteId("site-1"); a.setSeverity(Alert.Severity.WARN);
        a.setMessage("low voltage"); a.setCreatedAt(Instant.parse("2026-10-14T10:00:00Z"));
        return a;
    }

    @Test @DisplayName("valid body -> 201, Location .../api/v1/alerts/{id}, body has the alert")
    void created() throws Exception {
        when(service.create(any(AlertRequest.class))).thenReturn(saved());
        mvc.perform(post("/api/v1/alerts").contentType(MediaType.APPLICATION_JSON)
                .content("{\"siteId\":\"site-1\",\"severity\":\"WARN\",\"message\":\"low voltage\"}"))
           .andExpect(status().isCreated())
           .andExpect(header().string(HttpHeaders.LOCATION, endsWith("/api/v1/alerts/" + ID)))
           .andExpect(jsonPath("$.id").value(ID.toString()))
           .andExpect(jsonPath("$.siteId").value("site-1"))
           .andExpect(jsonPath("$.severity").value("WARN"));
    }

    @Test @DisplayName("blank siteId, missing severity, 501-character message -> 400 and the service is never called")
    void invalid() throws Exception {
        mvc.perform(post("/api/v1/alerts").contentType(MediaType.APPLICATION_JSON)
                .content("{\"siteId\":\" \",\"message\":\"" + "x".repeat(501) + "\"}"))
           .andExpect(status().isBadRequest());
        verifyNoInteractions(service);
    }

    @Test @DisplayName("unknown severity value and malformed JSON -> 400")
    void malformed() throws Exception {
        mvc.perform(post("/api/v1/alerts").contentType(MediaType.APPLICATION_JSON)
                .content("{\"siteId\":\"s\",\"severity\":\"BOGUS\",\"message\":\"m\"}"))
           .andExpect(status().isBadRequest());
        mvc.perform(post("/api/v1/alerts").contentType(MediaType.APPLICATION_JSON).content("{not json"))
           .andExpect(status().isBadRequest());
        verifyNoInteractions(service);
    }
}
