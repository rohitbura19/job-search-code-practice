package practice.e;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@WebMvcTest(DeviceController.class)
class DeviceControllerTest {
    static final String PROBLEM = "application/problem+json";
    static final DeviceView VIEW = new DeviceView("AB-123455", "Charger", "1.2.3", Channel.STABLE, DeviceStatus.ACTIVE, Instant.parse("2026-10-27T04:00:00Z"));
    static final String OK_BODY = "{\"serial\":\"AB-123455\",\"model\":\"Charger\",\"firmwareVersion\":\"1.2.3\",\"channel\":\"STABLE\"}";

    @Autowired MockMvc mvc;
    @MockBean DeviceService service;

    @Test void registerIs201WithLocation() throws Exception {
        when(service.register(any())).thenReturn(VIEW);
        mvc.perform(post("/api/devices").contentType(MediaType.APPLICATION_JSON).content(OK_BODY))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/devices/AB-123455"))
                .andExpect(jsonPath("$.serial").value("AB-123455"))
                .andExpect(jsonPath("$.channel").value("STABLE"));
    }

    @Test void invalidBodyIs400ProblemWithPerFieldErrors() throws Exception {
        mvc.perform(post("/api/devices").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"serial\":\"AB-123456\",\"model\":\" \",\"firmwareVersion\":\"1.x\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(PROBLEM))
                .andExpect(jsonPath("$.title").value("Invalid request"))
                .andExpect(jsonPath("$.errors.serial").exists())
                .andExpect(jsonPath("$.errors.model").exists())
                .andExpect(jsonPath("$.errors.firmwareVersion").exists())
                .andExpect(jsonPath("$.errors.channel").exists());
        verifyNoInteractions(service);
    }

    @Test void malformedJsonIs400Problem() throws Exception {
        mvc.perform(post("/api/devices").contentType(MediaType.APPLICATION_JSON).content("{not json"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(PROBLEM));
    }

    @Test void duplicateSerialIs409FromEitherTheCheckOrTheConstraint() throws Exception {
        when(service.register(any())).thenThrow(new DeviceConflictException("AB-123455"));
        mvc.perform(post("/api/devices").contentType(MediaType.APPLICATION_JSON).content(OK_BODY))
                .andExpect(status().isConflict()).andExpect(content().contentTypeCompatibleWith(PROBLEM));
        doThrow(new DataIntegrityViolationException("uk_device_serial")).when(service).register(any());
        mvc.perform(post("/api/devices").contentType(MediaType.APPLICATION_JSON).content(OK_BODY))
                .andExpect(status().isConflict()).andExpect(content().contentTypeCompatibleWith(PROBLEM));
    }

    @Test void getFoundAndNotFound() throws Exception {
        when(service.get("AB-123455")).thenReturn(VIEW);
        when(service.get("ZZ-000000")).thenThrow(new DeviceNotFoundException("ZZ-000000"));
        mvc.perform(get("/api/devices/AB-123455")).andExpect(status().isOk()).andExpect(jsonPath("$.model").value("Charger"));
        mvc.perform(get("/api/devices/ZZ-000000")).andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(PROBLEM)).andExpect(jsonPath("$.status").value(404));
    }

    @Test void nullFieldsAreOmittedFromJson() throws Exception {
        when(service.get("AB-123455")).thenReturn(new DeviceView("AB-123455", "Charger", "1.2.3", Channel.STABLE, DeviceStatus.ACTIVE, null));
        mvc.perform(get("/api/devices/AB-123455")).andExpect(jsonPath("$.lastSeen").doesNotExist());
    }

    @Test void patchPassesOnlyWhatWasSent() throws Exception {
        when(service.patch(eq("AB-123455"), any())).thenReturn(VIEW);
        mvc.perform(patch("/api/devices/AB-123455").contentType(MediaType.APPLICATION_JSON).content("{\"channel\":\"BETA\"}"))
                .andExpect(status().isOk());
        ArgumentCaptor<PatchDeviceRequest> c = ArgumentCaptor.forClass(PatchDeviceRequest.class);
        verify(service).patch(eq("AB-123455"), c.capture());
        assertNull(c.getValue().model());
        assertEquals(Channel.BETA, c.getValue().channel());
    }

    @Test void patchWithATooLongOrEmptyModelIs400() throws Exception {
        for (String model : List.of("\"\"", "\"" + "x".repeat(41) + "\"")) {
            mvc.perform(patch("/api/devices/AB-123455").contentType(MediaType.APPLICATION_JSON).content("{\"model\":" + model + "}"))
                    .andExpect(status().isBadRequest()).andExpect(jsonPath("$.errors.model").exists());
        }
        verifyNoInteractions(service);
    }

    @Test void listDefaultsToActiveAndEmptyFragment() throws Exception {
        when(service.list("", DeviceStatus.ACTIVE)).thenReturn(List.of(VIEW));
        mvc.perform(get("/api/devices")).andExpect(status().isOk()).andExpect(jsonPath("$[0].serial").value("AB-123455"));
        when(service.list("gate", DeviceStatus.RETIRED)).thenReturn(List.of());
        mvc.perform(get("/api/devices").param("model", "gate").param("status", "RETIRED")).andExpect(status().isOk()).andExpect(jsonPath("$").isEmpty());
    }

    @Test void unknownStatusIs400Problem() throws Exception {
        mvc.perform(get("/api/devices").param("status", "FOO"))
                .andExpect(status().isBadRequest()).andExpect(content().contentTypeCompatibleWith(PROBLEM));
    }

    @Test void retireStaleDefaultsTo90DaysAndReturnsTheCount() throws Exception {
        when(service.retireStale(90)).thenReturn(3);
        mvc.perform(post("/api/devices/maintenance/retire-stale")).andExpect(status().isOk()).andExpect(jsonPath("$.retired").value(3));
        when(service.retireStale(7)).thenReturn(0);
        mvc.perform(post("/api/devices/maintenance/retire-stale").param("days", "7")).andExpect(jsonPath("$.retired").value(0));
    }

    @Test void nonPositiveDaysIs400Problem() throws Exception {
        mvc.perform(post("/api/devices/maintenance/retire-stale").param("days", "0"))
                .andExpect(status().isBadRequest()).andExpect(content().contentTypeCompatibleWith(PROBLEM));
        verify(service, never()).retireStale(0);
    }
}
