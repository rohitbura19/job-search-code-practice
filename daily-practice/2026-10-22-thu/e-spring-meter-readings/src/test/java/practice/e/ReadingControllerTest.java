package practice.e;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ReadingController.class)
class ReadingControllerTest {
    @Autowired MockMvc mvc;
    @MockBean MeterReadingRepository repo;

    static final String PROBLEM = "application/problem+json";

    @Test void createReturns201WithLocation() throws Exception {
        when(repo.save(any())).thenAnswer(inv -> {
            MeterReading m = inv.getArgument(0);
            ReflectionTestUtils.setField(m, "id", 5L);
            return m;
        });
        mvc.perform(post("/api/readings").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"meterId\":\"m1\",\"kwh\":1.5,\"readAt\":\"2026-01-01T10:00:00Z\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/readings/5"))
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.meterId").value("m1"))
                .andExpect(jsonPath("$.readAt").value("2026-01-01T10:00:00Z"));
    }

    @Test void invalidBodyIsProblemJsonWithFieldErrorsAndNoRepositoryCall() throws Exception {
        mvc.perform(post("/api/readings").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"meterId\":\" \",\"kwh\":-1,\"readAt\":\"2999-01-01T00:00:00Z\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(PROBLEM))
                .andExpect(jsonPath("$.title").value("Validation failed"))
                .andExpect(jsonPath("$.errors.length()").value(3))
                .andExpect(jsonPath("$.errors[*].field", containsInAnyOrder("meterId", "kwh", "readAt")));
        verifyNoInteractions(repo);
    }

    @Test void missingFieldsAreRejectedToo() throws Exception {
        mvc.perform(post("/api/readings").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[*].field", containsInAnyOrder("meterId", "kwh", "readAt")));
        verifyNoInteractions(repo);
    }

    @Test void rangeParsesIsoInstants() throws Exception {
        Instant from = Instant.parse("2026-10-01T00:00:00Z"), to = Instant.parse("2026-10-02T00:00:00Z");
        when(repo.findByMeterIdAndReadAtBetweenOrderByReadAtAsc("m1", from, to))
                .thenReturn(List.of(new MeterReading("m1", new BigDecimal("2.5"), from)));
        mvc.perform(get("/api/meters/m1/readings").param("from", "2026-10-01T00:00:00Z").param("to", "2026-10-02T00:00:00Z"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].kwh").value(2.5));
    }

    @Test void rangeNeedsBothBounds() throws Exception {
        mvc.perform(get("/api/meters/m1/readings").param("from", "2026-10-01T00:00:00Z"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(PROBLEM));
    }

    @Test void latestDefaultsToFive() throws Exception {
        when(repo.findByMeterIdOrderByReadAtDesc(eq("m1"), any())).thenReturn(List.of());
        mvc.perform(get("/api/meters/m1/latest")).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(0));
        ArgumentCaptor<Pageable> page = ArgumentCaptor.forClass(Pageable.class);
        verify(repo).findByMeterIdOrderByReadAtDesc(eq("m1"), page.capture());
        org.junit.jupiter.api.Assertions.assertEquals(5, page.getValue().getPageSize());
        org.junit.jupiter.api.Assertions.assertEquals(0, page.getValue().getPageNumber());
    }

    @Test void latestLimitIsBoundedTo1To100() throws Exception {
        for (String bad : new String[]{"0", "101", "-3"}) {
            mvc.perform(get("/api/meters/m1/latest").param("limit", bad))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentTypeCompatibleWith(PROBLEM));
        }
        mvc.perform(get("/api/meters/m1/latest").param("limit", "100")).andExpect(status().isOk());
        mvc.perform(get("/api/meters/m1/latest").param("limit", "abc")).andExpect(status().isBadRequest());
        verify(repo, times(1)).findByMeterIdOrderByReadAtDesc(any(), any());
    }

    @Test void totalReturnsMeterAndSum() throws Exception {
        when(repo.totalKwh("m1")).thenReturn(new BigDecimal("7.875"));
        mvc.perform(get("/api/meters/m1/total"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meterId").value("m1"))
                .andExpect(jsonPath("$.totalKwh").value(7.875));
    }
}
