package practice.e;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Unique-url check-then-insert is racy: the unique index is the real guard; catching DataIntegrityViolationException would close the gap.
 * The view is built inside the transaction because the tags are lazy.
 */
@Service
public class ArticleService {
    private final ArticleRepository repo;
    private final ReadingProps props;

    public ArticleService(ArticleRepository repo, ReadingProps props) { this.repo = repo; this.props = props; }

    @Transactional
    @CacheEvict(value = "tagCounts", allEntries = true)
    public ArticleView add(ArticleRequest req) {
        Set<String> tags = req.tags() == null ? Set.of()
                : req.tags().stream().map(t -> t.trim().toLowerCase(Locale.ROOT)).collect(Collectors.toSet());
        if (tags.size() > props.maxTags()) throw new TooManyTagsException(props.maxTags());
        if (repo.existsByUrl(req.url())) throw new DuplicateUrlException(req.url());
        return ArticleView.of(repo.save(new Article(req.url(), req.title(), tags)));
    }

    @Transactional(readOnly = true)
    public ArticleView get(long id) {
        return repo.findById(id).map(ArticleView::of).orElseThrow(() -> new ArticleNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public PagedResponse<ArticleView> byTag(String tag, int page, int size) {
        var pageable = PageRequest.of(page, Math.min(size, props.maxPageSize()), Sort.by("id"));
        return PagedResponse.of(repo.findByTag(tag.trim().toLowerCase(Locale.ROOT), pageable).map(ArticleView::of));
    }

    @Transactional(readOnly = true)
    @Cacheable("tagCounts")
    public List<TagCountView> tagCounts() {
        return repo.tagCounts().stream().map(TagCountView::of).toList();
    }
}
