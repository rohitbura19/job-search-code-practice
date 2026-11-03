package practice.e;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BookingController.class)
class BookingControllerTest {
    static final String BODY = """
            {"room":"A","startAt":"2026-12-01T10:00:00Z","endAt":"2026-12-01T11:00:00Z","bookedBy":"a@x.com"}""";
    @Autowired MockMvc mvc;
    @MockBean BookingService service;

    @Test void createReturns201WithLocation() throws Exception {
        given(service.create(any())).willReturn(new BookingView(5L, "A", Instant.parse("2026-12-01T10:00:00Z"), Instant.parse("2026-12-01T11:00:00Z"), "a@x.com"));
        mvc.perform(post("/api/bookings").contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/bookings/5"))
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.room").value("A"));
    }

    @Test void invalidBodiesAre400() throws Exception {
        for (String bad : List.of(
                BODY.replace("\"A\"", "\" \""),                                              // blank room
                BODY.replace("a@x.com", "not-an-email"),
                BODY.replace("2026-12-01T11:00:00Z", "2026-12-01T09:00:00Z"),                // end before start
                BODY.replace("2026-12-01T11:00:00Z", "2026-12-01T10:00:00Z"),                // empty interval
                "{\"room\":\"A\"}",                                                          // missing fields
                BODY.replace("\"A\"", "\"" + "x".repeat(41) + "\""))) {
            mvc.perform(post("/api/bookings").contentType(MediaType.APPLICATION_JSON).content(bad)).andExpect(status().isBadRequest());
        }
    }

    @Test void overlapIs409ProblemDetail() throws Exception {
        given(service.create(any())).willThrow(new BookingExceptions.Overlap("A"));
        mvc.perform(post("/api/bookings").contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isConflict())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.title").value("Room already booked"))
                .andExpect(jsonPath("$.detail").value("room A is already booked in that period"));
    }

    @Test void tooLongAndInPastAre422() throws Exception {
        given(service.create(any())).willThrow(new BookingExceptions.TooLong(Duration.ofHours(4)));
        mvc.perform(post("/api/bookings").contentType(MediaType.APPLICATION_JSON).content(BODY)).andExpect(status().isUnprocessableEntity());
        org.mockito.BDDMockito.willThrow(new BookingExceptions.InPast()).given(service).create(any());
        mvc.perform(post("/api/bookings").contentType(MediaType.APPLICATION_JSON).content(BODY)).andExpect(status().isUnprocessableEntity());
    }

    @Test void listReturnsBodyAndTotalHeader() throws Exception {
        Instant since = Instant.parse("2026-12-01T00:00:00Z");
        BookingView v = new BookingView(1L, "A", since.plusSeconds(60), since.plusSeconds(120), "a@x.com");
        given(service.upcoming("A", since, 0, 20)).willReturn(new PageImpl<>(List.of(v), PageRequest.of(0, 20), 41));
        mvc.perform(get("/api/bookings").param("room", "A").param("since", "2026-12-01T00:00:00Z"))
                .andExpect(status().isOk())
                .andExpect(header().string("X-Total-Count", "41"))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test void listPassesPagingParamsAndRequiresRoomAndSince() throws Exception {
        given(service.upcoming(eq("B"), any(), eq(3), eq(5))).willReturn(new PageImpl<>(List.of()));
        mvc.perform(get("/api/bookings").param("room", "B").param("since", "2026-12-01T00:00:00Z").param("page", "3").param("size", "5"))
                .andExpect(status().isOk()).andExpect(header().string("X-Total-Count", "0"));
        mvc.perform(get("/api/bookings").param("since", "2026-12-01T00:00:00Z")).andExpect(status().isBadRequest());
        mvc.perform(get("/api/bookings").param("room", "B")).andExpect(status().isBadRequest());
    }

    @Test void cancelIs204AndMissingIs404() throws Exception {
        mvc.perform(delete("/api/bookings/9")).andExpect(status().isNoContent());
        org.mockito.Mockito.doThrow(new BookingExceptions.NotFound(10L)).when(service).cancel(10L);
        mvc.perform(delete("/api/bookings/10")).andExpect(status().isNotFound()).andExpect(jsonPath("$.status").value(404));
    }
}
