# 11. reading-list - repository queries

_From daily-practice/2026-11-01-sun. E. Spring Boot: reading-list API with an element collection (30 min)_

Write the annotation + query for every TODO (JPQL unless it says native). Answers: `daily-practice/2026-11-01-sun/answers/`.

## Entity `Article`

```java
```

## `ArticleRepository`

```java
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
```
