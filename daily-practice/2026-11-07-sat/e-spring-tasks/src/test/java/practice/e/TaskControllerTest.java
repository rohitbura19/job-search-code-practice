package practice.e;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TaskController.class)
class TaskControllerTest {
    @Autowired MockMvc mvc;
    @MockBean TaskService service;

    private static final Instant NOW = Instant.parse("2026-11-07T00:00:00Z");

    private static TaskView view(long id) { return new TaskView(id, "t", TaskStatus.TODO, NOW, NOW, List.of()); }

    @Test void createReturns201WithLocation() throws Exception {
        given(service.create(any())).willReturn(view(5));
        mvc.perform(post("/tasks").contentType(MediaType.APPLICATION_JSON).content("{\"title\":\"t\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/tasks/5"))
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.status").value("TODO"))
                .andExpect(jsonPath("$.comments").isEmpty());
    }

    @Test void createValidatesBody() throws Exception {
        for (String body : new String[]{"{\"title\":\"\"}", "{\"title\":\"   \"}", "{}", "{\"title\":\"" + "x".repeat(101) + "\"}"})
            mvc.perform(post("/tasks").contentType(MediaType.APPLICATION_JSON).content(body))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                    .andExpect(jsonPath("$.status").value(400));
        verifyNoInteractions(service);
    }

    @Test void malformedJsonIs400() throws Exception {
        mvc.perform(post("/tasks").contentType(MediaType.APPLICATION_JSON).content("{nope"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"));
    }

    @Test void getOne() throws Exception {
        given(service.get(7L)).willReturn(view(7));
        mvc.perform(get("/tasks/7")).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(7));
    }

    @Test void notFoundIsProblemDetail() throws Exception {
        given(service.get(8L)).willThrow(new Exceptions.NotFoundException("Task 8 not found"));
        mvc.perform(get("/tasks/8"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.detail").value("Task 8 not found"))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test void pathIdMustBePositiveAndNumeric() throws Exception {
        mvc.perform(get("/tasks/0")).andExpect(status().isBadRequest());
        mvc.perform(get("/tasks/abc")).andExpect(status().isBadRequest());
        verifyNoInteractions(service);
    }

    @Test void listPassesParametersAndSetsTotalHeader() throws Exception {
        var page = new PageImpl<>(List.of(new TaskSummary(1L, "a", TaskStatus.DOING, NOW)), PageRequest.of(1, 1), 3);
        given(service.list(TaskStatus.DOING, 1, 1, "title,desc")).willReturn(page);
        mvc.perform(get("/tasks").param("status", "DOING").param("page", "1").param("size", "1").param("sort", "title,desc"))
                .andExpect(status().isOk())
                .andExpect(header().string("X-Total-Count", "3"))
                .andExpect(jsonPath("$[0].title").value("a"))
                .andExpect(jsonPath("$[0].comments").doesNotExist());
    }

    @Test void listDefaults() throws Exception {
        given(service.list(null, 0, 20, null)).willReturn(new PageImpl<>(List.of()));
        mvc.perform(get("/tasks")).andExpect(status().isOk()).andExpect(header().string("X-Total-Count", "0")).andExpect(content().json("[]"));
    }

    @Test void listRejectsBadParameters() throws Exception {
        mvc.perform(get("/tasks").param("size", "0")).andExpect(status().isBadRequest());
        mvc.perform(get("/tasks").param("size", "101")).andExpect(status().isBadRequest());
        mvc.perform(get("/tasks").param("page", "-1")).andExpect(status().isBadRequest());
        mvc.perform(get("/tasks").param("status", "NOPE")).andExpect(status().isBadRequest());
        verifyNoInteractions(service);
    }

    @Test void badSortFromServiceIs400() throws Exception {
        given(service.list(any(), anyInt(), anyInt(), eq("password"))).willThrow(new IllegalArgumentException("Unsupported sort: password"));
        mvc.perform(get("/tasks").param("sort", "password"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("Unsupported sort: password"));
    }

    @Test void statsStaticPathBeatsIdPattern() throws Exception {
        Map<TaskStatus, Long> m = new EnumMap<>(TaskStatus.class);
        m.put(TaskStatus.TODO, 2L);
        m.put(TaskStatus.DOING, 0L);
        m.put(TaskStatus.DONE, 1L);
        given(service.stats()).willReturn(m);
        mvc.perform(get("/tasks/stats")).andExpect(status().isOk()).andExpect(jsonPath("$.TODO").value(2)).andExpect(jsonPath("$.DONE").value(1));
    }

    @Test void changeStatus() throws Exception {
        given(service.changeStatus(3L, TaskStatus.DONE)).willReturn(view(3));
        mvc.perform(put("/tasks/3/status").contentType(MediaType.APPLICATION_JSON).content("{\"status\":\"DONE\"}")).andExpect(status().isOk());
        mvc.perform(put("/tasks/3/status").contentType(MediaType.APPLICATION_JSON).content("{\"status\":\"BOGUS\"}")).andExpect(status().isBadRequest());
        mvc.perform(put("/tasks/3/status").contentType(MediaType.APPLICATION_JSON).content("{}")).andExpect(status().isBadRequest());
    }

    @Test void conflictIs409() throws Exception {
        given(service.changeStatus(3L, TaskStatus.TODO)).willThrow(new Exceptions.ConflictException("Task 3 is DONE"));
        mvc.perform(put("/tasks/3/status").contentType(MediaType.APPLICATION_JSON).content("{\"status\":\"TODO\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value("Task 3 is DONE"));
    }

    @Test void addCommentCreated() throws Exception {
        given(service.addComment(eq(1L), any())).willReturn(new CommentView(9L, "ann", "hi"));
        mvc.perform(post("/tasks/1/comments").contentType(MediaType.APPLICATION_JSON).content("{\"author\":\"ann\",\"text\":\"hi\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/tasks/1/comments/9"))
                .andExpect(jsonPath("$.id").value(9));
    }

    @Test void addCommentValidation() throws Exception {
        for (String body : new String[]{"{\"author\":\"\",\"text\":\"hi\"}", "{\"author\":\"a\"}", "{\"author\":\"a\",\"text\":\"" + "x".repeat(501) + "\"}"})
            mvc.perform(post("/tasks/1/comments").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isBadRequest());
        verifyNoInteractions(service);
    }

    @Test void deleteComment() throws Exception {
        mvc.perform(delete("/tasks/1/comments/2")).andExpect(status().isNoContent());
        verify(service).removeComment(1L, 2L);
        doThrow(new Exceptions.NotFoundException("nope")).when(service).removeComment(1L, 3L);
        mvc.perform(delete("/tasks/1/comments/3")).andExpect(status().isNotFound());
        mvc.perform(delete("/tasks/1/comments/0")).andExpect(status().isBadRequest());
    }
}
