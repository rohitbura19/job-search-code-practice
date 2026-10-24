package practice.e;

import java.util.List;

// TODO: make this a REST controller under /api/products; no logic here, only delegation:
//   POST   /api/products          valid body -> 201 + Location /api/products/{id} + the ProductView
//   GET    /api/products?category=&page=&size=   category required and not blank; page defaults to 0 and must be >= 0; size optional, >= 1.
//                                  A violation -> 400 problem+json and the service is never called. Do NOT put @Validated on the class:
//                                  in Spring 6.1 the parameter constraints are checked by MVC itself (what exception does it throw?).
//   GET    /api/products/summary  the per-category totals
//   DELETE /api/products/{id}     204; 404 for an unknown id comes from the exception handler
public class ProductController {
    private final ProductService service;

    public ProductController(ProductService service) { this.service = service; }

    public Object create(CreateProductRequest request) { return null; }

    public PageView<ProductView> list(String category, int page, Integer size) { return null; }

    public List<CategoryCount> summary() { return null; }

    public void deactivate(long id) {}
}
