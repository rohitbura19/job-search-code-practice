package practice.e;

import static org.hamcrest.Matchers.endsWith;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import practice.e.Exceptions.ConflictException;
import practice.e.Exceptions.NotFoundException;

@WebMvcTest(SubscriptionController.class)
@Import(ApiExceptionHandler.class)
class SubscriptionControllerTest {
    @Autowired MockMvc mvc;
    @MockBean SubscriptionService service;

    static final Instant T = Instant.parse("2026-11-10T08:00:00Z");
    static final Views.SubscriptionView VIEW = new Views.SubscriptionView(5L, "ann@x.io", Plan.PRO, SubscriptionStatus.ACTIVE, T, T.plusSeconds(86_400), 1500);

    @Test void createReturns201LocationAndEchoesRequestId() throws Exception {
        when(service.subscribe("ann@x.io", Plan.PRO)).thenReturn(VIEW);
        mvc.perform(post("/api/subscriptions").header("X-Request-Id", "abc-1").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"ann@x.io\",\"plan\":\"PRO\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", endsWith("/api/subscriptions/5")))
                .andExpect(header().string("X-Request-Id", "abc-1"))
                .andExpect(jsonPath("$.priceCents").value(1500))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test void createWithoutRequestIdHeaderSetsNone() throws Exception {
        when(service.subscribe(any(), any())).thenReturn(VIEW);
        mvc.perform(post("/api/subscriptions").contentType(MediaType.APPLICATION_JSON).content("{\"email\":\"ann@x.io\",\"plan\":\"FREE\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().doesNotExist("X-Request-Id"));
    }

    @Test void createValidatesBody() throws Exception {
        String[] bad = {"{\"email\":\"\",\"plan\":\"PRO\"}", "{\"email\":\"not-an-email\",\"plan\":\"PRO\"}", "{\"plan\":\"PRO\"}",
                "{\"email\":\"ann@x.io\"}", "{\"email\":\"ann@x.io\",\"plan\":\"GOLD\"}", "{not json", "{\"email\":\"" + "a".repeat(120) + "@x.io\",\"plan\":\"PRO\"}"};
        for (String body : bad)
            mvc.perform(post("/api/subscriptions").contentType(MediaType.APPLICATION_JSON).content(body))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentTypeCompatibleWith("application/problem+json"));
        verifyNoInteractions(service);
    }

    @Test void getById() throws Exception {
        when(service.get(5L)).thenReturn(VIEW);
        mvc.perform(get("/api/subscriptions/5")).andExpect(status().isOk()).andExpect(jsonPath("$.email").value("ann@x.io"));
        mvc.perform(get("/api/subscriptions/0")).andExpect(status().isBadRequest());
        mvc.perform(get("/api/subscriptions/abc")).andExpect(status().isBadRequest());
    }

    @Test void notFoundAndConflictBecomeProblemDetails() throws Exception {
        when(service.get(9L)).thenThrow(new NotFoundException("Subscription 9 not found"));
        mvc.perform(get("/api/subscriptions/9")).andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.detail").value("Subscription 9 not found"));
        when(service.changePlan(eq(5L), any())).thenThrow(new ConflictException("Subscription 5 is not active"));
        mvc.perform(put("/api/subscriptions/5/plan").contentType(MediaType.APPLICATION_JSON).content("{\"plan\":\"FREE\"}"))
                .andExpect(status().isConflict()).andExpect(jsonPath("$.detail").value("Subscription 5 is not active"));
    }

    @Test void lostRaceOnUniqueEmailIs409WithoutLeakingSql() throws Exception {
        when(service.subscribe(any(), any())).thenThrow(new DataIntegrityViolationException("could not execute statement [Unique index violation: UK_EMAIL]"));
        mvc.perform(post("/api/subscriptions").contentType(MediaType.APPLICATION_JSON).content("{\"email\":\"ann@x.io\",\"plan\":\"PRO\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value("Duplicate or invalid data"));
    }

    @Test void byEmailRequiresAWellFormedEmail() throws Exception {
        when(service.forEmail("ann@x.io")).thenReturn(List.of(VIEW));
        mvc.perform(get("/api/subscriptions").param("email", "ann@x.io")).andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value(5));
        mvc.perform(get("/api/subscriptions")).andExpect(status().isBadRequest());
        mvc.perform(get("/api/subscriptions").param("email", "")).andExpect(status().isBadRequest());
        mvc.perform(get("/api/subscriptions").param("email", "nope")).andExpect(status().isBadRequest());
    }

    @Test void changePlanValidatesBody() throws Exception {
        when(service.changePlan(5L, Plan.BASIC)).thenReturn(VIEW);
        mvc.perform(put("/api/subscriptions/5/plan").contentType(MediaType.APPLICATION_JSON).content("{\"plan\":\"BASIC\"}")).andExpect(status().isOk());
        mvc.perform(put("/api/subscriptions/5/plan").contentType(MediaType.APPLICATION_JSON).content("{}")).andExpect(status().isBadRequest());
        mvc.perform(put("/api/subscriptions/-1/plan").contentType(MediaType.APPLICATION_JSON).content("{\"plan\":\"BASIC\"}")).andExpect(status().isBadRequest());
    }

    @Test void deleteReturns204AndIsPositiveChecked() throws Exception {
        mvc.perform(delete("/api/subscriptions/5")).andExpect(status().isNoContent()).andExpect(content().string(""));
        verify(service).cancel(5L);
        mvc.perform(delete("/api/subscriptions/0")).andExpect(status().isBadRequest());
    }

    @Test void renewalsDefaultAndBounds() throws Exception {
        when(service.renewalsDue(Duration.ofDays(7))).thenReturn(List.of(VIEW));
        mvc.perform(get("/api/subscriptions/renewals")).andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value(5));
        mvc.perform(get("/api/subscriptions/renewals").param("withinDays", "90")).andExpect(status().isOk());
        mvc.perform(get("/api/subscriptions/renewals").param("withinDays", "0")).andExpect(status().isBadRequest());
        mvc.perform(get("/api/subscriptions/renewals").param("withinDays", "91")).andExpect(status().isBadRequest());
    }

    @Test void statsIsAJsonObjectKeyedByPlan() throws Exception {
        when(service.activeByPlan()).thenReturn(Map.of(Plan.PRO, 2L));
        mvc.perform(get("/api/subscriptions/stats")).andExpect(status().isOk()).andExpect(jsonPath("$.PRO").value(2));
    }
}
