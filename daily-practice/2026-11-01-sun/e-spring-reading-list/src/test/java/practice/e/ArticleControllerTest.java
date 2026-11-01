package practice.e;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.util.List;
import java.util.TreeSet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ArticleController.class)
class ArticleControllerTest {
    @Autowired MockMvc mvc;
    @MockBean ArticleService service;

    static final String OK_BODY = "{\"url\":\"https://x.example/a\",\"title\":\"A title\",\"tags\":[\"java\",\"spring\"]}";

    private ArticleView view(long id) {
        return new ArticleView(id, "https://x.example/a", "A title", new TreeSet<>(java.util.Set.of("java", "spring")), Instant.parse("2026-01-01T00:00:00Z"));
    }

    private void problem(org.springframework.test.web.servlet.ResultActions r, int status) throws Exception {
        r.andExpect(status().is(status)).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON));
    }

    @Test void createReturns201WithLocation() throws Exception {
        when(service.add(any())).thenReturn(view(7));
        mvc.perform(post("/api/articles").contentType(MediaType.APPLICATION_JSON).content(OK_BODY))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/articles/7"))
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.tags[0]").value("java"));
    }

    @Test void invalidBodiesAre400ProblemJsonAndNeverReachTheService() throws Exception {
        String[] bad = {
                "{\"url\":\"ftp://x\",\"title\":\"t\"}",
                "{\"title\":\"t\"}",
                "{\"url\":\"https://x.example\",\"title\":\"  \"}",
                "{\"url\":\"https://x.example\",\"title\":\"t\",\"tags\":[\"ok\",\"\"]}",
                "{\"url\":\"https://x.example\",\"title\":\"t\",\"tags\":[\"" + "x".repeat(21) + "\"]}",
                "{\"url\":\"https://x.example\",\"title\":\"" + "t".repeat(121) + "\"}",
                "{not json"};
        for (String b : bad) problem(mvc.perform(post("/api/articles").contentType(MediaType.APPLICATION_JSON).content(b)), 400);
        verifyNoInteractions(service);
    }

    @Test void nullTagsAreAccepted() throws Exception {
        when(service.add(any())).thenReturn(view(1));
        mvc.perform(post("/api/articles").contentType(MediaType.APPLICATION_JSON).content("{\"url\":\"http://x.example\",\"title\":\"t\"}"))
                .andExpect(status().isCreated());
    }

    @Test void serviceErrorsMapToProblemDetails() throws Exception {
        org.mockito.Mockito.doThrow(new DuplicateUrlException("https://x.example/a")).when(service).add(any());
        problem(mvc.perform(post("/api/articles").contentType(MediaType.APPLICATION_JSON).content(OK_BODY)), 409);
        org.mockito.Mockito.doThrow(new TooManyTagsException(5)).when(service).add(any());
        problem(mvc.perform(post("/api/articles").contentType(MediaType.APPLICATION_JSON).content(OK_BODY)), 400);
        when(service.get(99)).thenThrow(new ArticleNotFoundException(99));
        problem(mvc.perform(get("/api/articles/99")), 404);
    }

    @Test void getById() throws Exception {
        when(service.get(3)).thenReturn(view(3));
        mvc.perform(get("/api/articles/3")).andExpect(status().isOk()).andExpect(jsonPath("$.title").value("A title"));
        problem(mvc.perform(get("/api/articles/abc")), 400);
    }

    @Test void listUsesDefaultsAndPassesTheTag() throws Exception {
        when(service.byTag("java", 0, 20)).thenReturn(new PagedResponse<>(List.of(view(1)), 0, 20, 1));
        mvc.perform(get("/api/articles").param("tag", "java"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(1))
                .andExpect(jsonPath("$.items[0].id").value(1));
        verify(service).byTag("java", 0, 20);
    }

    @Test void listValidatesParameters() throws Exception {
        problem(mvc.perform(get("/api/articles").param("tag", "java").param("page", "-1")), 400);
        problem(mvc.perform(get("/api/articles").param("tag", "java").param("size", "0")), 400);
        problem(mvc.perform(get("/api/articles")), 400);                       // tag is required
        problem(mvc.perform(get("/api/articles").param("tag", "java").param("page", "x")), 400);
        verifyNoInteractions(service);
    }

    @Test void tagsEndpointIsNotCapturedByTheIdMapping() throws Exception {
        when(service.tagCounts()).thenReturn(List.of(new TagCountView("java", 3)));
        mvc.perform(get("/api/articles/tags")).andExpect(status().isOk()).andExpect(jsonPath("$[0].tag").value("java")).andExpect(jsonPath("$[0].count").value(3));
    }
}
