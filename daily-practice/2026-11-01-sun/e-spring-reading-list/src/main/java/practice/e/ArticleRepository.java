package practice.e;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * TODO: existsByUrl is derived and given; finish the rest (annotations and parameter names):
 *   findByTag:      @Query JPQL: articles that have the tag (join over the element collection); pageable, so Spring needs a count query too
 *   tagCounts:      @Query JPQL returning the TagCount projection: select the tag value as "tag" and count(articles) as "total",
 *                   group by tag, order by total desc then tag asc
 *   findTop3ByOrderByIdDesc: derived; the 3 newest articles WITH their tags loaded in the same query (@EntityGraph)
 */
public interface ArticleRepository extends JpaRepository<Article, Long> {
    boolean existsByUrl(String url);

    Page<Article> findByTag(String tag, Pageable pageable);

    List<TagCount> tagCounts();

    List<Article> findTop3ByOrderByIdDesc();
}
