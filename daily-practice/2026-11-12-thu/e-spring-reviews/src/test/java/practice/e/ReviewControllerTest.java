package practice.e;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import practice.e.Views.*;

@WebMvcTest(ReviewController.class)
class ReviewControllerTest {
    @Autowired MockMvc mvc;
    @MockBean ReviewService service;

    private static final String BODY = "{\"rating\":5,\"title\":\"Great\",\"body\":\"works\"}";
    private static ReviewView view(long id) { return new ReviewView(id, 7, 5, "Great", "works", ReviewStatus.PUBLISHED, Instant.parse("2026-11-12T08:00:00Z")); }

    @Test void createReturns201WithLocation() throws Exception {
        when(service.create(eq(3L), eq(7L), any())).thenReturn(view(42));
        mvc.perform(post("/api/products/3/reviews").header("X-User-Id", 7).contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.endsWith("/api/reviews/42")))
                .andExpect(jsonPath("$.id").value(42))
                .andExpect(jsonPath("$.status").value("PUBLISHED"));
        verify(service).create(eq(3L), eq(7L), eq(new CreateReview(5, "Great", "works")));
    }
    @Test void invalidBodyIs400() throws Exception {
        for (String bad : List.of("{\"rating\":6,\"title\":\"x\"}", "{\"rating\":0,\"title\":\"x\"}", "{\"rating\":3,\"title\":\"  \"}",
                "{\"rating\":3,\"title\":\"" + "x".repeat(81) + "\"}", "not json")) {
            mvc.perform(post("/api/products/3/reviews").header("X-User-Id", 7).contentType(MediaType.APPLICATION_JSON).content(bad))
                    .andExpect(status().isBadRequest());
        }
        verifyNoInteractions(service);
    }
    @Test void missingOrNonPositiveUserHeaderIs400() throws Exception {
        mvc.perform(post("/api/products/3/reviews").contentType(MediaType.APPLICATION_JSON).content(BODY)).andExpect(status().isBadRequest());
        mvc.perform(post("/api/products/3/reviews").header("X-User-Id", 0).contentType(MediaType.APPLICATION_JSON).content(BODY)).andExpect(status().isBadRequest());
        verifyNoInteractions(service);
    }
    @Test void duplicateIs409ProblemDetail() throws Exception {
        when(service.create(anyLong(), anyLong(), any())).thenThrow(new DuplicateReviewException(3, 7));
        mvc.perform(post("/api/products/3/reviews").header("X-User-Id", 7).contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isConflict())
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.title").value("Duplicate review"));
    }
    @Test void lostInsertRaceIsAlso409() throws Exception {
        when(service.create(anyLong(), anyLong(), any())).thenThrow(new DataIntegrityViolationException("uk_review_product_user"));
        mvc.perform(post("/api/products/3/reviews").header("X-User-Id", 7).contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isConflict());
    }
    @Test void listUsesDefaultsAndValidatesParams() throws Exception {
        when(service.list(anyLong(), anyInt(), anyInt())).thenReturn(new ReviewPage(List.of(view(1)), 0, 10, 1));
        mvc.perform(get("/api/products/3/reviews")).andExpect(status().isOk()).andExpect(jsonPath("$.items[0].id").value(1)).andExpect(jsonPath("$.total").value(1));
        verify(service).list(3L, 0, 10);
        mvc.perform(get("/api/products/3/reviews").param("size", "0")).andExpect(status().isBadRequest());
        mvc.perform(get("/api/products/3/reviews").param("size", "101")).andExpect(status().isBadRequest());
        mvc.perform(get("/api/products/3/reviews").param("page", "-1")).andExpect(status().isBadRequest());
        mvc.perform(get("/api/products/3/reviews").param("page", "abc")).andExpect(status().isBadRequest());
    }
    @Test void summary() throws Exception {
        when(service.summary(3L)).thenReturn(new Summary(4.5, 2, Map.of(4, 1L, 5, 1L)));
        mvc.perform(get("/api/products/3/summary")).andExpect(status().isOk()).andExpect(jsonPath("$.average").value(4.5)).andExpect(jsonPath("$.count").value(2));
    }
    @Test void getUnknownIs404() throws Exception {
        when(service.get(9L)).thenThrow(new ReviewNotFoundException(9));
        mvc.perform(get("/api/reviews/9")).andExpect(status().isNotFound());
    }
    @Test void deleteIs204Or403Or404() throws Exception {
        mvc.perform(delete("/api/reviews/1").header("X-User-Id", 7)).andExpect(status().isNoContent());
        verify(service).delete(1L, 7L);
        doThrow(new NotOwnerException()).when(service).delete(2L, 7L);
        mvc.perform(delete("/api/reviews/2").header("X-User-Id", 7)).andExpect(status().isForbidden());
        doThrow(new ReviewNotFoundException(3)).when(service).delete(3L, 7L);
        mvc.perform(delete("/api/reviews/3").header("X-User-Id", 7)).andExpect(status().isNotFound());
        mvc.perform(delete("/api/reviews/3")).andExpect(status().isBadRequest());
    }
}
