package practice.e;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

class CouponServiceTest {
    static final LocalDate TODAY = LocalDate.of(2026, 10, 25);
    CouponRepository repo = mock(CouponRepository.class);
    CouponService service;

    @BeforeEach void setUp() {
        Clock clock = Clock.fixed(Instant.parse("2026-10-25T10:15:30Z"), ZoneOffset.UTC);
        service = new CouponService(repo, new CouponProperties(50, 100), clock);
    }

    static CreateCouponRequest req(String code, int pct, int max) { return new CreateCouponRequest(code, pct, TODAY.plusDays(3), max); }

    @Test void createRejectsTooGenerousAndTooManyBeforeTouchingTheRepository() {
        CouponException e1 = assertThrows(CouponException.class, () -> service.create(req("BIGSALE", 51, 5)));
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, e1.status());
        assertEquals("Discount too high", e1.title());
        CouponException e2 = assertThrows(CouponException.class, () -> service.create(req("BIGSALE", 10, 101)));
        assertEquals("Too many redemptions", e2.title());
        verifyNoInteractions(repo);
        when(repo.saveAndFlush(any())).thenAnswer(i -> i.getArgument(0));
        assertDoesNotThrow(() -> service.create(req("EDGE", 50, 100)));
    }

    @Test void createRejectsKnownCodeWithoutSaving() {
        when(repo.existsByCode("SAVE10")).thenReturn(true);
        CouponException e = assertThrows(CouponException.class, () -> service.create(req("SAVE10", 10, 5)));
        assertEquals(HttpStatus.CONFLICT, e.status());
        verify(repo, never()).saveAndFlush(any());
    }

    @Test void createMapsTheUniqueConstraintRaceToDuplicate() {
        when(repo.saveAndFlush(any())).thenThrow(new DataIntegrityViolationException("uk_coupon_code"));
        CouponException e = assertThrows(CouponException.class, () -> service.create(req("SAVE10", 10, 5)));
        assertEquals("Duplicate code", e.title());
    }

    @Test void createReturnsTheView() {
        when(repo.saveAndFlush(any())).thenAnswer(i -> i.getArgument(0));
        CouponView v = service.create(req("SAVE10", 10, 5));
        assertEquals("SAVE10", v.code());
        assertEquals(5, v.redemptionsLeft());
    }

    @Test void redeemUsesTheClockAndReturnsTheFreshView() {
        Coupon after = new Coupon("SAVE10", 10, TODAY.plusDays(1), 5);
        when(repo.redeem("SAVE10", TODAY)).thenReturn(1);
        when(repo.findByCode("SAVE10")).thenReturn(Optional.of(after));
        assertEquals("SAVE10", service.redeem("SAVE10").code());
        verify(repo).redeem("SAVE10", TODAY);
    }

    @Test void redeemFailureIsClassified() {
        when(repo.redeem(any(), any())).thenReturn(0);
        when(repo.findByCode("NOSUCH")).thenReturn(Optional.empty());
        when(repo.findByCode("OLDONE")).thenReturn(Optional.of(new Coupon("OLDONE", 5, TODAY.minusDays(1), 5)));
        when(repo.findByCode("LASTDAY")).thenReturn(Optional.of(new Coupon("LASTDAY", 5, TODAY, 5)));
        assertEquals(HttpStatus.NOT_FOUND, assertThrows(CouponException.class, () -> service.redeem("NOSUCH")).status());
        assertEquals(HttpStatus.GONE, assertThrows(CouponException.class, () -> service.redeem("OLDONE")).status());
        CouponException exhausted = assertThrows(CouponException.class, () -> service.redeem("LASTDAY"));
        assertEquals(HttpStatus.CONFLICT, exhausted.status());
        assertEquals("Coupon exhausted", exhausted.title(), "valid today (inclusive), so the reason is the limit");
    }

    @Test void getFindsOrFails() {
        when(repo.findByCode("A1B2")).thenReturn(Optional.of(new Coupon("A1B2", 5, TODAY, 3)));
        assertEquals(3, service.get("A1B2").redemptionsLeft());
        assertEquals(HttpStatus.NOT_FOUND, assertThrows(CouponException.class, () -> service.get("ZZZZ")).status());
    }

    @Test void isAServiceWithTransactionalBoundaries() throws Exception {
        assertTrue(CouponService.class.isAnnotationPresent(Service.class));
        Method create = CouponService.class.getMethod("create", CreateCouponRequest.class);
        Method redeem = CouponService.class.getMethod("redeem", String.class);
        Method get = CouponService.class.getMethod("get", String.class);
        for (Method m : new Method[]{create, redeem}) {
            Transactional t = m.getAnnotation(Transactional.class);
            assertNotNull(t, m.getName() + " is transactional");
            assertFalse(t.readOnly());
        }
        Transactional ro = get.getAnnotation(Transactional.class);
        assertNotNull(ro);
        assertTrue(ro.readOnly());
    }
}
