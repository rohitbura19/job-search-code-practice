package practice.e;

import java.util.List;
import org.springframework.data.domain.Page;

public record PagedResponse<T>(List<T> content, int page, int size, long total) {
    public static <T> PagedResponse<T> of(Page<T> p) { return new PagedResponse<>(p.getContent(), p.getNumber(), p.getSize(), p.getTotalElements()); }
}
