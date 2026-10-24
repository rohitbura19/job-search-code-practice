package practice.e;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ProductController.class)
class ProductControllerTest {
    static final String PROBLEM = "application/problem+json";

    @Autowired MockMvc mvc;
    @MockBean ProductService service;

    @Test void createReturns201WithLocation() throws Exception {
        when(service.create(any())).thenReturn(new ProductView(5L, "TV-1", "Television", "tv", 50_000));
        mvc.perform(post("/api/products").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"sku\":\"TV-1\",\"name\":\"Television\",\"category\":\"tv\",\"priceCents\":50000}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/products/5"))
                .andExpect(jsonPath("$.sku").value("TV-1"))
                .andExpect(jsonPath("$.priceCents").value(50000));
    }

    @Test void invalidBodyListsEveryBadField() throws Exception {
        mvc.perform(post("/api/products").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"sku\":\"bad sku\",\"name\":\" \",\"category\":\"tv\",\"priceCents\":0}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(PROBLEM))
                .andExpect(jsonPath("$.title").value("Validation failed"))
                .andExpect(jsonPath("$.errors[*].field", containsInAnyOrder("sku", "name", "priceCents")));
        verifyNoInteractions(service);
    }

    @Test void missingFieldsAreReported() throws Exception {
        mvc.perform(post("/api/products").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[*].field", containsInAnyOrder("sku", "name", "category", "priceCents")));
        verifyNoInteractions(service);
    }

    @Test void duplicateSkuIs409() throws Exception {
        when(service.create(any())).thenThrow(new DuplicateSkuException("TV-1"));
        mvc.perform(post("/api/products").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"sku\":\"TV-1\",\"name\":\"A\",\"category\":\"tv\",\"priceCents\":1}"))
                .andExpect(status().isConflict())
                .andExpect(content().contentTypeCompatibleWith(PROBLEM))
                .andExpect(jsonPath("$.title").value("Duplicate SKU"));
    }

    @Test void listPassesDefaultsAndReturnsThePage() throws Exception {
        when(service.list("tv", 0, null)).thenReturn(new PageView<>(List.of(new ProductView(1L, "TV-1", "A", "tv", 10)), 0, 20, 1));
        mvc.perform(get("/api/products").param("category", "tv"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].sku").value("TV-1"))
                .andExpect(jsonPath("$.size").value(20))
                .andExpect(jsonPath("$.total").value(1));
        verify(service).list("tv", 0, null);
    }

    @Test void listParsesPageAndSize() throws Exception {
        when(service.list("tv", 2, 5)).thenReturn(new PageView<>(List.of(), 2, 5, 11));
        mvc.perform(get("/api/products").param("category", "tv").param("page", "2").param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page").value(2));
    }

    @Test void badParametersAre400ProblemJsonAndNeverReachTheService() throws Exception {
        mvc.perform(get("/api/products")).andExpect(status().isBadRequest()).andExpect(content().contentTypeCompatibleWith(PROBLEM));
        mvc.perform(get("/api/products").param("category", "tv").param("page", "-1")).andExpect(status().isBadRequest());
        mvc.perform(get("/api/products").param("category", "tv").param("size", "0")).andExpect(status().isBadRequest());
        mvc.perform(get("/api/products").param("category", " ")).andExpect(status().isBadRequest());
        mvc.perform(get("/api/products").param("category", "tv").param("page", "x")).andExpect(status().isBadRequest());
        verifyNoInteractions(service);
    }

    @Test void summaryReturnsCategoryTotals() throws Exception {
        CategoryCount c = new CategoryCount() {
            public String getCategory() { return "tv"; }
            public long getTotal() { return 3; }
        };
        when(service.summary()).thenReturn(List.of(c));
        mvc.perform(get("/api/products/summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].category").value("tv"))
                .andExpect(jsonPath("$[0].total").value(3));
    }

    @Test void deactivateIs204AndUnknownIs404() throws Exception {
        mvc.perform(delete("/api/products/5")).andExpect(status().isNoContent());
        verify(service).deactivate(5);
        doThrow(new ProductNotFoundException(9)).when(service).deactivate(9);
        mvc.perform(delete("/api/products/9"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(PROBLEM))
                .andExpect(jsonPath("$.title").value("Product not found"));
    }
}
