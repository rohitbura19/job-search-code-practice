package practice.e;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(StockController.class)
@TestPropertySource(properties = {"stock.max-page-size=10", "stock.low-threshold=7"})
class StockControllerTest {
    @Autowired MockMvc mvc;
    @MockBean StockService service;

    String body(String sku, Object qty) { return "{\"sku\":" + (sku == null ? "null" : "\"" + sku + "\"") + ",\"qty\":" + qty + "}"; }

    @Test void reserveOk() throws Exception {
        when(service.reserve("AAA-1", 3)).thenReturn(new StockView("AAA-1", 10, 3, 7));
        mvc.perform(post("/api/stock/reservations").contentType(MediaType.APPLICATION_JSON).content(body("AAA-1", 3)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sku").value("AAA-1"))
                .andExpect(jsonPath("$.available").value(7));
    }

    @Test void reserveValidation() throws Exception {
        for (String bad : new String[] {body("", 1), body(null, 1), body("aaa-1", 1), body("AB", 1), body("AAA-1", 0), body("AAA-1", 101), body("AAA-1", -4)}) {
            mvc.perform(post("/api/stock/reservations").contentType(MediaType.APPLICATION_JSON).content(bad))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentTypeCompatibleWith("application/problem+json"));
        }
    }

    @Test void reserveMalformedJsonIs400() throws Exception {
        mvc.perform(post("/api/stock/reservations").contentType(MediaType.APPLICATION_JSON).content("{nope"))
                .andExpect(status().isBadRequest());
    }

    @Test void outOfStockIs409Problem() throws Exception {
        when(service.reserve("AAA-1", 9)).thenThrow(new OutOfStockException("AAA-1", 9));
        mvc.perform(post("/api/stock/reservations").contentType(MediaType.APPLICATION_JSON).content(body("AAA-1", 9)))
                .andExpect(status().isConflict())
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.title").value("Not enough stock"))
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test void unknownSkuIs404Problem() throws Exception {
        when(service.reserve("ZZZ-9", 1)).thenThrow(new StockNotFoundException("ZZZ-9"));
        when(service.get("ZZZ-9")).thenThrow(new StockNotFoundException("ZZZ-9"));
        mvc.perform(post("/api/stock/reservations").contentType(MediaType.APPLICATION_JSON).content(body("ZZZ-9", 1)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Stock item not found"));
        mvc.perform(get("/api/stock/ZZZ-9")).andExpect(status().isNotFound());
    }

    @Test void getBySku() throws Exception {
        when(service.get("AAA-1")).thenReturn(new StockView("AAA-1", 10, 4, 6));
        mvc.perform(get("/api/stock/AAA-1")).andExpect(status().isOk()).andExpect(jsonPath("$.onHand").value(10));
    }

    @Test void lowStockUsesConfiguredDefaultsAndCapsPageSize() throws Exception {
        when(service.lowStock(eq(7), any(Pageable.class))).thenReturn(new PageImpl<>(List.of(new StockView("AAA-1", 10, 9, 1)), PageRequest.of(0, 10), 1));
        mvc.perform(get("/api/stock/low?size=1000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].sku").value("AAA-1"))
                .andExpect(jsonPath("$.total").value(1))
                .andExpect(jsonPath("$.page").value(0));
        ArgumentCaptor<Pageable> cap = ArgumentCaptor.forClass(Pageable.class);
        verify(service).lowStock(eq(7), cap.capture());
        assertEquals(10, cap.getValue().getPageSize());     // max-page-size from the test property
        assertEquals(0, cap.getValue().getPageNumber());
    }

    @Test void lowStockExplicitThresholdAndPage() throws Exception {
        when(service.lowStock(eq(2), any(Pageable.class))).thenReturn(new PageImpl<>(List.of(), PageRequest.of(3, 4), 0));
        mvc.perform(get("/api/stock/low?threshold=2&page=3&size=4")).andExpect(status().isOk());
        ArgumentCaptor<Pageable> cap = ArgumentCaptor.forClass(Pageable.class);
        verify(service).lowStock(eq(2), cap.capture());
        assertEquals(PageRequest.of(3, 4), cap.getValue());
    }

    @Test void badPagingParametersAre400() throws Exception {
        for (String q : new String[] {"page=-1", "size=0", "threshold=-5", "size=abc"}) {
            mvc.perform(get("/api/stock/low?" + q)).andExpect(status().isBadRequest());
        }
    }
}
