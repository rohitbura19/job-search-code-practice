# 12. wallet - repository queries

_From daily-practice/2026-11-02-mon. E. Spring Boot: wallet API with idempotent, deadlock-free transfers (30 min)_

Write the annotation + query for every TODO (JPQL unless it says native). Answers: `daily-practice/2026-11-02-mon/answers/`.

## Entity `Account`

```java
@Entity
@Table(name = "accounts", uniqueConstraints = @UniqueConstraint(name = "uk_account_owner", columnNames = "owner"))
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, length = 40) private String owner;
    @Column(nullable = false, precision = 19, scale = 2) private BigDecimal balance;
    @Version private long version;
```

## `AccountRepository`

```java
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByOwner(String owner);
    // TODO: findByIdForUpdate(Long id): SELECT ... FOR UPDATE through a PESSIMISTIC_WRITE lock and an explicit JPQL @Query with @Param.
    Optional<Account> findByIdForUpdate(Long id);
}
```
