package practice.e;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

class ProductServiceTest {
    ProductRepository repo = mock(ProductRepository.class);
    ProductService service;

    @BeforeEach void setUp() { service = new ProductService(repo, new CatalogProperties(20, 50)); }

    static Product withId(Product p, long id) {
        ReflectionTestUtils.setField(p, "id", id);
        return p;
    }

    static Page<Product> pageOf(long total, Product... items) { return new PageImpl<>(List.of(items), Pageable.ofSize(1), total); }

    Pageable capturedPageable() {
        ArgumentCaptor<Pageable> c = ArgumentCaptor.forClass(Pageable.class);
        verify(repo).findByCategoryAndActiveTrue(eq("tv"), c.capture());
        return c.getValue();
    }

    private static String eq(String s) { return org.mockito.ArgumentMatchers.eq(s); }

    @Test void createRejectsKnownSkuWithoutSaving() {
        when(repo.existsBySku("TV-1")).thenReturn(true);
        assertThrows(DuplicateSkuException.class, () -> service.create(new CreateProductRequest("TV-1", "A", "tv", 5L)));
        verify(repo, never()).saveAndFlush(any());
    }

    @Test void createMapsTheUniqueConstraintRaceToDuplicateSku() {
        when(repo.saveAndFlush(any())).thenThrow(new DataIntegrityViolationException("uk_product_sku"));
        assertThrows(DuplicateSkuException.class, () -> service.create(new CreateProductRequest("TV-1", "A", "tv", 5L)));
    }

    @Test void createReturnsTheView() {
        when(repo.saveAndFlush(any())).thenAnswer(i -> withId(i.getArgument(0), 1L));
        ProductView v = service.create(new CreateProductRequest("TV-1", "Television", "tv", 500L));
        assertEquals("TV-1", v.sku());
        assertEquals(500, v.priceCents());
    }

    @Test void listUsesDefaultSizeWhenAbsentAndClampsToTheMaximum() {
        when(repo.findByCategoryAndActiveTrue(any(), any())).thenReturn(pageOf(0));
        assertEquals(20, service.list("tv", 0, null).size());
        assertEquals(50, service.list("tv", 0, 100_000).size());
        assertEquals(7, service.list("tv", 3, 7).size());
        verify(repo, times(3)).findByCategoryAndActiveTrue(any(), any());
    }

    @Test void listSortsByNameThenIdAndUsesTheRequestedPage() {
        when(repo.findByCategoryAndActiveTrue(any(), any())).thenReturn(pageOf(12, withId(new Product("A-1", "A", "tv", 1), 1L)));
        PageView<ProductView> v = service.list("tv", 2, 5);
        Pageable p = capturedPageable();
        assertEquals(2, p.getPageNumber());
        assertEquals(5, p.getPageSize());
        assertNotNull(p.getSort().getOrderFor("name"));
        assertNotNull(p.getSort().getOrderFor("id"), "a unique tie-breaker keeps pages stable");
        assertEquals(12, v.total());
        assertEquals(1, v.items().size());
    }

    @Test void deactivateIsIdempotentButUnknownIdFails() {
        when(repo.deactivate(1)).thenReturn(1);
        service.deactivate(1);
        when(repo.deactivate(2)).thenReturn(0);
        when(repo.existsById(2L)).thenReturn(true);
        service.deactivate(2);
        when(repo.deactivate(3)).thenReturn(0);
        when(repo.existsById(3L)).thenReturn(false);
        assertThrows(ProductNotFoundException.class, () -> service.deactivate(3));
    }

    @Test void isAServiceWithTransactionalBoundaries() throws Exception {
        assertTrue(ProductService.class.isAnnotationPresent(Service.class));
        for (String name : List.of("create", "deactivate")) {
            Method m = ProductService.class.getMethod(name, name.equals("create") ? CreateProductRequest.class : long.class);
            Transactional t = m.getAnnotation(Transactional.class);
            assertNotNull(t, name + " is transactional");
            assertFalse(t.readOnly());
        }
        Transactional ro = ProductService.class.getMethod("list", String.class, int.class, Integer.class).getAnnotation(Transactional.class);
        assertNotNull(ro);
        assertTrue(ro.readOnly());
    }
}
