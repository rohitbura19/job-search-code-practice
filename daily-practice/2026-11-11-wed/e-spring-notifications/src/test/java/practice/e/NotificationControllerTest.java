package practice.e;

import static org.hamcrest.Matchers.endsWith;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import practice.e.Exceptions.InvalidTargetException;
import practice.e.Exceptions.NotFoundException;

@WebMvcTest(NotificationController.class)
@Import(ApiExceptionHandler.class)
class NotificationControllerTest {
    @Autowired MockMvc mvc;
    @MockBean NotificationService service;

    static final Instant T = Instant.parse("2026-11-11T08:00:00Z");
    static final Views.NotificationView VIEW = new Views.NotificationView(5L, 1L, Channel.EMAIL, "ann@x.io", "hello", T, false);

    @Test void sendReturns201AndLocation() throws Exception {
        when(service.send(new SendRequest(1L, Channel.EMAIL, "ann@x.io", "hello"))).thenReturn(VIEW);
        mvc.perform(post("/api/notifications").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":1,\"channel\":\"EMAIL\",\"target\":\"ann@x.io\",\"message\":\"hello\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", endsWith("/api/notifications/5")))
                .andExpect(jsonPath("$.target").value("ann@x.io"))
                .andExpect(jsonPath("$.read").value(false));
    }

    @Test void sendValidatesBody() throws Exception {
        String[] bad = {
                "{\"channel\":\"EMAIL\",\"target\":\"a@x.io\",\"message\":\"m\"}",
                "{\"userId\":0,\"channel\":\"EMAIL\",\"target\":\"a@x.io\",\"message\":\"m\"}",
                "{\"userId\":1,\"target\":\"a@x.io\",\"message\":\"m\"}",
                "{\"userId\":1,\"channel\":\"PUSH\",\"target\":\"a@x.io\",\"message\":\"m\"}",
                "{\"userId\":1,\"channel\":\"EMAIL\",\"target\":\" \",\"message\":\"m\"}",
                "{\"userId\":1,\"channel\":\"EMAIL\",\"target\":\"a@x.io\",\"message\":\"\"}",
                "{\"userId\":1,\"channel\":\"EMAIL\",\"target\":\"a@x.io\",\"message\":\"" + "m".repeat(501) + "\"}",
                "{\"userId\":1,\"channel\":\"EMAIL\",\"target\":\"" + "a".repeat(201) + "\",\"message\":\"m\"}",
                "{broken"};
        for (String body : bad)
            mvc.perform(post("/api/notifications").contentType(MediaType.APPLICATION_JSON).content(body))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentTypeCompatibleWith("application/problem+json"));
        verifyNoInteractions(service);
    }

    @Test void invalidTargetIs422ProblemDetail() throws Exception {
        when(service.send(any())).thenThrow(new InvalidTargetException("Not an e-mail address: x"));
        mvc.perform(post("/api/notifications").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":1,\"channel\":\"EMAIL\",\"target\":\"x\",\"message\":\"m\"}"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.title").value("Invalid target"))
                .andExpect(jsonPath("$.detail").value("Not an e-mail address: x"));
    }

    @Test void listUsesDefaultsAndHeader() throws Exception {
        when(service.list(7L, false, 0, 20)).thenReturn(new Views.PageView(List.of(VIEW), 0, 20, 1));
        mvc.perform(get("/api/notifications").header("X-User-Id", "7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(1))
                .andExpect(jsonPath("$.items[0].id").value(5))
                .andExpect(jsonPath("$.items[0].createdAt").value("2026-11-11T08:00:00Z"));
        when(service.list(7L, true, 2, 50)).thenReturn(new Views.PageView(List.of(), 2, 50, 0));
        mvc.perform(get("/api/notifications").header("X-User-Id", "7").param("unreadOnly", "true").param("page", "2").param("size", "50"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isEmpty());
    }

    @Test void listRejectsBadHeaderAndParams() throws Exception {
        mvc.perform(get("/api/notifications")).andExpect(status().isBadRequest());
        mvc.perform(get("/api/notifications").header("X-User-Id", "0")).andExpect(status().isBadRequest());
        mvc.perform(get("/api/notifications").header("X-User-Id", "abc")).andExpect(status().isBadRequest());
        mvc.perform(get("/api/notifications").header("X-User-Id", "1").param("size", "101")).andExpect(status().isBadRequest());
        mvc.perform(get("/api/notifications").header("X-User-Id", "1").param("size", "0")).andExpect(status().isBadRequest());
        mvc.perform(get("/api/notifications").header("X-User-Id", "1").param("page", "-1")).andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"));
        verifyNoInteractions(service);
    }

    @Test void unreadCountAndReadAll() throws Exception {
        when(service.unreadCount(3L)).thenReturn(4L);
        when(service.markAllRead(3L)).thenReturn(4);
        mvc.perform(get("/api/notifications/unread-count").header("X-User-Id", "3"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.unread").value(4));
        mvc.perform(post("/api/notifications/read-all").header("X-User-Id", "3"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.updated").value(4));
        mvc.perform(post("/api/notifications/read-all")).andExpect(status().isBadRequest());
    }

    @Test void markReadMapsNotFoundAndValidatesId() throws Exception {
        when(service.markRead(5L, 1L)).thenReturn(new Views.NotificationView(5L, 1L, Channel.EMAIL, "ann@x.io", "hello", T, true));
        when(service.markRead(404L, 1L)).thenThrow(new NotFoundException("Notification 404 not found"));
        mvc.perform(patch("/api/notifications/5/read").header("X-User-Id", "1"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.read").value(true));
        mvc.perform(patch("/api/notifications/404/read").header("X-User-Id", "1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.detail").value("Notification 404 not found"));
        mvc.perform(patch("/api/notifications/0/read").header("X-User-Id", "1")).andExpect(status().isBadRequest());
        mvc.perform(patch("/api/notifications/abc/read").header("X-User-Id", "1")).andExpect(status().isBadRequest());
        mvc.perform(patch("/api/notifications/5/read")).andExpect(status().isBadRequest());
    }
}
