package practice.e;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

class OrderServiceTest {
    static final Instant NOW = Instant.parse("2026-10-28T04:00:00Z");
    PurchaseOrderRepository repo = mock(PurchaseOrderRepository.class);
    OrderService svc = new OrderService(repo, Clock.fixed(NOW, ZoneOffset.UTC));

    static CreateOrderRequest req() {
        return new CreateOrderRequest("ORD-000001", "asha", List.of(new LineRequest("a", 2, 500), new LineRequest("b", 1, 1000)));
    }

    @Test void createBuildsTheAggregateAndStampsTheClock() {
        when(repo.existsByOrderNo("ORD-000001")).thenReturn(false);
        when(repo.save(any())).thenAnswer(i -> i.getArgument(0));
        OrderView v = svc.create(req());
        ArgumentCaptor<PurchaseOrder> cap = ArgumentCaptor.forClass(PurchaseOrder.class);
        verify(repo).save(cap.capture());
        assertEquals(NOW, cap.getValue().getCreatedAt());
        assertEquals(2, cap.getValue().getLines().size());
        assertSame(cap.getValue(), cap.getValue().getLines().get(0).getOrder(), "both sides of the association are set");
        assertEquals(2000, v.totalPaise());
        assertEquals(OrderStatus.NEW, v.status());
        assertEquals(List.of(new LineView("a", 2, 500), new LineView("b", 1, 1000)), v.lines());
    }

    @Test void createRejectsDuplicates() {
        when(repo.existsByOrderNo("ORD-000001")).thenReturn(true);
        assertThrows(DuplicateOrderException.class, () -> svc.create(req()));
        verify(repo, never()).save(any());
    }

    @Test void getAndCancelUnknownAre404() {
        when(repo.findByOrderNo("ORD-404404")).thenReturn(Optional.empty());
        assertThrows(OrderNotFoundException.class, () -> svc.get("ORD-404404"));
        assertThrows(OrderNotFoundException.class, () -> svc.cancel("ORD-404404"));
    }

    @Test void cancelChangesTheManagedEntityWithoutSave() {
        PurchaseOrder o = new PurchaseOrder("ORD-000002", "ben", NOW);
        when(repo.findByOrderNo("ORD-000002")).thenReturn(Optional.of(o));
        assertEquals(OrderStatus.CANCELLED, svc.cancel("ORD-000002").status());
        verify(repo, never()).save(any());
        assertThrows(IllegalStateTransitionException.class, () -> svc.cancel("ORD-000002"));
    }

    @Test void listUsesAllStatusesWhenNoneGivenAndSortsNewestFirst() {
        when(repo.findByCustomerIdAndStatusIn(any(), any(), any())).thenReturn(new PageImpl<>(List.of(new PurchaseOrder("ORD-000003", "asha", NOW))));
        Page<OrderSummary> p = svc.list("asha", Set.of(), 2, 10);
        assertEquals("ORD-000003", p.getContent().get(0).orderNo());
        ArgumentCaptor<Pageable> pageable = ArgumentCaptor.forClass(Pageable.class);
        @SuppressWarnings("unchecked") ArgumentCaptor<java.util.Collection<OrderStatus>> statuses = ArgumentCaptor.forClass(java.util.Collection.class);
        verify(repo).findByCustomerIdAndStatusIn(org.mockito.ArgumentMatchers.eq("asha"), statuses.capture(), pageable.capture());
        assertEquals(EnumSet.allOf(OrderStatus.class), Set.copyOf(statuses.getValue()));
        assertEquals(2, pageable.getValue().getPageNumber());
        assertEquals(10, pageable.getValue().getPageSize());
        assertEquals("createdAt: DESC,id: DESC", pageable.getValue().getSort().toString());
    }

    @Test void transactionBoundaries() throws Exception {
        assertTrue(OrderService.class.getAnnotation(Transactional.class).readOnly(), "class level must be readOnly");
        Transactional onCreate = OrderService.class.getMethod("create", CreateOrderRequest.class).getAnnotation(Transactional.class);
        Transactional onCancel = OrderService.class.getMethod("cancel", String.class).getAnnotation(Transactional.class);
        assertNotNull(onCreate);
        assertFalse(onCreate.readOnly());
        assertNotNull(onCancel);
        assertFalse(onCancel.readOnly());
    }
}
