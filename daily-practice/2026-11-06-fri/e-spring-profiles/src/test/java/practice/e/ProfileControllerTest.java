package practice.e;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ProfileController.class)
class ProfileControllerTest {
    @Autowired MockMvc mvc;
    @MockBean ProfileService service;

    private static final String BODY = "{\"email\":\"ann@x.com\",\"displayName\":\"Ann\",\"city\":\"Pune\",\"country\":\"IN\"}";

    private static ProfileView view(long version) { return new ProfileView(7L, "ann@x.com", "Ann", "Pune", "IN", version); }

    @Test void createReturns201WithLocationAndEtag() throws Exception {
        when(service.create(any())).thenReturn(view(0));
        mvc.perform(post("/profiles").contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/profiles/7"))
                .andExpect(header().string("ETag", "\"0\""))
                .andExpect(jsonPath("$.email").value("ann@x.com"));
    }

    @Test void invalidBodyIs400ProblemDetail() throws Exception {
        mvc.perform(post("/profiles").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"nope\",\"displayName\":\"\",\"city\":\"Pune\",\"country\":\"IND\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON));
        verifyNoInteractions(service);
    }

    @Test void duplicateEmailIs409() throws Exception {
        when(service.create(any())).thenThrow(new Exceptions.DuplicateEmailException("ann@x.com"));
        mvc.perform(post("/profiles").contentType(MediaType.APPLICATION_JSON).content(BODY)).andExpect(status().isConflict());
    }

    @Test void getReturnsEtag() throws Exception {
        when(service.get(7)).thenReturn(view(4));
        mvc.perform(get("/profiles/7")).andExpect(status().isOk()).andExpect(header().string("ETag", "\"4\""));
    }

    @Test void getMissingIs404() throws Exception {
        when(service.get(8)).thenThrow(new Exceptions.ProfileNotFoundException(8));
        mvc.perform(get("/profiles/8")).andExpect(status().isNotFound());
    }

    @Test void patchWithoutIfMatchIs428() throws Exception {
        mvc.perform(patch("/profiles/7").contentType(MediaType.APPLICATION_JSON).content("{\"city\":\"Delhi\"}"))
                .andExpect(status().isPreconditionRequired());
        verifyNoInteractions(service);
    }

    @Test void patchParsesQuotedAndWeakIfMatch() throws Exception {
        when(service.patch(eq(7L), eq(3L), any())).thenReturn(view(4));
        mvc.perform(patch("/profiles/7").header("If-Match", "\"3\"").contentType(MediaType.APPLICATION_JSON).content("{\"city\":\"Delhi\"}"))
                .andExpect(status().isOk()).andExpect(header().string("ETag", "\"4\""));
        mvc.perform(patch("/profiles/7").header("If-Match", "W/\"3\"").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isOk());
    }

    @Test void patchStaleIs412AndBadHeaderIs400() throws Exception {
        when(service.patch(eq(7L), eq(1L), any())).thenThrow(new Exceptions.StaleVersionException(7, 1, 2));
        mvc.perform(patch("/profiles/7").header("If-Match", "\"1\"").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isPreconditionFailed());
        mvc.perform(patch("/profiles/7").header("If-Match", "abc").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test void patchValidatesPresentFields() throws Exception {
        mvc.perform(patch("/profiles/7").header("If-Match", "1").contentType(MediaType.APPLICATION_JSON).content("{\"displayName\":\"\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test void searchPassesFiltersAndCapsPageSize() throws Exception {
        when(service.search(any(), any(), any())).thenReturn(new PageImpl<>(List.of(view(0))));
        mvc.perform(get("/profiles").param("city", "Pune").param("q", "an").param("size", "1000"))
                .andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value(7));
        ArgumentCaptor<Pageable> pg = ArgumentCaptor.forClass(Pageable.class);
        verify(service).search(eq("Pune"), eq("an"), pg.capture());
        org.junit.jupiter.api.Assertions.assertEquals(50, pg.getValue().getPageSize(), "max-page-size property");
        mvc.perform(get("/profiles")).andExpect(status().isOk());
        verify(service, times(2)).search(any(), any(), pg.capture());
        org.junit.jupiter.api.Assertions.assertEquals(20, pg.getValue().getPageSize(), "default-page-size property");
    }
}
