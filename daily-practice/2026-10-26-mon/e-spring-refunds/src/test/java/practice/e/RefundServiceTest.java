package practice.e;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

class RefundServiceTest {
    static final Instant NOW = Instant.parse("2026-10-26T10:15:30Z");
    RefundRepository repo = mock(RefundRepository.class);
    RefundService service;

    @BeforeEach void setUp() {
        service = new RefundService(repo, new RefundProperties(10_000, 1_000), Clock.fixed(NOW, ZoneOffset.UTC));
        when(repo.saveAndFlush(any())).thenAnswer(i -> i.getArgument(0));
    }

    static CreateRefundRequest req(String pay, long amt) { return new CreateRefundRequest(pay, amt, "damaged"); }
    static Refund existing(String pay, long amt, String key) { return new Refund(pay, amt, null, key, RefundStatus.PENDING, NOW); }

    @Test void tooLargeIsRejectedBeforeAnyRepositoryCall() {
        RefundException e = assertThrows(RefundException.class, () -> service.create("key-12345678", req("p", 10_001)));
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, e.status());
        verifyNoInteractions(repo);
        assertDoesNotThrow(() -> service.create("key-12345678", req("p", 10_000)));
    }

    @Test void smallRefundsAreAutoApprovedLargerOnesWait() {
        ArgumentCaptor<Refund> saved = ArgumentCaptor.forClass(Refund.class);
        CreateResult small = service.create("key-00000001", req("p", 1_000));
        CreateResult big = service.create("key-00000002", req("p", 1_001));
        verify(repo, times(2)).saveAndFlush(saved.capture());
        assertEquals(RefundStatus.APPROVED, saved.getAllValues().get(0).getStatus(), "the threshold itself is auto-approved");
        assertEquals(RefundStatus.PENDING, saved.getAllValues().get(1).getStatus());
        assertFalse(small.replayed());
        assertFalse(big.replayed());
        assertEquals(NOW, saved.getAllValues().get(0).getCreatedAt(), "time comes from the injected Clock");
        assertEquals("key-00000001", saved.getAllValues().get(0).getIdempotencyKey());
    }

    @Test void sameKeySameRequestIsAReplayAndSavesNothing() {
        when(repo.findByIdempotencyKey("key-12345678")).thenReturn(Optional.of(existing("p", 500, "key-12345678")));
        CreateResult r = service.create("key-12345678", req("p", 500));
        assertTrue(r.replayed());
        assertEquals(500, r.view().amountPaise());
        verify(repo, never()).saveAndFlush(any());
    }

    @Test void sameKeyDifferentRequestIsAConflict() {
        when(repo.findByIdempotencyKey("key-12345678")).thenReturn(Optional.of(existing("p", 500, "key-12345678")));
        RefundException e1 = assertThrows(RefundException.class, () -> service.create("key-12345678", req("p", 501)));
        assertEquals(HttpStatus.CONFLICT, e1.status());
        assertEquals("Idempotency key reused", e1.title());
        assertThrows(RefundException.class, () -> service.create("key-12345678", req("other", 500)));
    }

    @Test void aRaceOnTheUniqueKeyIsAnsweredFromTheWinner() {
        when(repo.findByIdempotencyKey("key-12345678")).thenReturn(Optional.empty(), Optional.of(existing("p", 500, "key-12345678")));
        when(repo.saveAndFlush(any())).thenThrow(new DataIntegrityViolationException("uk_refund_idem"));
        assertTrue(service.create("key-12345678", req("p", 500)).replayed());
        when(repo.findByIdempotencyKey("key-87654321")).thenReturn(Optional.empty(), Optional.of(existing("p", 500, "key-87654321")));
        assertThrows(RefundException.class, () -> service.create("key-87654321", req("p", 999)));
    }

    @Test void decideApprovesOrRejectsPendingOnly() {
        Refund r = existing("p", 5, "k");
        when(repo.findById(7L)).thenReturn(Optional.of(r));
        assertEquals(RefundStatus.REJECTED, service.decide(7, false).status());
        RefundException again = assertThrows(RefundException.class, () -> service.decide(7, true));
        assertEquals(HttpStatus.CONFLICT, again.status());
        assertEquals("Already decided", again.title());
        assertEquals(HttpStatus.NOT_FOUND, assertThrows(RefundException.class, () -> service.decide(8, true)).status());
    }

    @Test void getAndList() {
        when(repo.findById(1L)).thenReturn(Optional.of(existing("p", 5, "k")));
        assertEquals("p", service.get(1).paymentId());
        assertThrows(RefundException.class, () -> service.get(2));
        Pageable pg = PageRequest.of(1, 2);
        when(repo.findByPaymentId("p", pg)).thenReturn(new PageImpl<>(List.of(existing("p", 5, "k")), pg, 3));
        when(repo.findAll(pg)).thenReturn(new PageImpl<>(List.of(), pg, 0));
        PageView<RefundView> v = service.list("p", pg);
        assertEquals(1, v.items().size());
        assertEquals(1, v.page());
        assertEquals(2, v.size());
        assertEquals(3, v.totalElements());
        assertEquals(2, v.totalPages());
        assertTrue(service.list(null, pg).items().isEmpty());
        verify(repo).findAll(pg);
    }

    @Test void transactionBoundaries() throws Exception {
        assertTrue(RefundService.class.isAnnotationPresent(Service.class));
        assertNull(RefundService.class.getMethod("create", String.class, CreateRefundRequest.class).getAnnotation(Transactional.class),
                "create must not be one big transaction");
        Transactional decide = RefundService.class.getMethod("decide", long.class, boolean.class).getAnnotation(Transactional.class);
        assertNotNull(decide);
        assertFalse(decide.readOnly());
        assertTrue(RefundService.class.getMethod("get", long.class).getAnnotation(Transactional.class).readOnly());
        assertTrue(RefundService.class.getMethod("list", String.class, Pageable.class).getAnnotation(Transactional.class).readOnly());
    }
}
