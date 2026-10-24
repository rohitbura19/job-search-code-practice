package practice.e;

import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private final ProductRepository repo;
    private final CatalogProperties props;

    public ProductService(ProductRepository repo, CatalogProperties props) {
        this.repo = repo;
        this.props = props;
    }

    @Transactional
    public ProductView create(CreateProductRequest r) {
        // existsBySku gives the friendly path; the unique constraint is the real guard against two concurrent creates.
        if (repo.existsBySku(r.sku())) throw new DuplicateSkuException(r.sku());
        try {
            return ProductView.of(repo.saveAndFlush(new Product(r.sku(), r.name(), r.category(), r.priceCents())));
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateSkuException(r.sku());
        }
    }

    @Transactional(readOnly = true)
    public PageView<ProductView> list(String category, int page, Integer size) {
        int effective = size == null ? props.defaultPageSize() : Math.min(size, props.maxPageSize());
        var result = repo.findByCategoryAndActiveTrue(category, PageRequest.of(page, effective, Sort.by("name").and(Sort.by("id"))));
        return new PageView<>(result.map(ProductView::of).getContent(), page, effective, result.getTotalElements());
    }

    @Transactional(readOnly = true)
    public List<CategoryCount> summary() { return repo.countByCategory(); }

    /** Idempotent: deactivating an already inactive product succeeds; only an unknown id is an error. */
    @Transactional
    public void deactivate(long id) {
        if (repo.deactivate(id) == 0 && !repo.existsById(id)) throw new ProductNotFoundException(id);
    }
}
