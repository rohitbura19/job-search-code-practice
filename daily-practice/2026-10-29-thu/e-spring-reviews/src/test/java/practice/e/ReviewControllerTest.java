package practice.e;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ReviewController.class)
class ReviewControllerTest {
    static final String PROBLEM = "application/problem+json";
    static final ReviewView VIEW = new ReviewView(11L, "p1", "asha", 5, "great", Instant.parse("2026-10-29T04:00:00Z"));
    static final String OK_BODY = "{\"author\":\"asha\",\"rating\":5,\"text\":\"great\"}";

    @Autowired MockMvc mvc;
    @MockBean ReviewService service;

    @Test void createIs201WithTheView() throws Exception {
        when(service.create(org.mockito.ArgumentMatchers.eq("p1"), any())).thenReturn(VIEW);
        mvc.perform(post("/api/products/p1/reviews").contentType(MediaType.APPLICATION_JSON).content(OK_BODY))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(11))
                .andExpect(jsonPath("$.author").value("asha"))
                .andExpect(jsonPath("$.rating").value(5));
    }

    @Test void invalidBodyIs400WithFieldErrors() throws Exception {
        mvc.perform(post("/api/products/p1/reviews").contentType(MediaType.APPLICATION_JSON).content("{\"author\":\"  \",\"rating\":6,\"text\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(PROBLEM))
                .andExpect(jsonPath("$.errors.author").exists())
                .andExpect(jsonPath("$.errors.rating").exists())
                .andExpect(jsonPath("$.errors.text").exists());
        mvc.perform(post("/api/products/p1/reviews").contentType(MediaType.APPLICATION_JSON).content("{\"author\":\"a\",\"text\":\"t\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.rating").exists());     // missing number -> 0 -> fails @Min(1)
        mvc.perform(post("/api/products/p1/reviews").contentType(MediaType.APPLICATION_JSON).content("{nope")).andExpect(status().isBadRequest());
        verifyNoInteractions(service);
    }

    @Test void tooLongTextIsRejected() throws Exception {
        String body = "{\"author\":\"a\",\"rating\":3,\"text\":\"" + "x".repeat(501) + "\"}";
        mvc.perform(post("/api/products/p1/reviews").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.errors.text").exists());
    }

    @Test void conflictsAre409AndDoNotLeakSql() throws Exception {
        when(service.create(any(), any())).thenThrow(new DuplicateReviewException("p1", "asha"));
        mvc.perform(post("/api/products/p1/reviews").contentType(MediaType.APPLICATION_JSON).content(OK_BODY))
                .andExpect(status().isConflict())
                .andExpect(content().contentTypeCompatibleWith(PROBLEM))
                .andExpect(jsonPath("$.detail").value("asha already reviewed p1"));
        doThrow(new DataIntegrityViolationException("could not execute statement; SQL [insert into product_review ...]")).when(service).create(any(), any());
        mvc.perform(post("/api/products/p1/reviews").contentType(MediaType.APPLICATION_JSON).content(OK_BODY))
                .andExpect(status().isConflict())
                .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("insert into"))));
    }

    @Test void listPassesPageAndClampsSizeToTheConfiguredMax() throws Exception {
        when(service.list(any(), anyInt(), anyInt())).thenReturn(new PageImpl<>(List.of(VIEW), PageRequest.of(1, 50), 51));
        mvc.perform(get("/api/products/p1/reviews").param("page", "1").param("size", "9999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].author").value("asha"))
                .andExpect(jsonPath("$.totalElements").value(51))
                .andExpect(jsonPath("$.totalPages").value(2));
        verify(service).list("p1", 1, 50);
    }

    @Test void listDefaultsAndLowerClamps() throws Exception {
        when(service.list(any(), anyInt(), anyInt())).thenReturn(new PageImpl<>(List.of()));
        mvc.perform(get("/api/products/p1/reviews")).andExpect(status().isOk());
        verify(service).list("p1", 0, 10);
        mvc.perform(get("/api/products/p2/reviews").param("size", "0").param("page", "-3")).andExpect(status().isOk());
        verify(service).list("p2", 0, 1);
        mvc.perform(get("/api/products/p3/reviews").param("size", "abc")).andExpect(status().isBadRequest());
    }

    @Test void ratingSummary() throws Exception {
        when(service.summary("p1")).thenReturn(new RatingSummary("p1", 2, 4.5));
        mvc.perform(get("/api/products/p1/rating"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(2))
                .andExpect(jsonPath("$.average").value(4.5));
    }

    @Test void hideIs204AndUnknownIs404() throws Exception {
        mvc.perform(post("/api/reviews/11/hide")).andExpect(status().isNoContent());
        verify(service).hide(11L);
        doThrow(new ReviewNotFoundException(404)).when(service).hide(404L);
        mvc.perform(post("/api/reviews/404/hide"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(PROBLEM))
                .andExpect(jsonPath("$.detail").value("review 404 not found"));
        mvc.perform(post("/api/reviews/abc/hide")).andExpect(status().isBadRequest());
    }
}
