package practice.e;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {
    @Mock AccountRepository accounts;
    @Mock TransferRepository transfers;
    WalletService service;
    final Clock clock = Clock.fixed(Instant.parse("2026-11-02T10:00:00Z"), ZoneOffset.UTC);
    Account a1 = new Account("asha", new BigDecimal("100.00"));
    Account a2 = new Account("bo", new BigDecimal("20.00"));

    @BeforeEach void setUp() {
        service = new WalletService(accounts, transfers, new WalletProps(new BigDecimal("1000.00")), clock);
    }

    private static TransferRequest req(long from, long to, String amount) { return new TransferRequest(from, to, new BigDecimal(amount)); }

    private void stubAccounts() {
        when(accounts.findByIdForUpdate(1L)).thenReturn(Optional.of(a1));
        when(accounts.findByIdForUpdate(2L)).thenReturn(Optional.of(a2));
        when(transfers.findByIdempotencyKey(any())).thenReturn(Optional.empty());
        lenient().when(transfers.save(any())).thenAnswer(i -> i.getArgument(0));
    }

    @Test void movesMoneyAndStampsTime() {
        stubAccounts();
        TransferOutcome out = service.transfer("k", req(1, 2, "30.25"));
        assertThat(out.created()).isTrue();
        assertThat(a1.getBalance()).isEqualByComparingTo("69.75");
        assertThat(a2.getBalance()).isEqualByComparingTo("50.25");
        assertThat(out.view().createdAt()).isEqualTo(Instant.parse("2026-11-02T10:00:00Z"));
        assertThat(out.view().idempotencyKey()).isEqualTo("k");
    }

    @Test void locksLowerIdFirstWhateverTheDirection() {
        stubAccounts();
        service.transfer("k", req(2, 1, "5.00"));
        InOrder order = inOrder(accounts);
        order.verify(accounts).findByIdForUpdate(1L);
        order.verify(accounts).findByIdForUpdate(2L);
        assertThat(a2.getBalance()).isEqualByComparingTo("15.00");   // money moved from 2 to 1
        assertThat(a1.getBalance()).isEqualByComparingTo("105.00");
    }

    @Test void insufficientFundsLeavesBalancesAndSavesNothing() {
        stubAccounts();
        assertThatThrownBy(() -> service.transfer("k", req(2, 1, "20.01"))).isInstanceOf(WalletExceptions.InsufficientFunds.class);
        assertThat(a2.getBalance()).isEqualByComparingTo("20.00");
        verify(transfers, never()).save(any());
    }

    @Test void exactBalanceIsAllowed() {
        stubAccounts();
        service.transfer("k", req(2, 1, "20.00"));
        assertThat(a2.getBalance()).isEqualByComparingTo("0.00");
    }

    @Test void sameAccountAndOverLimitAreInvalidBeforeAnyLock() {
        when(transfers.findByIdempotencyKey(any())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.transfer("k", req(1, 1, "5.00"))).isInstanceOf(WalletExceptions.InvalidTransfer.class);
        assertThatThrownBy(() -> service.transfer("k", req(1, 2, "1000.01"))).isInstanceOf(WalletExceptions.InvalidTransfer.class);
        verifyNoInteractions(accounts);
    }

    @Test void missingAccountIsNotFound() {
        when(transfers.findByIdempotencyKey(any())).thenReturn(Optional.empty());
        when(accounts.findByIdForUpdate(1L)).thenReturn(Optional.of(a1));
        when(accounts.findByIdForUpdate(2L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.transfer("k", req(1, 2, "5.00"))).isInstanceOf(WalletExceptions.AccountNotFound.class);
    }

    @Test void replayReturnsStoredTransferWithoutTouchingAccounts() {
        Transfer stored = new Transfer("k", 1L, 2L, new BigDecimal("30.25"), Instant.EPOCH);
        when(transfers.findByIdempotencyKey("k")).thenReturn(Optional.of(stored));
        TransferOutcome out = service.transfer("k", req(1, 2, "30.250"));   // same amount, different scale
        assertThat(out.created()).isFalse();
        assertThat(out.view().createdAt()).isEqualTo(Instant.EPOCH);
        verifyNoInteractions(accounts);
        verify(transfers, never()).save(any());
    }

    @Test void sameKeyDifferentPayloadConflicts() {
        Transfer stored = new Transfer("k", 1L, 2L, new BigDecimal("30.25"), Instant.EPOCH);
        when(transfers.findByIdempotencyKey("k")).thenReturn(Optional.of(stored));
        assertThatThrownBy(() -> service.transfer("k", req(1, 2, "31.00"))).isInstanceOf(WalletExceptions.IdempotencyConflict.class);
        assertThatThrownBy(() -> service.transfer("k", req(2, 1, "30.25"))).isInstanceOf(WalletExceptions.IdempotencyConflict.class);
    }

    @Test void createAccountRejectsDuplicateOwnerAndDefaultsBalance() {
        when(accounts.findByOwner("asha")).thenReturn(Optional.of(a1));
        assertThatThrownBy(() -> service.createAccount(new AccountRequest("asha", null))).isInstanceOf(WalletExceptions.DuplicateOwner.class);
        when(accounts.findByOwner("cy")).thenReturn(Optional.empty());
        when(accounts.save(any())).thenAnswer(i -> i.getArgument(0));
        assertThat(service.createAccount(new AccountRequest("cy", null)).balance()).isEqualByComparingTo("0");
    }

    @Test void getAccountNotFound() {
        when(accounts.findById(9L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getAccount(9L)).isInstanceOf(WalletExceptions.AccountNotFound.class);
    }
}
