package practice.e;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(WalletController.class)
class WalletControllerTest {
    @Autowired MockMvc mvc;
    @MockBean WalletService service;

    static final String TRANSFER = "{\"fromId\":1,\"toId\":2,\"amount\":25.50}";

    private static TransferView view() {
        return new TransferView(7L, "k-1", 1L, 2L, new BigDecimal("25.50"), Instant.parse("2026-11-02T00:00:00Z"));
    }

    @Test void openAccountReturns201WithLocation() throws Exception {
        when(service.createAccount(any())).thenReturn(new AccountView(5L, "asha", new BigDecimal("100.00")));
        mvc.perform(post("/api/accounts").contentType(MediaType.APPLICATION_JSON).content("{\"owner\":\"asha\",\"openingBalance\":100.00}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/accounts/5"))
                .andExpect(jsonPath("$.owner").value("asha"));
    }

    @Test void openAccountValidation() throws Exception {
        for (String body : new String[]{"{\"owner\":\"\"}", "{}", "{\"owner\":\"x\",\"openingBalance\":-1}",
                "{\"owner\":\"x\",\"openingBalance\":1.234}", "{\"owner\":\"" + "o".repeat(41) + "\"}"}) {
            mvc.perform(post("/api/accounts").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isBadRequest());
        }
        verifyNoInteractions(service);
    }

    @Test void getAccountAndNotFoundProblemDetail() throws Exception {
        when(service.getAccount(5L)).thenReturn(new AccountView(5L, "asha", BigDecimal.TEN));
        when(service.getAccount(9L)).thenThrow(new WalletExceptions.AccountNotFound(9L));
        mvc.perform(get("/api/accounts/5")).andExpect(status().isOk()).andExpect(jsonPath("$.balance").value(10));
        mvc.perform(get("/api/accounts/9")).andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test void newTransferIs201AndReplayIs200() throws Exception {
        when(service.transfer(eq("k-1"), any())).thenReturn(new TransferOutcome(view(), true));
        when(service.transfer(eq("k-2"), any())).thenReturn(new TransferOutcome(view(), false));
        mvc.perform(post("/api/transfers").header("Idempotency-Key", "k-1").contentType(MediaType.APPLICATION_JSON).content(TRANSFER))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.id").value(7));
        mvc.perform(post("/api/transfers").header("Idempotency-Key", "k-2").contentType(MediaType.APPLICATION_JSON).content(TRANSFER))
                .andExpect(status().isOk());
    }

    @Test void transferRequestValidation() throws Exception {
        for (String body : new String[]{"{\"fromId\":1,\"toId\":2,\"amount\":0}", "{\"fromId\":1,\"toId\":2,\"amount\":-5}",
                "{\"fromId\":1,\"toId\":2,\"amount\":1.001}", "{\"toId\":2,\"amount\":5}", "{\"fromId\":1,\"amount\":5}",
                "{\"fromId\":1,\"toId\":2}", "not json"}) {
            mvc.perform(post("/api/transfers").header("Idempotency-Key", "k").contentType(MediaType.APPLICATION_JSON).content(body))
                    .andExpect(status().isBadRequest());
        }
        verifyNoInteractions(service);
    }

    @Test void idempotencyKeyHeaderIsRequiredAndBounded() throws Exception {
        mvc.perform(post("/api/transfers").contentType(MediaType.APPLICATION_JSON).content(TRANSFER)).andExpect(status().isBadRequest());
        mvc.perform(post("/api/transfers").header("Idempotency-Key", " ").contentType(MediaType.APPLICATION_JSON).content(TRANSFER))
                .andExpect(status().isBadRequest());
        mvc.perform(post("/api/transfers").header("Idempotency-Key", "k".repeat(65)).contentType(MediaType.APPLICATION_JSON).content(TRANSFER))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(service);
    }

    @Test void domainErrorsMapToStatuses() throws Exception {
        when(service.transfer(eq("insufficient"), any())).thenThrow(new WalletExceptions.InsufficientFunds("low"));
        when(service.transfer(eq("conflict"), any())).thenThrow(new WalletExceptions.IdempotencyConflict("conflict"));
        when(service.transfer(eq("invalid"), any())).thenThrow(new WalletExceptions.InvalidTransfer("same account"));
        when(service.transfer(eq("missing"), any())).thenThrow(new WalletExceptions.AccountNotFound(2L));
        when(service.transfer(eq("race"), any())).thenThrow(new org.springframework.dao.DataIntegrityViolationException("uk_transfer_key"));
        String[][] cases = {{"insufficient", "409"}, {"conflict", "409"}, {"invalid", "422"}, {"missing", "404"}, {"race", "409"}};
        for (String[] c : cases) {
            mvc.perform(post("/api/transfers").header("Idempotency-Key", c[0]).contentType(MediaType.APPLICATION_JSON).content(TRANSFER))
                    .andExpect(status().is(Integer.parseInt(c[1])))
                    .andExpect(jsonPath("$.status").value(Integer.parseInt(c[1])));
        }
    }

    @Test void duplicateOwnerIs409() throws Exception {
        when(service.createAccount(any())).thenThrow(new WalletExceptions.DuplicateOwner("asha"));
        mvc.perform(post("/api/accounts").contentType(MediaType.APPLICATION_JSON).content("{\"owner\":\"asha\"}"))
                .andExpect(status().isConflict());
    }
}
