package practice.e;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.net.URI;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService service;

    public ProductController(ProductService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<ProductView> create(@Valid @RequestBody CreateProductRequest request) {
        ProductView v = service.create(request);
        return ResponseEntity.created(URI.create("/api/products/" + v.id())).body(v);
    }

    // No @Validated on the class: Spring 6.1 validates @RequestParam constraints itself and throws HandlerMethodValidationException,
    // which ResponseEntityExceptionHandler already maps to a 400 ProblemDetail.
    @GetMapping
    public PageView<ProductView> list(@RequestParam @NotBlank String category,
                                      @RequestParam(defaultValue = "0") @Min(0) int page,
                                      @RequestParam(required = false) @Min(1) Integer size) {
        return service.list(category, page, size);
    }

    @GetMapping("/summary")
    public List<CategoryCount> summary() { return service.summary(); }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivate(@PathVariable long id) { service.deactivate(id); }
}
