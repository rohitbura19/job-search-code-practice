package practice.e;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/** E - controller + validation + ProblemDetail. The service is mocked. Run: mvn test (inside e-spring-support-case). */
@WebMvcTest(CaseController.class)
@Import(ApiExceptionHandler.class)
class CaseControllerTest {

    @Autowired MockMvc mvc;
    @MockBean CaseService service;            // Spring Boot 3.4+: @MockitoBean

    static final UUID ID = UUID.fromString("11111111-2222-3333-4444-555555555555");
    static CaseView view(long version, CaseStatus status) {
        return new CaseView(ID, version, "EVC-0001", Severity.HIGH, status, "no power", Instant.parse("2026-10-21T10:00:00Z"));
    }
    static final String VALID = """
            {"chargerSerial":"EVC-0001","severity":"HIGH","summary":"no power","contactEmail":"ops@example.com"}""";

    // ---------- POST ----------

    @Test @DisplayName("POST valid -> 201 with Location, ETag and body")
    void createOk() throws Exception {
        when(service.open(any())).thenReturn(view(0, CaseStatus.OPEN));
        mvc.perform(post("/api/v1/cases").contentType(MediaType.APPLICATION_JSON).content(VALID))
           .andExpect(status().isCreated())
           .andExpect(header().string(HttpHeaders.LOCATION, endsWith("/api/v1/cases/" + ID)))
           .andExpect(header().string(HttpHeaders.ETAG, "\"0\""))
           .andExpect(jsonPath("$.id").value(ID.toString()))
           .andExpect(jsonPath("$.status").value("OPEN"));
    }

    @Test @DisplayName("POST invalid -> 400 problem+json listing exactly the 3 bad fields; service never called")
    void createInvalid() throws Exception {
        mvc.perform(post("/api/v1/cases").contentType(MediaType.APPLICATION_JSON)
                .content("{\"chargerSerial\":\"ab\",\"severity\":\"HIGH\",\"summary\":\" \",\"contactEmail\":\"nope\"}"))
           .andExpect(status().isBadRequest())
           .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
           .andExpect(jsonPath("$.title").value("Validation failed"))
           .andExpect(jsonPath("$.errors", hasSize(3)))
           .andExpect(jsonPath("$.errors[*].field", containsInAnyOrder("chargerSerial", "summary", "contactEmail")));
        verifyNoInteractions(service);
    }

    @Test @DisplayName("a blank serial is ONE error with the composed message (@ReportAsSingleViolation)")
    void blankSerialSingleViolation() throws Exception {
        mvc.perform(post("/api/v1/cases").contentType(MediaType.APPLICATION_JSON)
                .content("{\"chargerSerial\":\" \",\"severity\":\"HIGH\",\"summary\":\"x\",\"contactEmail\":\"a@b.example\"}"))
           .andExpect(status().isBadRequest())
           .andExpect(jsonPath("$.errors", hasSize(1)))
           .andExpect(jsonPath("$.errors[0].field").value("chargerSerial"))
           .andExpect(jsonPath("$.errors[0].message").value("must be 6 to 32 characters: A-Z, 0-9 and '-'"));
    }

    @Test @DisplayName("missing severity and a 201-character summary are rejected")
    void requiredAndSize() throws Exception {
        String longSummary = "x".repeat(201);
        mvc.perform(post("/api/v1/cases").contentType(MediaType.APPLICATION_JSON)
                .content("{\"chargerSerial\":\"EVC-0001\",\"summary\":\"" + longSummary + "\",\"contactEmail\":\"a@b.example\"}"))
           .andExpect(status().isBadRequest())
           .andExpect(jsonPath("$.errors[*].field", containsInAnyOrder("severity", "summary")));
    }

