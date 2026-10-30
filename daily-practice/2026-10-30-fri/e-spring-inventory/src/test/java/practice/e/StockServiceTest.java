package practice.e;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {
    @Mock StockRepository repo;
    @Mock StockEventPublisher events;
    StockProps props = new StockProps(5, 50, new StockProps.Topics("r", "l", "x"));

    StockService service() { return new StockService(repo, events, props); }

    @Test void reserveSuccessPublishesReservedOnly() {
        when(repo.reserve("AAA-1", 3)).thenReturn(1);
        when(repo.findBySku("AAA-1")).thenReturn(Optional.of(new StockItem("AAA-1", 100, 3)));
        assertEquals(new StockView("AAA-1", 100, 3, 97), service().reserve("AAA-1", 3));
        verify(events).reserved("AAA-1", 3);
        verify(events, never()).lowStock(anyString(), anyInt());
    }

    @Test void reserveNearEmptyAlsoPublishesLowStock() {
        when(repo.reserve("AAA-1", 5)).thenReturn(1);
        when(repo.findBySku("AAA-1")).thenReturn(Optional.of(new StockItem("AAA-1", 10, 5)));   // available 5 == threshold
        service().reserve("AAA-1", 5);
        verify(events).lowStock("AAA-1", 5);
    }

    @Test void reserveRejectedWhenNotEnough() {
        when(repo.reserve("AAA-1", 9)).thenReturn(0);
        when(repo.findBySku("AAA-1")).thenReturn(Optional.of(new StockItem("AAA-1", 10, 5)));
        assertThrows(OutOfStockException.class, () -> service().reserve("AAA-1", 9));
        verify(events, never()).reserved(anyString(), anyInt());
    }

    @Test void reserveUnknownSku() {
        when(repo.reserve("ZZZ-9", 1)).thenReturn(0);
        when(repo.findBySku("ZZZ-9")).thenReturn(Optional.empty());
        assertThrows(StockNotFoundException.class, () -> service().reserve("ZZZ-9", 1));
        verify(events, never()).reserved(anyString(), anyInt());
    }

    @Test void restock() {
        when(repo.restock("AAA-1", 7)).thenReturn(1);
        when(repo.restock("ZZZ-9", 7)).thenReturn(0);
        service().restock("AAA-1", 7);
        assertThrows(StockNotFoundException.class, () -> service().restock("ZZZ-9", 7));
    }

    @Test void getAndLowStockMapToViews() {
        when(repo.findBySku("AAA-1")).thenReturn(Optional.of(new StockItem("AAA-1", 10, 4)));
        when(repo.findBySku("ZZZ-9")).thenReturn(Optional.empty());
        assertEquals(new StockView("AAA-1", 10, 4, 6), service().get("AAA-1"));
        assertThrows(StockNotFoundException.class, () -> service().get("ZZZ-9"));
        when(repo.findLowStock(any(Integer.class), any())).thenReturn(new PageImpl<>(List.of(new StockItem("AAA-1", 10, 9)), PageRequest.of(0, 5), 1));
        assertEquals(List.of(new StockView("AAA-1", 10, 9, 1)), service().lowStock(5, PageRequest.of(0, 5)).getContent());
    }
}
