package practice.e;

import java.util.List;
import org.springframework.http.ResponseEntity;

/**
 * TODO: REST controller under /api/articles (add class-level and method-level annotations).
 *   POST /                    body ArticleRequest (validated) -> 201 + Location: /api/articles/{id} + ArticleView
 *   GET  /{id}                -> ArticleView
 *   GET  /?tag&page&size      tag required; page >= 0 (default 0); size >= 1 (default 20) -> PagedResponse; violations -> 400 problem+json
 *   GET  /tags                -> List<TagCountView>
 * Do NOT put @Validated on the class: Spring 6.1 validates @Min/@Max on handler parameters itself (HandlerMethodValidationException).
 */
public class ArticleController {
    private final ArticleService service;

    public ArticleController(ArticleService service) { this.service = service; }

    public ResponseEntity<ArticleView> create(ArticleRequest req) { throw new UnsupportedOperationException("TODO"); }
    public ArticleView get(long id) { throw new UnsupportedOperationException("TODO"); }
    public PagedResponse<ArticleView> byTag(String tag, int page, int size) { throw new UnsupportedOperationException("TODO"); }
    public List<TagCountView> tags() { throw new UnsupportedOperationException("TODO"); }
}
