package practice.e;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(RefundController.class)
class RefundControllerTest {
    static final String PROBLEM = "application/problem+json";
    static final String KEY = "key-12345678";
    static final RefundView VIEW = new RefundView(42L, "pay-1", 500, RefundStatus.APPROVED, Instant.parse("2026-10-26T04:00:00Z"));

    @Autowired MockMvc mvc;
    @MockBean RefundService service;

    static String body(String pay, String amt) { return "{\"paymentId\":" + pay + ",\"amountPaise\":" + amt + "}"; }

    @Test void newRefundIs201WithLocation() throws Exception {
        when(service.create(eq(KEY), any())).thenReturn(new CreateResult(VIEW, false));
        mvc.perform(post("/api/refunds").header("Idempotency-Key", KEY).contentType(MediaType.APPLICATION_JSON).content(body("\"pay-1\"", "500")))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/refunds/42"))
                .andExpect(header().doesNotExist("Idempotent-Replayed"))
                .andExpect(jsonPath("$.id").value(42))
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test void replayIs200WithTheSameLocationAndAMarker() throws Exception {
        when(service.create(eq(KEY), any())).thenReturn(new CreateResult(VIEW, true));
        mvc.perform(post("/api/refunds").header("Idempotency-Key", KEY).contentType(MediaType.APPLICATION_JSON).content(body("\"pay-1\"", "500")))
                .andExpect(status().isOk())
                .andExpect(header().string("Location", "/api/refunds/42"))
                .andExpect(header().string("Idempotent-Replayed", "true"));
    }

    @Test void theKeyHeaderIsRequiredAndChecked() throws Exception {
        mvc.perform(post("/api/refunds").contentType(MediaType.APPLICATION_JSON).content(body("\"pay-1\"", "500")))
                .andExpect(status().isBadRequest()).andExpect(content().contentTypeCompatibleWith(PROBLEM));
        mvc.perform(post("/api/refunds").header("Idempotency-Key", "short").contentType(MediaType.APPLICATION_JSON).content(body("\"pay-1\"", "500")))
                .andExpect(status().isBadRequest()).andExpect(content().contentTypeCompatibleWith(PROBLEM));
        mvc.perform(post("/api/refunds").header("Idempotency-Key", "short").contentType(MediaType.APPLICATION_JSON).content(body("\"pay-1\"", "500")))
                .andExpect(jsonPath("$.errors[*].field", containsInAnyOrder("idempotencyKey")));
        mvc.perform(post("/api/refunds").header("Idempotency-Key", "has spaces in it").contentType(MediaType.APPLICATION_JSON).content(body("\"pay-1\"", "500")))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(service);
    }

    @Test void invalidBodyListsEveryBadField() throws Exception {
        mvc.perform(post("/api/refunds").header("Idempotency-Key", KEY).contentType(MediaType.APPLICATION_JSON).content(body("\" \"", "-5")))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(PROBLEM))
                .andExpect(jsonPath("$.title").value("Validation failed"))
                .andExpect(jsonPath("$.errors[*].field", containsInAnyOrder("paymentId", "amountPaise")));
        mvc.perform(post("/api/refunds").header("Idempotency-Key", KEY).contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[*].field", containsInAnyOrder("paymentId", "amountPaise")));
        verifyNoInteractions(service);
    }

    @Test void businessFailuresBecomeProblemJson() throws Exception {
        when(service.create(eq(KEY), any())).thenThrow(RefundException.tooLarge(10, 5));
        mvc.perform(post("/api/refunds").header("Idempotency-Key", KEY).contentType(MediaType.APPLICATION_JSON).content(body("\"pay-1\"", "500")))
                .andExpect(status().isUnprocessableEntity()).andExpect(content().contentTypeCompatibleWith(PROBLEM))
                .andExpect(jsonPath("$.title").value("Refund too large"));
        when(service.get(5)).thenThrow(RefundException.notFound(5));
        mvc.perform(get("/api/refunds/5")).andExpect(status().isNotFound()).andExpect(jsonPath("$.title").value("Refund not found"));
    }

    @Test void getByIdAndANonNumericIdIs400() throws Exception {
        when(service.get(42)).thenReturn(VIEW);
        mvc.perform(get("/api/refunds/42")).andExpect(status().isOk()).andExpect(jsonPath("$.paymentId").value("pay-1"));
        mvc.perform(get("/api/refunds/abc")).andExpect(status().isBadRequest()).andExpect(content().contentTypeCompatibleWith(PROBLEM));
    }

    @Test void listPassesThePageableAndCapsTheSize() throws Exception {
        when(service.list(any(), any())).thenReturn(new PageView<>(List.of(VIEW), 2, 5, 11, 3));
        mvc.perform(get("/api/refunds").param("paymentId", "pay-1").param("page", "2").param("size", "5").param("sort", "createdAt,desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].id").value(42))
                .andExpect(jsonPath("$.totalPages").value(3));
        ArgumentCaptor<Pageable> pg = ArgumentCaptor.forClass(Pageable.class);
        verify(service).list(eq("pay-1"), pg.capture());
        assertEquals(2, pg.getValue().getPageNumber());
        assertEquals(5, pg.getValue().getPageSize());
        assertEquals(Sort.Order.desc("createdAt"), pg.getValue().getSort().getOrderFor("createdAt"));

        mvc.perform(get("/api/refunds").param("size", "100000")).andExpect(status().isOk());
        mvc.perform(get("/api/refunds")).andExpect(status().isOk());
        ArgumentCaptor<Pageable> all = ArgumentCaptor.forClass(Pageable.class);
        verify(service, times(2)).list(eq(null), all.capture());
        assertEquals(50, all.getAllValues().get(0).getPageSize(), "max-page-size from application.yml");
        assertEquals(20, all.getAllValues().get(1).getPageSize(), "default-page-size from application.yml");
    }

    @Test void decision() throws Exception {
        when(service.decide(42, true)).thenReturn(VIEW);
        mvc.perform(post("/api/refunds/42/decision").contentType(MediaType.APPLICATION_JSON).content("{\"approved\":true}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(42));
        mvc.perform(post("/api/refunds/42/decision").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.errors[*].field", containsInAnyOrder("approved")));
        when(service.decide(43, false)).thenThrow(RefundException.alreadyDecided(43, RefundStatus.APPROVED));
        mvc.perform(post("/api/refunds/43/decision").contentType(MediaType.APPLICATION_JSON).content("{\"approved\":false}"))
                .andExpect(status().isConflict()).andExpect(jsonPath("$.title").value("Already decided"));
    }

    @Test void aLostOptimisticLockRaceIs409() throws Exception {
        when(service.decide(44, true)).thenThrow(new ObjectOptimisticLockingFailureException(Refund.class, 44L));
        mvc.perform(post("/api/refunds/44/decision").contentType(MediaType.APPLICATION_JSON).content("{\"approved\":true}"))
                .andExpect(status().isConflict())
                .andExpect(content().contentTypeCompatibleWith(PROBLEM))
                .andExpect(jsonPath("$.title").value("Concurrent update"));
    }
}
