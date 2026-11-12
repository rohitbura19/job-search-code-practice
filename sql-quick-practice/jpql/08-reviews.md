# 08. reviews - repository queries

_From daily-practice/2026-10-29-thu. > Project: `e-spring-reviews/` (own `pom.xml`). Tests: `ReviewRepositoryTest` (`@DataJpaTest`), `ReviewControllerTest` (`@WebMvcTest`), `ReviewServiceTest` (Mockito + fixed `Clock`), `ReviewPropsTest` (`ApplicationContextRunner`)._

Write the annotation + query for every TODO (JPQL unless it says native). Answers: `daily-practice/2026-10-29-thu/answers/`.

## Entity `Review`

```java
    private Long id;
    private String productId;
    private String author;
    private int rating;
    private String text;
    private ReviewStatus status = ReviewStatus.PUBLISHED;
    private Instant createdAt;
```

## `ReviewRepository`

```java
public interface ReviewRepository extends JpaRepository<Review, Long> {
    // TODO: derived query: the product's reviews with this status, paged (the caller supplies the sort).
    Page<Review> findByProductIdAndStatus(String productId, ReviewStatus status, Pageable pageable);
    // TODO: derived existence query on productId + author.
    boolean existsByProductIdAndAuthor(String productId, String author);
    // TODO: @Query (JPQL, constructor expression `select new practice.e.RatingSummary(...)`): count and avg(rating) of the PUBLISHED
    //       reviews of one product (named parameter). Empty Optional when the product has none (group by makes no row).
    Optional<RatingSummary> summary(String productId);
    // TODO: a bulk UPDATE query that sets the status of ONE review by id and returns the number of rows changed. It bypasses the
    //       persistence context: what must you add so a later find in the same transaction does not return the stale entity?
    int setStatus(Long id, ReviewStatus status);
}
```
