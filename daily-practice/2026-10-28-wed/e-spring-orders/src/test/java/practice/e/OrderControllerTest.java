package practice.e;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(OrderController.class)
class OrderControllerTest {
    static final String PROBLEM = "application/problem+json";
    static final OrderView VIEW = new OrderView("ORD-000001", "asha", OrderStatus.NEW, 2000, List.of(new LineView("a", 2, 500), new LineView("b", 1, 1000)));
    static final String OK_BODY = "{\"orderNo\":\"ORD-000001\",\"customerId\":\"asha\",\"lines\":[{\"sku\":\"a\",\"quantity\":2,\"unitPricePaise\":500}]}";

    @Autowired MockMvc mvc;
    @MockBean OrderService service;

    @Test void createIs201WithLocation() throws Exception {
        when(service.create(any())).thenReturn(VIEW);
        mvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON).content(OK_BODY))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/orders/ORD-000001"))
                .andExpect(jsonPath("$.totalPaise").value(2000))
                .andExpect(jsonPath("$.lines.length()").value(2))
                .andExpect(jsonPath("$.status").value("NEW"));
    }

    @Test void invalidNestedLineIs400WithFieldPaths() throws Exception {
        mvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"orderNo\":\"ORD-12\",\"customerId\":\" \",\"lines\":[{\"sku\":\"a\",\"quantity\":0,\"unitPricePaise\":500},{\"sku\":\"\",\"quantity\":1,\"unitPricePaise\":-5}]}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(PROBLEM))
                .andExpect(jsonPath("$.errors.orderNo").exists())
                .andExpect(jsonPath("$.errors.customerId").exists())
                .andExpect(jsonPath("$.errors['lines[0].quantity']").exists())
                .andExpect(jsonPath("$.errors['lines[1].sku']").exists())
                .andExpect(jsonPath("$.errors['lines[1].unitPricePaise']").exists());
        verifyNoInteractions(service);
    }

    @Test void emptyLinesAndMalformedJson() throws Exception {
        mvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON).content("{\"orderNo\":\"ORD-000001\",\"customerId\":\"a\",\"lines\":[]}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.lines").exists());
        mvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON).content("{nope"))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(service);
    }

    @Test void unknownOrderIs404Problem() throws Exception {
        when(service.get("ORD-000404")).thenThrow(new OrderNotFoundException("ORD-000404"));
        mvc.perform(get("/api/orders/ORD-000404"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(PROBLEM))
                .andExpect(jsonPath("$.detail").value("order ORD-000404 not found"));
    }

    @Test void conflictsAre409() throws Exception {
        when(service.create(any())).thenThrow(new DuplicateOrderException("ORD-000001"));
        mvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON).content(OK_BODY)).andExpect(status().isConflict());
        doThrow(new DataIntegrityViolationException("could not execute statement; SQL [insert into purchase_order ...]")).when(service).create(any());
        mvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON).content(OK_BODY))
                .andExpect(status().isConflict())
                .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("insert into"))));
        doThrow(new IllegalStateTransitionException("ORD-000009", OrderStatus.SHIPPED, OrderStatus.CANCELLED)).when(service).cancel("ORD-000009");
        mvc.perform(post("/api/orders/ORD-000009/cancel")).andExpect(status().isConflict());
    }

    @Test void cancelReturnsTheView() throws Exception {
        when(service.cancel("ORD-000001")).thenReturn(new OrderView("ORD-000001", "asha", OrderStatus.CANCELLED, 2000, List.of()));
        mvc.perform(post("/api/orders/ORD-000001/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));
    }

    @Test void listPassesFiltersAndClampsSizeToTheConfiguredMax() throws Exception {
        when(service.list(any(), any(), anyInt(), anyInt())).thenReturn(new PageImpl<>(List.of(new OrderSummary("ORD-000003", OrderStatus.NEW, Instant.parse("2026-10-28T04:03:00Z"))), PageRequest.of(1, 50), 51));
        mvc.perform(get("/api/orders").param("customerId", "asha").param("status", "NEW", "PAID").param("page", "1").param("size", "9999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].orderNo").value("ORD-000003"))
                .andExpect(jsonPath("$.totalElements").value(51))
                .andExpect(jsonPath("$.totalPages").value(2));
        verify(service).list(eq("asha"), eq(Set.of(OrderStatus.NEW, OrderStatus.PAID)), eq(1), eq(50));
    }

    @Test void listDefaultsAndLowerClamp() throws Exception {
        when(service.list(any(), any(), anyInt(), anyInt())).thenReturn(new PageImpl<>(List.of()));
        mvc.perform(get("/api/orders").param("customerId", "asha")).andExpect(status().isOk());
        verify(service).list("asha", Set.of(), 0, 20);
        mvc.perform(get("/api/orders").param("customerId", "asha").param("size", "0").param("page", "-3")).andExpect(status().isOk());
        verify(service).list("asha", Set.of(), 0, 1);
    }

    @Test void listBadInputIs400() throws Exception {
        mvc.perform(get("/api/orders")).andExpect(status().isBadRequest());
        mvc.perform(get("/api/orders").param("customerId", "asha").param("status", "BOGUS")).andExpect(status().isBadRequest());
        verifyNoInteractions(service);
    }

    @Test void topCustomersClampsN() throws Exception {
        when(service.topCustomers(anyInt())).thenReturn(List.of(new CustomerTotal("ben", 3000)));
        mvc.perform(get("/api/orders/top-customers")).andExpect(status().isOk()).andExpect(jsonPath("$[0].customerId").value("ben")).andExpect(jsonPath("$[0].totalPaise").value(3000));
        verify(service).topCustomers(5);
        mvc.perform(get("/api/orders/top-customers").param("n", "500")).andExpect(status().isOk());
        verify(service).topCustomers(20);
    }
}
