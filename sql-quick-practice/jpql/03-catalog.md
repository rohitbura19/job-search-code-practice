# 03. catalog - repository queries

_From daily-practice/2026-10-24-sat. > Project: `e-spring-catalog/` (own `pom.xml`). Tests: `ProductRepositoryTest` (`@DataJpaTest`), `ProductControllerTest` (`@WebMvcTest`), `ProductServiceTest` (Mockito), `CatalogPropertiesTest` (`ApplicationContextRunner`)._

Write the annotation + query for every TODO (JPQL unless it says native). Answers: `daily-practice/2026-10-24-sat/answers/`.

## Entity `Product`

```java
    private Long id;
    private String sku;
    private String name;
    private String category;
    private long priceCents;
    private boolean active = true;
```

## `ProductRepository`

```java
// TODO: complete the four methods (annotations and queries; the signatures are given):
//   1. findByCategoryAndActiveTrue: a DERIVED query, paged; no @Query needed (the name already says it).
//   2. existsBySku: derived.
//   3. countByCategory: per category the number of ACTIVE products, ordered by category, as the CategoryCount projection
//      (JPQL with aliases `category` and `total` that match the projection getters). Careful: a method named countBy... is parsed as
//      a derived query unless it has @Query.
//   4. deactivate: ONE conditional update (active = false where id = :id and active = true) returning the rows changed; it must be
//      @Modifying; make the next read in the same transaction see the change (clearAutomatically / flushAutomatically).
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByCategoryAndActiveTrue(String category, Pageable pageable);
    boolean existsBySku(String sku);
    List<CategoryCount> countByCategory();
    int deactivate(long id);
}
```
