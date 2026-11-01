package practice.e;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/** The join over the element collection returns each article once per matching tag value; one tag value => once. countQuery is derived from the main query. */
public interface ArticleRepository extends JpaRepository<Article, Long> {
    boolean existsByUrl(String url);

    @Query("select a from Article a join a.tags t where t = :tag")
    Page<Article> findByTag(@Param("tag") String tag, Pageable pageable);

    @Query("select t as tag, count(a) as total from Article a join a.tags t group by t order by count(a) desc, t asc")
    List<TagCount> tagCounts();

    @EntityGraph(attributePaths = "tags")
    List<Article> findTop3ByOrderByIdDesc();
}