    @Test @DisplayName("malformed JSON -> 400 problem+json (handled by the base class)")
    void malformedJson() throws Exception {
        mvc.perform(post("/api/v1/cases").contentType(MediaType.APPLICATION_JSON).content("{not json"))
           .andExpect(status().isBadRequest())
           .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON));
        verifyNoInteractions(service);
    }

    // ---------- PATCH ----------

    @Test @DisplayName("PATCH with If-Match -> 200, new ETag, version passed to the service")
    void updateOk() throws Exception {
        when(service.update(eq(ID), eq(3L), any())).thenReturn(view(4, CaseStatus.RESOLVED));
        mvc.perform(patch("/api/v1/cases/{id}", ID).header(HttpHeaders.IF_MATCH, "\"3\"")
                .contentType(MediaType.APPLICATION_JSON).content("{\"status\":\"RESOLVED\"}"))
           .andExpect(status().isOk())
           .andExpect(header().string(HttpHeaders.ETAG, "\"4\""))
           .andExpect(jsonPath("$.version").value(4));
        verify(service).update(eq(ID), eq(3L), any());
    }

    @Test @DisplayName("PATCH without If-Match -> 428")
    void missingIfMatch() throws Exception {
        mvc.perform(patch("/api/v1/cases/{id}", ID).contentType(MediaType.APPLICATION_JSON).content("{\"status\":\"RESOLVED\"}"))
           .andExpect(status().isPreconditionRequired())
           .andExpect(jsonPath("$.title").value("If-Match required"));
        verifyNoInteractions(service);
    }

    @Test @DisplayName("PATCH with a non-numeric If-Match -> 428")
    void badIfMatch() throws Exception {
        mvc.perform(patch("/api/v1/cases/{id}", ID).header(HttpHeaders.IF_MATCH, "\"abc\"")
                .contentType(MediaType.APPLICATION_JSON).content("{\"status\":\"RESOLVED\"}"))
           .andExpect(status().isPreconditionRequired());
    }

    @Test @DisplayName("PATCH without status -> 400")
    void missingStatus() throws Exception {
        mvc.perform(patch("/api/v1/cases/{id}", ID).header(HttpHeaders.IF_MATCH, "\"3\"")
                .contentType(MediaType.APPLICATION_JSON).content("{}"))
           .andExpect(status().isBadRequest())
           .andExpect(jsonPath("$.errors[0].field").value("status"));
    }

    @Test @DisplayName("stale version -> 409 Stale version")
    void stale() throws Exception {
        when(service.update(eq(ID), eq(3L), any())).thenThrow(new StaleVersionException(ID));
        mvc.perform(patch("/api/v1/cases/{id}", ID).header(HttpHeaders.IF_MATCH, "\"3\"")
                .contentType(MediaType.APPLICATION_JSON).content("{\"status\":\"RESOLVED\"}"))
           .andExpect(status().isConflict())
           .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
           .andExpect(jsonPath("$.title").value("Stale version"));
    }

    @Test @DisplayName("illegal transition -> 409 Invalid status change")
    void invalidTransition() throws Exception {
        when(service.update(eq(ID), eq(3L), any())).thenThrow(new InvalidTransitionException(CaseStatus.CLOSED, CaseStatus.OPEN));
        mvc.perform(patch("/api/v1/cases/{id}", ID).header(HttpHeaders.IF_MATCH, "\"3\"")
                .contentType(MediaType.APPLICATION_JSON).content("{\"status\":\"OPEN\"}"))
           .andExpect(status().isConflict())
           .andExpect(jsonPath("$.title").value("Invalid status change"));
    }

    @Test @DisplayName("unknown id -> 404 Case not found")
    void notFound() throws Exception {
        when(service.update(eq(ID), anyLong(), any())).thenThrow(new CaseNotFoundException(ID));
        mvc.perform(patch("/api/v1/cases/{id}", ID).header(HttpHeaders.IF_MATCH, "\"3\"")
                .contentType(MediaType.APPLICATION_JSON).content("{\"status\":\"RESOLVED\"}"))
           .andExpect(status().isNotFound())
           .andExpect(jsonPath("$.title").value("Case not found"));
    }

    // ---------- GET ----------

    @Test @DisplayName("GET list -> PagedModel shape; default page size 20 sorted by createdAt DESC")
    void listDefaults() throws Exception {
        when(service.list(isNull(), any())).thenReturn(new PageImpl<>(List.of(view(0, CaseStatus.OPEN)), PageRequest.of(0, 20), 1));
        mvc.perform(get("/api/v1/cases"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.content[0].id").value(ID.toString()))
           .andExpect(jsonPath("$.page.totalElements").value(1));
        ArgumentCaptor<Pageable> p = ArgumentCaptor.forClass(Pageable.class);
        verify(service).list(isNull(), p.capture());
        assertEquals(20, p.getValue().getPageSize());
        assertEquals(Sort.Direction.DESC, p.getValue().getSort().getOrderFor("createdAt").getDirection());
    }

    @Test @DisplayName("GET with status filter and size above the cap -> status passed, size capped at 100")
    void listFilterAndCap() throws Exception {
        when(service.list(eq(CaseStatus.OPEN), any())).thenReturn(new PageImpl<>(List.of(), PageRequest.of(0, 100), 0));
        mvc.perform(get("/api/v1/cases").param("status", "OPEN").param("size", "5000"))
           .andExpect(status().isOk());
        ArgumentCaptor<Pageable> p = ArgumentCaptor.forClass(Pageable.class);
        verify(service).list(eq(CaseStatus.OPEN), p.capture());
        assertEquals(100, p.getValue().getPageSize());
    }
}
