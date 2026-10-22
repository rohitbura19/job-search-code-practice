package practice.e;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/** E - REST controller + validation + error mapping. Repository and service are mocked. Run: mvn test (inside e-spring-charger-events). */
@WebMvcTest(ChargerController.class)
@Import(ApiErrors.class)
class ChargerControllerTest {

    @Autowired MockMvc mvc;
    @MockBean ChargerEventRepository repo;          // Spring Boot 3.4+: @MockitoBean
    @MockBean ChargerEventService service;

    static ChargerEvent event() {
        ChargerEvent e = new ChargerEvent();
        e.setEventId("evt-1"); e.setChargerId("CH-7"); e.setStatus(ChargerEvent.Status.CHARGING);
        e.setKwh(12.5); e.setEventTime(Instant.parse("2026-10-11T10:00:00Z"));
        return e;
    }

    static final String VALID = """
        {"eventId":"evt-1","chargerId":"CH-7","status":"CHARGING","kwh":12.5,"eventTime":"2026-10-11T10:00:00Z"}""";

    @Test @DisplayName("GET status returns 200 and the latest event as JSON")
    void statusFound() throws Exception {
        when(repo.findFirstByChargerIdOrderByEventTimeDesc("CH-7")).thenReturn(Optional.of(event()));
        mvc.perform(get("/api/v1/chargers/CH-7/status"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.eventId").value("evt-1"))
            .andExpect(jsonPath("$.chargerId").value("CH-7"))
            .andExpect(jsonPath("$.status").value("CHARGING"))
            .andExpect(jsonPath("$.kwh").value(12.5))
            .andExpect(jsonPath("$.eventTime").value("2026-10-11T10:00:00Z"));
    }

    @Test @DisplayName("GET status for an unknown charger is 404")
    void statusNotFound() throws Exception {
        when(repo.findFirstByChargerIdOrderByEventTimeDesc("NOPE")).thenReturn(Optional.empty());
        mvc.perform(get("/api/v1/chargers/NOPE/status")).andExpect(status().isNotFound());
    }

    @Test @DisplayName("POST a valid event is accepted (202) and handed to the service")
    void ingestValid() throws Exception {
        mvc.perform(post("/api/v1/chargers/events").contentType(MediaType.APPLICATION_JSON).content(VALID))
            .andExpect(status().isAccepted());
        verify(service, times(1)).save(new EventDto("evt-1", "CH-7", ChargerEvent.Status.CHARGING, 12.5, Instant.parse("2026-10-11T10:00:00Z")));
    }

    @Test @DisplayName("POST a blank chargerId and a negative kwh is 400 naming both fields; the service is never called")
    void ingestInvalid() throws Exception {
        String bad = """
            {"eventId":"evt-1","chargerId":"  ","status":"CHARGING","kwh":-1,"eventTime":"2026-10-11T10:00:00Z"}""";
        mvc.perform(post("/api/v1/chargers/events").contentType(MediaType.APPLICATION_JSON).content(bad))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error", containsString("chargerId")))
            .andExpect(jsonPath("$.error", containsString("kwh")));
        verify(service, never()).save(any());
    }

    @Test @DisplayName("POST with missing status, eventId and eventTime is 400 naming each")
    void ingestMissingFields() throws Exception {
        mvc.perform(post("/api/v1/chargers/events").contentType(MediaType.APPLICATION_JSON).content("{\"chargerId\":\"CH-7\",\"kwh\":1}"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error", containsString("status")))
            .andExpect(jsonPath("$.error", containsString("eventId")))
            .andExpect(jsonPath("$.error", containsString("eventTime")));
        verify(service, never()).save(any());
    }

    @Test @DisplayName("POST zero kwh is allowed")
    void zeroKwhIsFine() throws Exception {
        String zero = VALID.replace("12.5", "0");
        mvc.perform(post("/api/v1/chargers/events").contentType(MediaType.APPLICATION_JSON).content(zero))
            .andExpect(status().isAccepted());
    }
}
