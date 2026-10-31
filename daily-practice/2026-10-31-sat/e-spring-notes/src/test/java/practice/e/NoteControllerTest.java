package practice.e;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(NoteController.class)
class NoteControllerTest {
    @Autowired MockMvc mvc;
    @MockBean NoteService service;

    static final NoteView VIEW = new NoteView(5, "ann", "Groceries", "milk", 0, Instant.parse("2026-01-01T00:00:00Z"));

    String create(String owner, String title, String body) {
        return "{\"owner\":" + q(owner) + ",\"title\":" + q(title) + ",\"body\":" + q(body) + "}";
    }

    static String q(String s) { return s == null ? "null" : "\"" + s + "\""; }

    @Test void createReturns201WithLocation() throws Exception {
        when(service.create(new NoteRequest("ann", "Groceries", "milk"))).thenReturn(VIEW);
        mvc.perform(post("/api/notes").contentType(MediaType.APPLICATION_JSON).content(create("ann", "Groceries", "milk")))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/notes/5"))
                .andExpect(jsonPath("$.title").value("Groceries"));
    }

    @Test void createValidation() throws Exception {
        String longTitle = "x".repeat(101);
        for (String bad : new String[] {create(null, "t", "b"), create("Ann", "t", "b"), create("an", "t", "b"), create("ann", " ", "b"),
                create("ann", longTitle, "b"), create("ann", "t", "x".repeat(2001)), "{not json", "{\"owner\":\"ann\",\"title\":\"t\",\"surprise\":1}"}) {
            mvc.perform(post("/api/notes").contentType(MediaType.APPLICATION_JSON).content(bad))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentTypeCompatibleWith("application/problem+json"));
        }
    }

    @Test void getAndNotFound() throws Exception {
        when(service.get(5)).thenReturn(VIEW);
        when(service.get(6)).thenThrow(new NoteNotFoundException(6));
        mvc.perform(get("/api/notes/5")).andExpect(status().isOk()).andExpect(jsonPath("$.owner").value("ann"));
        mvc.perform(get("/api/notes/6")).andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.title").value("Note not found"));
        mvc.perform(get("/api/notes/abc")).andExpect(status().isBadRequest());
    }

    @Test void listPassesParamsAndAppliesDefaults() throws Exception {
        when(service.list("ann", "milk", 2, 5)).thenReturn(new PagedResponse<>(List.of(VIEW), 2, 5, 11, 3));
        when(service.list("ann", null, 0, 20)).thenReturn(new PagedResponse<>(List.of(), 0, 20, 0, 0));
        mvc.perform(get("/api/notes").param("owner", "ann").param("q", "milk").param("page", "2").param("size", "5"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.totalPages").value(3)).andExpect(jsonPath("$.content[0].id").value(5));
        mvc.perform(get("/api/notes").param("owner", "ann")).andExpect(status().isOk()).andExpect(jsonPath("$.size").value(20));
    }

    @Test void listRejectsBadParameters() throws Exception {
        mvc.perform(get("/api/notes")).andExpect(status().isBadRequest());                                           // owner missing
        mvc.perform(get("/api/notes").param("owner", "ann").param("page", "-1")).andExpect(status().isBadRequest());
        mvc.perform(get("/api/notes").param("owner", "ann").param("size", "0")).andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"));
    }

    @Test void updateValidatesAndMapsConflicts() throws Exception {
        when(service.update(eq(5L), any(NoteUpdate.class))).thenReturn(VIEW);
        mvc.perform(put("/api/notes/5").contentType(MediaType.APPLICATION_JSON).content("{\"title\":\"t\",\"body\":\"b\",\"version\":0}"))
                .andExpect(status().isOk());
        mvc.perform(put("/api/notes/5").contentType(MediaType.APPLICATION_JSON).content("{\"title\":\"t\",\"body\":\"b\"}"))
                .andExpect(status().isBadRequest());                                                                  // version missing
        when(service.update(eq(6L), any(NoteUpdate.class))).thenThrow(new StaleVersionException(6, 0, 3));
        mvc.perform(put("/api/notes/6").contentType(MediaType.APPLICATION_JSON).content("{\"title\":\"t\",\"version\":0}"))
                .andExpect(status().isConflict()).andExpect(jsonPath("$.title").value("Stale version"));
    }

    @Test void deleteAndRestore() throws Exception {
        mvc.perform(delete("/api/notes/5")).andExpect(status().isNoContent());
        verify(service).delete(5);
        doThrow(new NoteNotFoundException(9)).when(service).delete(9);
        mvc.perform(delete("/api/notes/9")).andExpect(status().isNotFound());
        when(service.restore(5)).thenReturn(VIEW);
        mvc.perform(post("/api/notes/5/restore")).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(5));
    }

    @Test void quotaAndDuplicateAreConflicts() throws Exception {
        when(service.create(new NoteRequest("ann", "t", null))).thenThrow(new QuotaExceededException("ann", 2));
        mvc.perform(post("/api/notes").contentType(MediaType.APPLICATION_JSON).content(create("ann", "t", null)))
                .andExpect(status().isConflict()).andExpect(jsonPath("$.title").value("Note quota exceeded"));
        when(service.create(new NoteRequest("bob", "t", null))).thenThrow(new DataIntegrityViolationException("could not execute statement; SQL [insert ...]"));
        mvc.perform(post("/api/notes").contentType(MediaType.APPLICATION_JSON).content(create("bob", "t", null)))
                .andExpect(status().isConflict()).andExpect(jsonPath("$.title").value("Duplicate title"))
                .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("insert"))));
    }
}
