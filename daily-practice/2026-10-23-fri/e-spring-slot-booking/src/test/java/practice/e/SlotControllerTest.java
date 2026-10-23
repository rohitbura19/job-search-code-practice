package practice.e;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(SlotController.class)
class SlotControllerTest {
    static final String PROBLEM = "application/problem+json";
    static final Instant START = Instant.parse("2999-01-01T10:00:00Z");

    @Autowired MockMvc mvc;
    @MockBean SlotService service;

    @Test void createReturns201WithLocationAndBody() throws Exception {
        when(service.create(any())).thenReturn(new SlotView(7L, "st1", START, 4, 4));
        mvc.perform(post("/api/slots").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"stationId\":\"st1\",\"startsAt\":\"2999-01-01T10:00:00Z\",\"capacity\":4}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/slots/7"))
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.remaining").value(4))
                .andExpect(jsonPath("$.startsAt").value("2999-01-01T10:00:00Z"));
    }

    @Test void invalidBodyIsProblemJsonWithFieldErrorsAndNoServiceCall() throws Exception {
        mvc.perform(post("/api/slots").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"stationId\":\" \",\"startsAt\":\"2001-01-01T00:00:00Z\",\"capacity\":99}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(PROBLEM))
                .andExpect(jsonPath("$.title").value("Validation failed"))
                .andExpect(jsonPath("$.errors.length()").value(3))
                .andExpect(jsonPath("$.errors[*].field", containsInAnyOrder("stationId", "startsAt", "capacity")));
        verifyNoInteractions(service);
    }

    @Test void missingFieldsIncludingCapacityAreReported() throws Exception {
        mvc.perform(post("/api/slots").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[*].field", containsInAnyOrder("stationId", "startsAt", "capacity")));
        verifyNoInteractions(service);
    }

    @Test void availableParsesTheInstant() throws Exception {
        when(service.available("st1", Instant.parse("2026-11-01T00:00:00Z")))
                .thenReturn(List.of(new SlotView(1L, "st1", START, 3, 2)));
        mvc.perform(get("/api/stations/st1/slots").param("from", "2026-11-01T00:00:00Z"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].remaining").value(2));
    }

    @Test void availableNeedsAValidFrom() throws Exception {
        mvc.perform(get("/api/stations/st1/slots")).andExpect(status().isBadRequest());
        mvc.perform(get("/api/stations/st1/slots").param("from", "yesterday")).andExpect(status().isBadRequest());
        verifyNoInteractions(service);
    }

    @Test void reserveReturnsTheUpdatedView() throws Exception {
        when(service.reserve(5)).thenReturn(new SlotView(5L, "st1", START, 3, 1));
        mvc.perform(post("/api/slots/5/reservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.remaining").value(1));
    }

    @Test void reserveMapsDomainExceptionsToProblemDetails() throws Exception {
        when(service.reserve(404)).thenThrow(new SlotNotFoundException(404));
        when(service.reserve(409)).thenThrow(new SlotFullException(409));
        mvc.perform(post("/api/slots/404/reservations"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(PROBLEM))
                .andExpect(jsonPath("$.title").value("Slot not found"))
                .andExpect(jsonPath("$.detail").value("slot 404 not found"));
        mvc.perform(post("/api/slots/409/reservations"))
                .andExpect(status().isConflict())
                .andExpect(content().contentTypeCompatibleWith(PROBLEM))
                .andExpect(jsonPath("$.title").value("Slot full"));
    }

    @Test void nonNumericIdIs400() throws Exception {
        mvc.perform(post("/api/slots/abc/reservations")).andExpect(status().isBadRequest());
    }
}
