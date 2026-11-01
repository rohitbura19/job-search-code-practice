package practice.e;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** "/tags" is a literal mapping and wins over "/{id}"; a non-numeric id gives a TypeMismatch -> 400 through ResponseEntityExceptionHandler. */
@RestController
@RequestMapping("/api/articles")
public class ArticleController {
    private final ArticleService service;

    public ArticleController(ArticleService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<ArticleView> create(@Valid @RequestBody ArticleRequest req) {
        ArticleView v = service.add(req);
        return ResponseEntity.created(URI.create("/api/articles/" + v.id())).body(v);
    }

    @GetMapping("/{id}")
    public ArticleView get(@PathVariable long id) { return service.get(id); }

    @GetMapping
    public PagedResponse<ArticleView> byTag(@RequestParam String tag,
                                            @RequestParam(defaultValue = "0") @Min(0) int page,
                                            @RequestParam(defaultValue = "20") @Min(1) int size) {
        return service.byTag(tag, page, size);
    }

    @GetMapping("/tags")
    public List<TagCountView> tags() { return service.tagCounts(); }
}
