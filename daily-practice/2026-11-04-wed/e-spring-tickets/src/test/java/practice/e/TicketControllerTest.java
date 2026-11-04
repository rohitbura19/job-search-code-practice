package practice.e;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import org.springframework.http.MediaType;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TicketController.class)
class TicketControllerTest {
    static final Instant T = Instant.parse("2026-11-04T10:00:00Z");
    static final String BODY = """
            {"title":"Printer jams","priority":"HIGH","assignee":"ann@x.com"}""";
    @Autowired MockMvc mvc;
    @MockBean TicketService service;

    static TicketView view(long id, Status s) { return new TicketView(id, "Printer jams", s, Priority.HIGH, "ann@x.com", T); }

    @Test void createReturns201WithLocation() throws Exception {
        given(service.create(any())).willReturn(view(7, Status.OPEN));
        mvc.perform(post("/api/tickets").contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/tickets/7"))
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.status").value("OPEN"))
                .andExpect(jsonPath("$.priority").value("HIGH"));
    }

    @Test void invalidBodiesAre400() throws Exception {
        given(service.create(any())).willReturn(view(1, Status.OPEN));
        for (String bad : List.of(
                BODY.replace("Printer jams", " "),
                BODY.replace("Printer jams", "x".repeat(101)),
                BODY.replace("Printer jams", "SERVER IS DOWN"),
                BODY.replace("HIGH", "SUPER"),
                BODY.replace(",\"priority\":\"HIGH\"", ""),
                BODY.replace("ann@x.com", "not-an-email"),
                "{}", "not json")) {
            mvc.perform(post("/api/tickets").contentType(MediaType.APPLICATION_JSON).content(bad)).andExpect(status().isBadRequest());
        }
        mvc.perform(post("/api/tickets").contentType(MediaType.APPLICATION_JSON).content(BODY.replace(",\"assignee\":\"ann@x.com\"", "")))
                .andExpect(status().isCreated());
    }

    @Test void getOneAndMissingIs404ProblemDetail() throws Exception {
        given(service.get(1L)).willReturn(view(1, Status.OPEN));
        given(service.get(2L)).willThrow(new TicketExceptions.NotFound(2L));
        mvc.perform(get("/api/tickets/1")).andExpect(status().isOk()).andExpect(jsonPath("$.title").value("Printer jams"));
        mvc.perform(get("/api/tickets/2")).andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Ticket not found"))
                .andExpect(jsonPath("$.detail").value("ticket 2 not found"))
                .andExpect(jsonPath("$.status").value(404));
        mvc.perform(get("/api/tickets/abc")).andExpect(status().isBadRequest());
    }

    @Test void patchStatus() throws Exception {
        given(service.transition(1L, Status.RESOLVED)).willReturn(view(1, Status.RESOLVED));
        mvc.perform(patch("/api/tickets/1/status").contentType(MediaType.APPLICATION_JSON).content("{\"status\":\"RESOLVED\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.status").value("RESOLVED"));
        mvc.perform(patch("/api/tickets/1/status").contentType(MediaType.APPLICATION_JSON).content("{}")).andExpect(status().isBadRequest());
        mvc.perform(patch("/api/tickets/1/status").contentType(MediaType.APPLICATION_JSON).content("{\"status\":\"DONE\"}")).andExpect(status().isBadRequest());
    }

    @Test void illegalTransitionIs409() throws Exception {
        given(service.transition(1L, Status.CLOSED)).willThrow(new TicketExceptions.IllegalTransition(Status.IN_PROGRESS, Status.CLOSED));
        mvc.perform(patch("/api/tickets/1/status").contentType(MediaType.APPLICATION_JSON).content("{\"status\":\"CLOSED\"}"))
                .andExpect(status().isConflict())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Illegal status change"))
                .andExpect(jsonPath("$.detail").value("cannot move a ticket from IN_PROGRESS to CLOSED"));
    }

    @Test void optimisticLockFailureIs409() throws Exception {
        given(service.transition(eq(1L), any())).willThrow(new ObjectOptimisticLockingFailureException(Ticket.class, 1L));
        mvc.perform(patch("/api/tickets/1/status").contentType(MediaType.APPLICATION_JSON).content("{\"status\":\"CLOSED\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.title").value("Concurrent update"))
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test void searchDefaultsAndParameters() throws Exception {
        given(service.search(null, null, 0, 20)).willReturn(new TicketPage(List.of(view(1, Status.OPEN)), true));
        mvc.perform(get("/api/tickets")).andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1)).andExpect(jsonPath("$.hasNext").value(true));
        given(service.search(Status.OPEN, "ann@x.com", 2, 5)).willReturn(new TicketPage(List.of(), false));
        mvc.perform(get("/api/tickets").param("status", "OPEN").param("assignee", "ann@x.com").param("page", "2").param("size", "5"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.hasNext").value(false));
        mvc.perform(get("/api/tickets").param("status", "FOO")).andExpect(status().isBadRequest());
    }
}
