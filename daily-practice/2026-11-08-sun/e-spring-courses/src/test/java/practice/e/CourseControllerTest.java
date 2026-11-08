package practice.e;

import static org.hamcrest.Matchers.endsWith;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import practice.e.Exceptions.ConflictException;
import practice.e.Exceptions.NotFoundException;

@WebMvcTest(CourseController.class)
@Import(ApiExceptionHandler.class)
class CourseControllerTest {
    @Autowired MockMvc mvc;
    @MockBean CourseService service;

    private static final String VALID = "{\"title\":\"Java\",\"capacity\":3,\"startDate\":\"2026-11-01\",\"endDate\":\"2026-12-01\"}";
    private static final Views.CourseView VIEW = new Views.CourseView(7L, "Java", 3, LocalDate.of(2026, 11, 1), LocalDate.of(2026, 12, 1), List.of());

    @Test void createReturns201WithLocation() throws Exception {
        when(service.create(any())).thenReturn(VIEW);
        mvc.perform(post("/api/courses").contentType(MediaType.APPLICATION_JSON).content(VALID))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", endsWith("/api/courses/7")))
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.title").value("Java"));
    }

    @Test void createRejectsInvalidBody() throws Exception {
        String bad = "{\"title\":\"  \",\"capacity\":0,\"startDate\":null,\"endDate\":\"2026-12-01\"}";
        mvc.perform(post("/api/courses").contentType(MediaType.APPLICATION_JSON).content(bad))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.status").value(400));
        verifyNoInteractions(service);
    }

    @Test void createRejectsEndBeforeStartAndTooBigCapacity() throws Exception {
        mvc.perform(post("/api/courses").contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"x\",\"capacity\":3,\"startDate\":\"2026-12-01\",\"endDate\":\"2026-11-01\"}"))
                .andExpect(status().isBadRequest());
        mvc.perform(post("/api/courses").contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"x\",\"capacity\":501,\"startDate\":\"2026-11-01\",\"endDate\":\"2026-11-02\"}"))
                .andExpect(status().isBadRequest());
        mvc.perform(post("/api/courses").contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"" + "t".repeat(101) + "\",\"capacity\":5,\"startDate\":\"2026-11-01\",\"endDate\":\"2026-11-02\"}"))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(service);
    }

    @Test void conflictBecomes409ProblemDetail() throws Exception {
        when(service.create(any())).thenThrow(new ConflictException("course title already exists: Java"));
        mvc.perform(post("/api/courses").contentType(MediaType.APPLICATION_JSON).content(VALID))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.title").value("Conflict"))
                .andExpect(jsonPath("$.detail").value("course title already exists: Java"));
    }

    @Test void missingCourseIs404() throws Exception {
        when(service.get(99L)).thenThrow(new NotFoundException("course 99"));
        mvc.perform(get("/api/courses/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Not found"));
    }

    @Test void nonPositiveOrNonNumericIdIs400() throws Exception {
        mvc.perform(get("/api/courses/0")).andExpect(status().isBadRequest());
        mvc.perform(get("/api/courses/abc")).andExpect(status().isBadRequest());
        verifyNoInteractions(service);
    }

    @Test void literalPathWinsOverIdPath() throws Exception {
        when(service.enrollmentCounts()).thenReturn(List.of());
        mvc.perform(get("/api/courses/enrollment-counts")).andExpect(status().isOk()).andExpect(content().json("[]"));
        verify(service, never()).get(anyLong());
    }

    @Test void listUsesDefaultsAndSetsTotalHeader() throws Exception {
        var s = new Views.CourseSummary(1L, "Java", 3, LocalDate.of(2026, 11, 1), LocalDate.of(2026, 12, 1));
        when(service.upcoming(any(Pageable.class))).thenAnswer(inv -> new PageImpl<>(List.of(s), inv.getArgument(0), 41));
        mvc.perform(get("/api/courses"))
                .andExpect(status().isOk())
                .andExpect(header().string("X-Total-Count", "41"))
                .andExpect(jsonPath("$[0].title").value("Java"));
        var captor = org.mockito.ArgumentCaptor.forClass(Pageable.class);
        verify(service).upcoming(captor.capture());
        org.junit.jupiter.api.Assertions.assertEquals(PageRequest.of(0, 20, org.springframework.data.domain.Sort.by("schedule.endDate", "id")), captor.getValue());
    }

    @Test void listValidatesPaging() throws Exception {
        mvc.perform(get("/api/courses?size=1000")).andExpect(status().isBadRequest());
        mvc.perform(get("/api/courses?size=0")).andExpect(status().isBadRequest());
        mvc.perform(get("/api/courses?page=-1")).andExpect(status().isBadRequest());
        verifyNoInteractions(service);
    }

    @Test void enrollAndUnenrollReturn204() throws Exception {
        mvc.perform(put("/api/courses/7/students/3")).andExpect(status().isNoContent());
        mvc.perform(delete("/api/courses/7/students/3")).andExpect(status().isNoContent());
        verify(service).enroll(7L, 3L);
        verify(service).unenroll(7L, 3L);
    }

    @Test void enrollFullCourseIs409() throws Exception {
        doThrow(new ConflictException("course is full")).when(service).enroll(7L, 3L);
        mvc.perform(put("/api/courses/7/students/3")).andExpect(status().isConflict());
    }

    @Test void patchValidatesAndDelegates() throws Exception {
        when(service.patch(eq(7L), any())).thenReturn(VIEW);
        mvc.perform(patch("/api/courses/7").contentType(MediaType.APPLICATION_JSON).content("{\"capacity\":10}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(7));
        mvc.perform(patch("/api/courses/7").contentType(MediaType.APPLICATION_JSON).content("{\"capacity\":0}"))
                .andExpect(status().isBadRequest());
        mvc.perform(patch("/api/courses/7").contentType(MediaType.APPLICATION_JSON).content("{\"title\":\"   \"}"))
                .andExpect(status().isBadRequest());
    }

    @Test void createStudentValidatesEmail() throws Exception {
        when(service.createStudent(any())).thenReturn(new Views.StudentView(4L, "Ann", "ann@x.org"));
        mvc.perform(post("/api/students").contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"Ann\",\"email\":\"ann@x.org\"}"))
                .andExpect(status().isCreated()).andExpect(header().string("Location", endsWith("/api/students/4")));
        mvc.perform(post("/api/students").contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"Ann\",\"email\":\"nope\"}"))
                .andExpect(status().isBadRequest());
    }
}
