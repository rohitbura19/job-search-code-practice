package practice.e;

import java.util.List;

/**
 * TODO: add the Spring annotations and the logic.
 *   add(req): normalise tags (trim, lower-case, distinct; null = none); more than props.maxTags -> TooManyTagsException;
 *             url already saved -> DuplicateUrlException (do not save); otherwise save and return the view. Clears the "tagCounts" cache.
 *   get(id): view or ArticleNotFoundException.
 *   byTag(tag, page, size): tag is trimmed + lower-cased; size is capped at props.maxPageSize; sorted by id ascending.
 *   tagCounts(): maps the projection to TagCountView; cached in "tagCounts" (map to plain records: never cache projection proxies).
 * Writes run in a read-write transaction, reads in read-only ones.
 */
public class ArticleService {
    private final ArticleRepository repo;
    private final ReadingProps props;

    public ArticleService(ArticleRepository repo, ReadingProps props) { this.repo = repo; this.props = props; }

    public ArticleView add(ArticleRequest req) { throw new UnsupportedOperationException("TODO"); }
    public ArticleView get(long id) { throw new UnsupportedOperationException("TODO"); }
    public PagedResponse<ArticleView> byTag(String tag, int page, int size) { throw new UnsupportedOperationException("TODO"); }
    public List<TagCountView> tagCounts() { throw new UnsupportedOperationException("TODO"); }
}
