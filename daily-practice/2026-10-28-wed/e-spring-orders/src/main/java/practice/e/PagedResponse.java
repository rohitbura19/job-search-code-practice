package practice.e;

import java.util.List;
import org.springframework.data.domain.Page;

/** Stable JSON for a page (serialising Spring's PageImpl directly is not a supported contract). */
public record PagedResponse<T>(List<T> items, int page, int size, long totalElements, int totalPages) {
    static <T> PagedResponse<T> of(Page<T> p) { return new PagedResponse<>(p.getContent(), p.getNumber(), p.getSize(), p.getTotalElements(), p.getTotalPages()); }
}
