package practice.e;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ShipmentController.class)
class ShipmentControllerTest {
    @Autowired MockMvc mvc;
    @MockBean ShipmentService service;

    static ShipmentView view(long id) { return new ShipmentView(id, "o" + id, "addr", ShipmentStatus.CREATED, "TRK-o" + id, Instant.parse("2026-11-05T10:00:00Z")); }

    @Test void createReturns201WithLocation() throws Exception {
        when(service.create(any())).thenReturn(view(7));
        mvc.perform(post("/api/shipments").contentType(MediaType.APPLICATION_JSON).content("{\"orderId\":\"o7\",\"address\":\"addr\"}"))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", "/api/shipments/7"))
            .andExpect(jsonPath("$.id").value(7))
            .andExpect(jsonPath("$.status").value("CREATED"));
        verify(service).create(new CreateShipmentRequest("o7", "addr"));
    }

    @Test void invalidBodyIs400ProblemDetailAndServiceIsNotCalled() throws Exception {
        String longAddress = "x".repeat(201);
        for (String body : List.of("{}", "{\"orderId\":\" \",\"address\":\"a\"}", "{\"orderId\":\"o\",\"address\":\"" + longAddress + "\"}", "{\"orderId\":\"" + "o".repeat(41) + "\",\"address\":\"a\"}")) {
            mvc.perform(post("/api/shipments").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
        }
        verifyNoInteractions(service);
    }

    @Test void duplicateIs409() throws Exception {
        when(service.create(any())).thenThrow(new Exceptions.DuplicateOrderException("o7"));
        mvc.perform(post("/api/shipments").contentType(MediaType.APPLICATION_JSON).content("{\"orderId\":\"o7\",\"address\":\"a\"}"))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.detail").value("Order o7 already has a shipment"));
    }

    @Test void getOneAndNotFound() throws Exception {
        when(service.get(3L)).thenReturn(view(3));
        when(service.get(4L)).thenThrow(new Exceptions.ShipmentNotFoundException(4L));
        mvc.perform(get("/api/shipments/3")).andExpect(status().isOk()).andExpect(jsonPath("$.orderId").value("o3"));
        mvc.perform(get("/api/shipments/4")).andExpect(status().isNotFound()).andExpect(jsonPath("$.status").value(404));
    }

    @Test void searchPassesDefaultsAndFilters() throws Exception {
        when(service.search(any(), eq(0), eq(20))).thenReturn(new ShipmentPage(List.of(view(1)), 1, 0, 20));
        mvc.perform(get("/api/shipments")).andExpect(status().isOk()).andExpect(jsonPath("$.content[0].id").value(1)).andExpect(jsonPath("$.total").value(1));
        verify(service).search(null, 0, 20);
        mvc.perform(get("/api/shipments").param("status", "DELIVERED").param("page", "2").param("size", "5")).andExpect(status().isOk());
        verify(service).search(ShipmentStatus.DELIVERED, 2, 5);
    }

    @Test void unknownStatusFilterIs400() throws Exception {
        mvc.perform(get("/api/shipments").param("status", "TELEPORTED")).andExpect(status().isBadRequest());
    }
}
