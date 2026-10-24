package practice.e;

import java.util.List;

// TODO: make this a Spring service; writes are transactional, reads are read-only transactions.
public class ProductService {
    private final ProductRepository repo;
    private final CatalogProperties props;

    public ProductService(ProductRepository repo, CatalogProperties props) {
        this.repo = repo;
        this.props = props;
    }

    /**
     * Save a new active product and return its view. A known sku -> DuplicateSkuException WITHOUT saving; and if two requests race past
     * that check, the unique constraint (DataIntegrityViolationException from saveAndFlush) must also surface as DuplicateSkuException.
     */
    public ProductView create(CreateProductRequest request) { throw new UnsupportedOperationException("TODO"); }

    /**
     * One page of the active products of a category, sorted by name then id (a unique tie-breaker keeps pages stable).
     * size == null -> props.defaultPageSize(); a larger size is clamped to props.maxPageSize(). PageView.size is the EFFECTIVE size.
     */
    public PageView<ProductView> list(String category, int page, Integer size) { throw new UnsupportedOperationException("TODO"); }

    public List<CategoryCount> summary() { throw new UnsupportedOperationException("TODO"); }

    /** Idempotent soft delete: an already inactive product is fine; only an id that does not exist throws ProductNotFoundException. */
    public void deactivate(long id) { throw new UnsupportedOperationException("TODO"); }
}
