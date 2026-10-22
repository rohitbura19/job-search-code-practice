package practice.e;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.hamcrest.Matchers.endsWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** E - web layer only; the service is mocked. Run: mvn test (inside e-spring-charger-commands). */
@WebMvcTest(CommandController.class)
class CommandControllerTest {

    @Autowired MockMvc mvc;
    @MockBean CommandService service;            // Spring Boot 3.4+: @MockitoBean

    static DeviceCommand command(String chargerId, CommandType type) {
        DeviceCommand c = new DeviceCommand();
        c.setChargerId(chargerId);
        c.setType(type);
        c.setIdempotencyKey("k");
        c.setCreatedAt(Instant.parse("2026-10-12T10:00:00Z"));
        c.setExpiresAt(Instant.parse("2026-10-12T10:05:00Z"));
        return c;
    }

    @Test @DisplayName("POST valid -> 202 Accepted with Location /api/v1/commands/{id} and the response body")
    void createOk() throws Exception {
        DeviceCommand c = command("CH1", CommandType.RESTART);
        when(service.create(eq("CH1"), eq("key-1"), any(CommandRequest.class))).thenReturn(c);
        mvc.perform(post("/api/v1/chargers/CH1/commands").header("Idempotency-Key", "key-1")
                .contentType(MediaType.APPLICATION_JSON).content("{\"type\":\"RESTART\",\"ttlSeconds\":60}"))
           .andExpect(status().isAccepted())
           .andExpect(header().string("Location", endsWith("/api/v1/commands/" + c.getId())))
           .andExpect(jsonPath("$.id").value(c.getId().toString()))
           .andExpect(jsonPath("$.chargerId").value("CH1"))
           .andExpect(jsonPath("$.type").value("RESTART"))
           .andExpect(jsonPath("$.status").value("PENDING"));
        ArgumentCaptor<CommandRequest> req = ArgumentCaptor.forClass(CommandRequest.class);
        verify(service).create(eq("CH1"), eq("key-1"), req.capture());
        assertEquals(CommandType.RESTART, req.getValue().type());
        assertEquals(Integer.valueOf(60), req.getValue().ttlSeconds());
    }

    @Test @DisplayName("POST without ttlSeconds is valid (the default TTL applies)")
    void createWithoutTtl() throws Exception {
        when(service.create(any(), any(), any())).thenReturn(command("CH1", CommandType.UNLOCK));
        mvc.perform(post("/api/v1/chargers/CH1/commands").header("Idempotency-Key", "key-2")
                .contentType(MediaType.APPLICATION_JSON).content("{\"type\":\"UNLOCK\"}"))
           .andExpect(status().isAccepted());
    }

    @Test @DisplayName("POST without the Idempotency-Key header -> 400, service never called")
    void missingKey() throws Exception {
        mvc.perform(post("/api/v1/chargers/CH1/commands")
                .contentType(MediaType.APPLICATION_JSON).content("{\"type\":\"RESTART\"}"))
           .andExpect(status().isBadRequest());
        verifyNoInteractions(service);
    }

    @Test @DisplayName("POST with a missing type -> 400")
    void missingType() throws Exception {
        mvc.perform(post("/api/v1/chargers/CH1/commands").header("Idempotency-Key", "k")
                .contentType(MediaType.APPLICATION_JSON).content("{\"ttlSeconds\":60}"))
           .andExpect(status().isBadRequest());
        verifyNoInteractions(service);
    }

    @Test @DisplayName("POST with ttlSeconds 0 or 3601 -> 400; 1 and 3600 are accepted")
    void ttlBounds() throws Exception {
        when(service.create(any(), any(), any())).thenReturn(command("CH1", CommandType.RESTART));
        for (int bad : new int[]{0, -5, 3601}) {
            mvc.perform(post("/api/v1/chargers/CH1/commands").header("Idempotency-Key", "k")
                    .contentType(MediaType.APPLICATION_JSON).content("{\"type\":\"RESTART\",\"ttlSeconds\":" + bad + "}"))
               .andExpect(status().isBadRequest());
        }
        for (int ok : new int[]{1, 3600}) {
            mvc.perform(post("/api/v1/chargers/CH1/commands").header("Idempotency-Key", "k")
                    .contentType(MediaType.APPLICATION_JSON).content("{\"type\":\"RESTART\",\"ttlSeconds\":" + ok + "}"))
               .andExpect(status().isAccepted());
        }
    }

    @Test @DisplayName("POST with an unknown command type or malformed JSON -> 400")
    void badBodies() throws Exception {
        mvc.perform(post("/api/v1/chargers/CH1/commands").header("Idempotency-Key", "k")
                .contentType(MediaType.APPLICATION_JSON).content("{\"type\":\"SELF_DESTRUCT\"}"))
           .andExpect(status().isBadRequest());
        mvc.perform(post("/api/v1/chargers/CH1/commands").header("Idempotency-Key", "k")
                .contentType(MediaType.APPLICATION_JSON).content("{not json"))
           .andExpect(status().isBadRequest());
        verifyNoInteractions(service);
    }

    @Test @DisplayName("GET history -> a page of CommandResponse; default page size 20")
    void historyDefaults() throws Exception {
        DeviceCommand c = command("CH1", CommandType.SET_CURRENT_LIMIT);
        when(service.history(eq("CH1"), any(Pageable.class))).thenReturn(new PageImpl<>(List.of(c), PageRequest.of(0, 20), 1));
        mvc.perform(get("/api/v1/chargers/CH1/commands"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.content[0].id").value(c.getId().toString()))
           .andExpect(jsonPath("$.content[0].type").value("SET_CURRENT_LIMIT"));
        ArgumentCaptor<Pageable> p = ArgumentCaptor.forClass(Pageable.class);
        verify(service).history(eq("CH1"), p.capture());
        assertEquals(0, p.getValue().getPageNumber());
        assertEquals(20, p.getValue().getPageSize());
    }

    @Test @DisplayName("GET history honours ?page= and ?size=")
    void historyPaging() throws Exception {
        when(service.history(eq("CH2"), any(Pageable.class))).thenReturn(new PageImpl<>(List.of(), PageRequest.of(2, 5), 0));
        mvc.perform(get("/api/v1/chargers/CH2/commands").param("page", "2").param("size", "5"))
           .andExpect(status().isOk());
        ArgumentCaptor<Pageable> p = ArgumentCaptor.forClass(Pageable.class);
        verify(service).history(eq("CH2"), p.capture());
        assertEquals(2, p.getValue().getPageNumber());
        assertEquals(5, p.getValue().getPageSize());
    }
}
