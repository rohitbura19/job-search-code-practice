package practice.e;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CouponController.class)
class CouponControllerTest {
    static final String PROBLEM = "application/problem+json";
    static final String FUTURE = LocalDate.now().plusYears(1).toString();

    @Autowired MockMvc mvc;
    @MockBean CouponService service;

    static String body(String code, String pct, String until, String max) {
        return "{\"code\":" + code + ",\"percentOff\":" + pct + ",\"validUntil\":" + until + ",\"maxRedemptions\":" + max + "}";
    }

    @Test void createReturns201WithLocation() throws Exception {
        when(service.create(any())).thenReturn(new CouponView("SAVE10", 10, LocalDate.parse(FUTURE), 5));
        mvc.perform(post("/api/coupons").contentType(MediaType.APPLICATION_JSON).content(body("\"SAVE10\"", "10", "\"" + FUTURE + "\"", "5")))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/coupons/SAVE10"))
                .andExpect(jsonPath("$.code").value("SAVE10"))
                .andExpect(jsonPath("$.redemptionsLeft").value(5));
    }

    @Test void invalidBodyListsEveryBadField() throws Exception {
        mvc.perform(post("/api/coupons").contentType(MediaType.APPLICATION_JSON).content(body("\"bad code\"", "0", "\"2020-01-01\"", "-1")))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(PROBLEM))
                .andExpect(jsonPath("$.title").value("Validation failed"))
                .andExpect(jsonPath("$.errors[*].field", containsInAnyOrder("code", "percentOff", "validUntil", "maxRedemptions")));
        verifyNoInteractions(service);
    }

    @Test void percentAbove100AndMissingFieldsAreReported() throws Exception {
        mvc.perform(post("/api/coupons").contentType(MediaType.APPLICATION_JSON).content(body("\"SAVE10\"", "101", "\"" + FUTURE + "\"", "5")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[*].field", containsInAnyOrder("percentOff")));
        mvc.perform(post("/api/coupons").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[*].field", containsInAnyOrder("code", "percentOff", "validUntil", "maxRedemptions")));
        verifyNoInteractions(service);
    }

    @Test void businessFailuresBecomeProblemJsonWithTheirStatus() throws Exception {
        when(service.create(any())).thenThrow(CouponException.duplicate("SAVE10"));
        mvc.perform(post("/api/coupons").contentType(MediaType.APPLICATION_JSON).content(body("\"SAVE10\"", "10", "\"" + FUTURE + "\"", "5")))
                .andExpect(status().isConflict())
                .andExpect(content().contentTypeCompatibleWith(PROBLEM))
                .andExpect(jsonPath("$.title").value("Duplicate code"));
        when(service.redeem("OLDONE")).thenThrow(CouponException.expired("OLDONE"));
        mvc.perform(post("/api/coupons/OLDONE/redeem")).andExpect(status().isGone()).andExpect(jsonPath("$.title").value("Coupon expired"));
        when(service.redeem("NOSUCH")).thenThrow(CouponException.notFound("NOSUCH"));
        mvc.perform(post("/api/coupons/NOSUCH/redeem")).andExpect(status().isNotFound());
        when(service.get("FULL")).thenThrow(CouponException.exhausted("FULL"));
        mvc.perform(get("/api/coupons/FULL")).andExpect(status().isConflict()).andExpect(content().contentTypeCompatibleWith(PROBLEM));
    }

    @Test void redeemAndGetReturnTheView() throws Exception {
        when(service.redeem("SAVE10")).thenReturn(new CouponView("SAVE10", 10, LocalDate.parse(FUTURE), 4));
        mvc.perform(post("/api/coupons/SAVE10/redeem")).andExpect(status().isOk()).andExpect(jsonPath("$.redemptionsLeft").value(4));
        when(service.get("SAVE10")).thenReturn(new CouponView("SAVE10", 10, LocalDate.parse(FUTURE), 4));
        mvc.perform(get("/api/coupons/SAVE10")).andExpect(status().isOk()).andExpect(jsonPath("$.percentOff").value(10));
    }

    @Test void badCodeInThePathIs400AndNeverReachesTheService() throws Exception {
        mvc.perform(post("/api/coupons/save10/redeem")).andExpect(status().isBadRequest()).andExpect(content().contentTypeCompatibleWith(PROBLEM));
        mvc.perform(get("/api/coupons/AB")).andExpect(status().isBadRequest());
        verifyNoInteractions(service);
    }

    @Test void illegalArgumentIs400() throws Exception {
        when(service.get("BOOM1")).thenThrow(new IllegalArgumentException("nope"));
        mvc.perform(get("/api/coupons/BOOM1")).andExpect(status().isBadRequest()).andExpect(jsonPath("$.title").value("Invalid request"));
    }
}
