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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import practice.e.Exceptions.ConflictException;
import practice.e.Exceptions.NotFoundException;

@WebMvcTest(InvoiceController.class)
@Import(ApiExceptionHandler.class)
class InvoiceControllerTest {
    @Autowired MockMvc mvc;
    @MockBean InvoiceService service;

    static final String VALID = "{\"customer\":\"Acme\",\"items\":[{\"description\":\"pen\",\"quantity\":2,\"unitPriceCents\":150}]}";
    static final Views.InvoiceView VIEW = new Views.InvoiceView(7L, "Acme", InvoiceStatus.DRAFT, List.of(new LineItem("pen", 2, 150)), 300, null);

    @Test void createReturns201WithLocation() throws Exception {
        when(service.create(any())).thenReturn(VIEW);
        mvc.perform(post("/api/invoices").contentType(MediaType.APPLICATION_JSON).content(VALID))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", endsWith("/api/invoices/7")))
                .andExpect(jsonPath("$.totalCents").value(300))
                .andExpect(jsonPath("$.status").value("DRAFT"))
                .andExpect(jsonPath("$.items[0].description").value("pen"));
    }

    @Test void createValidatesNestedItems() throws Exception {
        String[] bad = {
                "{\"customer\":\" \",\"items\":[{\"description\":\"p\",\"quantity\":1,\"unitPriceCents\":1}]}",
                "{\"customer\":\"A\",\"items\":[]}",
                "{\"customer\":\"A\"}",
                "{\"customer\":\"A\",\"items\":[{\"description\":\"p\",\"quantity\":0,\"unitPriceCents\":1}]}",
                "{\"customer\":\"A\",\"items\":[{\"description\":\"p\",\"quantity\":1001,\"unitPriceCents\":1}]}",
                "{\"customer\":\"A\",\"items\":[{\"description\":\"p\",\"quantity\":1,\"unitPriceCents\":0}]}",
                "{\"customer\":\"A\",\"items\":[{\"description\":\"\",\"quantity\":1,\"unitPriceCents\":5}]}",
                "{\"customer\":\"" + "x".repeat(41) + "\",\"items\":[{\"description\":\"p\",\"quantity\":1,\"unitPriceCents\":5}]}",
                "{not json"};
        for (String body : bad)
            mvc.perform(post("/api/invoices").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isBadRequest());
        verifyNoInteractions(service);
    }

    @Test void getAndNotFoundProblemDetail() throws Exception {
        when(service.get(7L)).thenReturn(VIEW);
        when(service.get(8L)).thenThrow(new NotFoundException("Invoice 8 not found"));
        mvc.perform(get("/api/invoices/7")).andExpect(status().isOk()).andExpect(jsonPath("$.customer").value("Acme"));
        mvc.perform(get("/api/invoices/8")).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404)).andExpect(jsonPath("$.detail").value("Invoice 8 not found"));
        mvc.perform(get("/api/invoices/0")).andExpect(status().isBadRequest());
        mvc.perform(get("/api/invoices/abc")).andExpect(status().isBadRequest());
    }

    @Test void listPagesWithTotalHeader() throws Exception {
        Pageable pr = PageRequest.of(1, 2, org.springframework.data.domain.Sort.by("id"));
        when(service.list(eq(InvoiceStatus.ISSUED), any())).thenReturn(new PageImpl<>(List.of(new Views.InvoiceSummary(3L, "Zeta", InvoiceStatus.ISSUED)), pr, 5));
        mvc.perform(get("/api/invoices").param("status", "ISSUED").param("page", "1").param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(header().string("X-Total-Count", "5"))
                .andExpect(jsonPath("$[0].customer").value("Zeta"));
        verify(service).list(InvoiceStatus.ISSUED, pr);
    }

    @Test void listRejectsBadParameters() throws Exception {
        mvc.perform(get("/api/invoices")).andExpect(status().isBadRequest());
        mvc.perform(get("/api/invoices").param("status", "NOPE")).andExpect(status().isBadRequest());
        mvc.perform(get("/api/invoices").param("status", "DRAFT").param("size", "0")).andExpect(status().isBadRequest());
        mvc.perform(get("/api/invoices").param("status", "DRAFT").param("size", "101")).andExpect(status().isBadRequest());
        mvc.perform(get("/api/invoices").param("status", "DRAFT").param("page", "-1")).andExpect(status().isBadRequest());
        verifyNoInteractions(service);
    }

    @Test void transitionsAndConflict() throws Exception {
        Views.InvoiceView issued = new Views.InvoiceView(7L, "Acme", InvoiceStatus.ISSUED, List.of(), 0, Instant.parse("2026-11-09T12:00:00Z"));
        when(service.issue(7L)).thenReturn(issued);
        when(service.pay(7L)).thenThrow(new ConflictException("Invoice 7 cannot move from DRAFT to PAID"));
        when(service.cancel(7L)).thenReturn(issued);
        mvc.perform(post("/api/invoices/7/issue")).andExpect(status().isOk()).andExpect(jsonPath("$.status").value("ISSUED"));
        mvc.perform(post("/api/invoices/7/pay")).andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value("Invoice 7 cannot move from DRAFT to PAID"));
        mvc.perform(post("/api/invoices/7/void")).andExpect(status().isOk());
        mvc.perform(post("/api/invoices/-1/issue")).andExpect(status().isBadRequest());
    }

    @Test void revenueLiteralPathBeatsIdPattern() throws Exception {
        when(service.revenue()).thenReturn(List.of(new CustomerTotal("Acme", 200)));
        mvc.perform(get("/api/invoices/revenue")).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customer").value("Acme")).andExpect(jsonPath("$[0].totalCents").value(200));
    }
}
