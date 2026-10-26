package practice.e;

import java.util.List;
import org.springframework.data.domain.Page;

/** A stable JSON shape for a page (do not serialise Spring's PageImpl: its structure is not a contract). */
public record PageView<T>(List<T> items, int page, int size, long totalElements, int totalPages) {
    static <S, T> PageView<T> of(Page<S> page, java.util.function.Function<S, T> mapper) {
        return new PageView<>(page.getContent().stream().map(mapper).toList(), page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages());
    }
}
